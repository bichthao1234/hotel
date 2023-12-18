USE [hotel_management]
GO
/****** Object:  StoredProcedure [dbo].[CHECK_OUT_BY_ROOM]    Script Date: 11/1/2023 11:03:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- Tạo Stored Procedure
CREATE PROCEDURE [dbo].[CHECK_OUT_BY_ROOM](
    @ID_CT_PT_LIST VARCHAR(MAX),
    @ID_NV VARCHAR(MAX)
)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION MyTransaction;

        DECLARE @ID_CT_PT BIGINT;
        DECLARE @ID_CT_PT_TABLE TABLE(ID_CT_PT BIGINT);

        INSERT INTO @ID_CT_PT_TABLE
        SELECT CAST(value AS BIGINT)
        FROM STRING_SPLIT(@ID_CT_PT_LIST, ',');

        DECLARE @ID_PT BIGINT;
        SELECT @ID_PT = ID_PT FROM CT_PHIEU_THUE WHERE ID_CT_PT = (SELECT TOP (1) ID_CT_PT FROM @ID_CT_PT_TABLE);

        INSERT INTO HOA_DON (NGAY_LAP, ID_NV, ID_PT)
        VALUES (GETDATE(), @ID_NV, @ID_PT)
        DECLARE @INSERTED_ID_HD BIGINT;
        SELECT @INSERTED_ID_HD = @@identity
        PRINT '@INSERTED_ID_HD = ' + CAST(@INSERTED_ID_HD as VARCHAR)

        DECLARE @ID_CT_PT_CURSOR CURSOR;
        SET @ID_CT_PT_CURSOR = CURSOR FOR SELECT ID_CT_PT FROM @ID_CT_PT_TABLE;
        OPEN @ID_CT_PT_CURSOR;
        FETCH NEXT FROM @ID_CT_PT_CURSOR INTO @ID_CT_PT;

        WHILE @@FETCH_STATUS = 0
            BEGIN
                DECLARE @NGAY_DI DATE;
                SELECT @NGAY_DI = NGAY_DI FROM CT_PHIEU_THUE WHERE ID_CT_PT = @ID_CT_PT;
                IF @NGAY_DI IS NULL
                    BEGIN
                        PRINT 'Start CHECK_OUT...'
                        DECLARE @TONG_TIEN_CT_PHIEU_THUE DECIMAL(10, 2);
                        DECLARE @TONG_TIEN_DA_TRA DECIMAL(10, 2);
                        DECLARE @TONG_TIEN_CON_LAI DECIMAL(10, 2);

                        -- Step 1: lấy thông tin tất cả giá: giá phòng, giá dịch vụ, giá phụ thu
                        DECLARE @TONG_PHU_THU DECIMAL(10, 2);
                        SELECT @TONG_PHU_THU = SUM(DON_GIA * SO_LUONG) FROM CT_PHU_THU WHERE ID_CT_PT = @ID_CT_PT;
                        DECLARE @TONG_DICH_VU DECIMAL(10, 2);
                        SELECT @TONG_DICH_VU = SUM(GIA * SO_LUONG) FROM CT_DICH_VU WHERE ID_CT_PT = @ID_CT_PT;
                        DECLARE @TONG_TIEN_PHONG DECIMAL(10, 2);
                        select @TONG_TIEN_PHONG =
                               CPD.DON_GIA * CPD.SO_LUONG_PHONG_O * DATEDIFF(DAY, ctpt.NGAY_DEN, getdate())
                        from CT_PHIEU_THUE ctpt
                                 inner join PHONG P on P.SO_PHONG = ctpt.SO_PHONG
                                 inner join PHIEU_THUE PT on PT.ID_PT = ctpt.ID_PT
                                 inner join PHIEU_DAT PD on PD.ID_PD = PT.ID_PD
                                 inner join CT_PHIEU_DAT CPD
                                            on PD.ID_PD = CPD.ID_PD AND P.ID_HANG_PHONG = CPD.ID_HANG_PHONG
                        where ctpt.ID_CT_PT = @ID_CT_PT;
                        SELECT @TONG_TIEN_CT_PHIEU_THUE = @TONG_TIEN_PHONG + @TONG_PHU_THU + @TONG_DICH_VU;
                        PRINT '@TONG_TIEN_CT_PHIEU_THUE = ' + CAST(@TONG_TIEN_CT_PHIEU_THUE as VARCHAR)

                        -- Step 2: lấy thông tin những thứ đã trả tiền rồi (tiền cọc, tiền dịch vụ đã thanh toán) và tính số tiền còn lại cần trả
                        DECLARE @TIEN_DAT_COC DECIMAL(10, 2);
                        select @TIEN_DAT_COC = PD.SO_TIEN_COC
                        from CT_PHIEU_THUE ctpt
                                 inner join PHONG P on P.SO_PHONG = ctpt.SO_PHONG
                                 inner join PHIEU_THUE PT on PT.ID_PT = ctpt.ID_PT
                                 inner join PHIEU_DAT PD on PD.ID_PD = PT.ID_PD
                                 inner join CT_PHIEU_DAT CPD
                                            on PD.ID_PD = CPD.ID_PD AND P.ID_HANG_PHONG = CPD.ID_HANG_PHONG
                        where ctpt.ID_CT_PT = @ID_CT_PT;
                        DECLARE @DICH_VU_DA_TRA DECIMAL(10, 2);
                        SELECT @DICH_VU_DA_TRA = SUM(GIA * SO_LUONG)
                        FROM CT_DICH_VU
                        WHERE ID_CT_PT = @ID_CT_PT AND TT_THANH_TOAN = 0;
                        SELECT @TONG_TIEN_DA_TRA = @TIEN_DAT_COC + @DICH_VU_DA_TRA;
                        SELECT @TONG_TIEN_CON_LAI = @TONG_TIEN_CT_PHIEU_THUE - @TONG_TIEN_DA_TRA;
                        PRINT '@TONG_TIEN_CON_LAI = ' + CAST(@TONG_TIEN_CON_LAI as VARCHAR)

                        -- Step 3: update status của các chi tiết thành Đã thanh toán (CT_PHIEU_THUE, CT_PHU_THU, CT_DICH_VU)
                        UPDATE CT_PHU_THU SET TT_THANH_TOAN = 1 WHERE ID_CT_PT = @ID_CT_PT;
                        UPDATE CT_DICH_VU SET TT_THANH_TOAN = 1 WHERE ID_CT_PT = @ID_CT_PT;

                        -- Step 4: tạo record hóa đơn vào table HOA_DON để xuất hóa đơn
                        UPDATE CT_PHIEU_THUE
                        SET ID_HD         = @INSERTED_ID_HD,
                            NGAY_DI       = GETDATE(),
                            TT_THANH_TOAN = 1
                        WHERE ID_CT_PT = @ID_CT_PT;

                        -- Step 5: update status phòng thành Dơ
                        DECLARE @SO_PHONG VARCHAR(20);
                        SELECT @SO_PHONG = SO_PHONG FROM CT_PHIEU_THUE WHERE ID_CT_PT = @ID_CT_PT;
                        UPDATE PHONG
                        SET ID_TT = 'RS00003'
                        WHERE SO_PHONG = @SO_PHONG;

                        COMMIT TRANSACTION MyTransaction;
                    END

                FETCH NEXT FROM @ID_CT_PT_CURSOR INTO @ID_CT_PT;
            END
    END TRY
    BEGIN CATCH
        PRINT 'An error occurred: ' + ERROR_MESSAGE();
        ROLLBACK TRANSACTION MyTransaction;
        DECLARE
            @ErrorMsg NVARCHAR(4000);
        SET
            @ErrorMsg = N'Lỗi: ' + ERROR_MESSAGE();
        THROW
            51000, @ErrorMsg, 1;
    END CATCH;
END;
go

