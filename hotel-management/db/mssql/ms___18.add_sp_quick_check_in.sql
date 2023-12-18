-- Tạo Stored Procedure
CREATE PROCEDURE [dbo].[QUICK_CHECK_IN](
    @ID_NV VARCHAR(20),
    @NGAY_DEN DATE,
    @NGAY_DI DATE,
    @ID_HANG_PHONG VARCHAR(MAX),
    @SO_PHONG VARCHAR(MAX),
    @DON_GIA DECIMAL(10,2),
    @CMND_KHACH_O_LIST VARCHAR(MAX),
    @CMND VARCHAR(MAX)
)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION MyTransaction;
        -- Check phòng đặt hợp lệ hay không
        EXEC dbo.SP_CHECK_ROOM
             @NGAY_BD_THUE = @NGAY_DEN,
             @NGAY_DI = @NGAY_DI,
             @ID_HANG_PHONG = @ID_HANG_PHONG,
             @SO_LUONG_PHONG_O = 1;

        -- Step 1: Tạo phiếu đặt (CMND = null) từ thông tin (Vì phải có thông tin phiếu đặt mới lấy được giá phòng)
        INSERT INTO PHIEU_DAT(NGAY_DAT, NGAY_BD_THUE, NGAY_DI, TRANG_THAI, CMND, SO_TIEN_COC)
        VALUES (GETDATE(), @NGAY_DEN, @NGAY_DI, 2, @CMND, 0) -- Status = 2 = Đã thuê
        DECLARE @INSERTED_ID_PD BIGINT;
        SELECT @INSERTED_ID_PD = @@identity;

        -- Step 2: Tạo CT Phiếu đặt từ phiếu đặt
        INSERT INTO CT_PHIEU_DAT(ID_PD, ID_HANG_PHONG, SO_LUONG_PHONG_O, DON_GIA, TRANG_THAI)
        VALUES (@INSERTED_ID_PD, @ID_HANG_PHONG, 1, @DON_GIA, 1)

        -- Step 3: Tạo phiếu thuê
        INSERT INTO PHIEU_THUE (NGAY_LAP, ID_NV, ID_PD, CMND)
        VALUES (GETDATE(), @ID_NV, @INSERTED_ID_PD, @CMND);
        DECLARE @INSERTED_ID_PT BIGINT;
        SELECT @INSERTED_ID_PT = @@identity;

        -- Step 4: Tạo CT Phiếu thuê
        INSERT INTO CT_PHIEU_THUE (NGAY_DEN, NGAY_DI, GIO_DEN, ID_PT, SO_PHONG, DON_GIA)
        VALUES (@NGAY_DEN, @NGAY_DI, CONVERT(TIME, GETDATE()), @INSERTED_ID_PT, @SO_PHONG, @DON_GIA);
        DECLARE @INSERTED_ID_CT_PT BIGINT;
        SELECT @INSERTED_ID_CT_PT = @@identity;

        -- Step 5: Lưu chi tiết khách ở
        CREATE TABLE #DANH_SACH_KHACH_O_TABLE (CMND NVARCHAR(20));

        INSERT INTO #DANH_SACH_KHACH_O_TABLE
        SELECT CAST(value AS NVARCHAR(20))
        FROM STRING_SPLIT(@CMND_KHACH_O_LIST, ',');

        IF EXISTS(
                SELECT 1
                FROM #DANH_SACH_KHACH_O_TABLE DS
                WHERE EXISTS (
                              SELECT 1
                              FROM CT_PHIEU_THUE CPT
                                       INNER JOIN CT_KHACH_O CKO on CPT.ID_CT_PT = CKO.ID_CT_PT
                              WHERE CPT.TT_THANH_TOAN = 0 AND DS.CMND = CKO.CMND
                          )
            )
            BEGIN
                RAISERROR
                    (N'Danh sách khách ở không hợp lệ!', 16, 1);
            END

        INSERT INTO CT_KHACH_O (ID_CT_PT, CMND)
        SELECT @INSERTED_ID_CT_PT, CMND
        FROM #DANH_SACH_KHACH_O_TABLE;

        DROP TABLE #DANH_SACH_KHACH_O_TABLE;

        COMMIT TRANSACTION MyTransaction;
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

