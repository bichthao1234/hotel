-- ALTER the SP_CONFIRM_RESERVATION stored procedure
CREATE PROCEDURE [dbo].[SP_CONFIRM_RESERVATION](
    @ID_PD BIGINT,
    @NGAY_DEN DATE,
    @ID_NV NVARCHAR(MAX),
    @SO_PHONG_LIST NVARCHAR(MAX)
)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION MyTransaction;
        DECLARE @TRANG_THAI INT;
        DECLARE @NGAY_DEN_PD DATE;
        DECLARE @NGAY_DI_PD DATE;
        DECLARE @TONG_SO_PHONG_DA_DAT INT;
        DECLARE @TONG_SO_DA_CHECK_IN INT;
        SELECT @TRANG_THAI = TRANG_THAI, @NGAY_DEN_PD = NGAY_BD_THUE, @NGAY_DI_PD = NGAY_DI
        FROM PHIEU_DAT
        WHERE ID_PD = @ID_PD;
        SELECT @TONG_SO_PHONG_DA_DAT = SUM(SO_LUONG_PHONG_O) FROM CT_PHIEU_DAT WHERE ID_PD = @ID_PD;
        SELECT @TONG_SO_DA_CHECK_IN = COUNT(CTPT.ID_CT_PT)
        FROM CT_PHIEU_THUE CTPT
                 INNER JOIN dbo.PHIEU_THUE PT on PT.ID_PT = CTPT.ID_PT
                 INNER JOIN PHIEU_DAT PD on PD.ID_PD = PT.ID_PD
        WHERE PD.ID_PD = @ID_PD;

        IF (@TONG_SO_DA_CHECK_IN < @TONG_SO_PHONG_DA_DAT) AND @NGAY_DEN < @NGAY_DI_PD
            BEGIN

                -- Split the delimited strings into tables
                DECLARE @SO_PHONG_TABLE TABLE
                                        (
                                            ID_HANG_PHONG     BIGINT,
                                            SO_PHONG          VARCHAR(10),
                                            DANH_SACH_KHACH_O VARCHAR(MAX)
                                        );

                INSERT INTO @SO_PHONG_TABLE (ID_HANG_PHONG, SO_PHONG, DANH_SACH_KHACH_O)
                SELECT CAST(JSON_VALUE(value, '$.roomClassId') AS BIGINT)        AS ID_HANG_PHONG,
                       CAST(JSON_VALUE(value, '$.roomId') AS VARCHAR)            AS SO_PHONG,
                       CAST(JSON_VALUE(value, '$.customerIdsString') AS VARCHAR) AS DANH_SACH_KHACH_O
                FROM OPENJSON(@SO_PHONG_LIST);

                DECLARE @CMND NVARCHAR(MAX);
                SELECT @CMND = CMND FROM PHIEU_DAT WHERE ID_PD = @ID_PD;

                UPDATE PHIEU_DAT
                SET TRANG_THAI = 2,
                    ID_NV      = @ID_NV
                WHERE ID_PD = @ID_PD;

                DECLARE @INSERTED_ID_PT BIGINT;
                IF EXISTS(
                        SELECT 1
                        FROM PHIEU_THUE PT
                        WHERE PT.ID_PD = @ID_PD
                    )
                    BEGIN
                        SELECT @INSERTED_ID_PT = ID_PT FROM PHIEU_THUE WHERE ID_PD = @ID_PD;
                    END
                ELSE
                    BEGIN
                        INSERT INTO PHIEU_THUE (NGAY_LAP, ID_NV, CMND, ID_PD)
                        VALUES (GETDATE(), @ID_NV, @CMND, @ID_PD);
                        SELECT @INSERTED_ID_PT = @@identity
                    END

                DECLARE @ID_CT_PD_CURSOR CURSOR;
                SET @ID_CT_PD_CURSOR = CURSOR FOR SELECT ID_PD, ID_HANG_PHONG FROM CT_PHIEU_DAT WHERE ID_PD = @ID_PD;

                OPEN @ID_CT_PD_CURSOR;

                DECLARE @ID_HANG_PHONG BIGINT;

                FETCH NEXT FROM @ID_CT_PD_CURSOR INTO @ID_PD, @ID_HANG_PHONG;

                WHILE @@FETCH_STATUS = 0
                    BEGIN

                        DECLARE @SO_PHONG_CURSOR CURSOR;
                        SET @SO_PHONG_CURSOR =
                                CURSOR FOR SELECT SO_PHONG FROM @SO_PHONG_TABLE WHERE ID_HANG_PHONG = @ID_HANG_PHONG;

                        OPEN @SO_PHONG_CURSOR;

                        DECLARE @SO_PHONG VARCHAR(10);
                        FETCH NEXT FROM @SO_PHONG_CURSOR INTO @SO_PHONG;

                        WHILE @@FETCH_STATUS = 0
                            BEGIN
                                PRINT 'Started with ID_PD = ' + CAST(@ID_PD as VARCHAR) +
                                      ', AND ID_HANG_PHONG = ' + CAST(@ID_HANG_PHONG as VARCHAR) +
                                      ', AND SO_PHONG = ' + @SO_PHONG;

                                DECLARE @DON_GIA DECIMAL(10, 2);
                                SELECT @DON_GIA = DON_GIA
                                FROM CT_PHIEU_DAT
                                WHERE ID_PD = @ID_PD AND ID_HANG_PHONG = @ID_HANG_PHONG;

                                UPDATE CT_PHIEU_DAT
                                SET TRANG_THAI = 1
                                WHERE ID_PD = @ID_PD
                                  AND ID_HANG_PHONG = @ID_HANG_PHONG;

                                INSERT INTO CT_PHIEU_THUE (NGAY_DEN, NGAY_DI, GIO_DEN, ID_PT, SO_PHONG, DON_GIA)
                                VALUES (@NGAY_DEN, @NGAY_DI_PD, CONVERT(TIME, GETDATE()), @INSERTED_ID_PT, @SO_PHONG,
                                        @DON_GIA);
                                DECLARE @INSERTED_ID_CT_PT BIGINT;
                                SELECT @INSERTED_ID_CT_PT = @@identity

                                UPDATE PHONG
                                SET ID_TT = 'RS00002'
                                WHERE SO_PHONG = @SO_PHONG;

                                CREATE TABLE #DANH_SACH_KHACH_O_TABLE
                                (
                                    CMND NVARCHAR(20)
                                );

                                INSERT INTO #DANH_SACH_KHACH_O_TABLE
                                SELECT CAST(value AS NVARCHAR(20))
                                FROM STRING_SPLIT(
                                        (SELECT DANH_SACH_KHACH_O FROM @SO_PHONG_TABLE WHERE SO_PHONG = @SO_PHONG),
                                        ',');

                                IF EXISTS(
                                        SELECT 1
                                        FROM #DANH_SACH_KHACH_O_TABLE DS
                                        WHERE EXISTS(
                                                      SELECT 1
                                                      FROM CT_PHIEU_THUE CPT
                                                               INNER JOIN CT_KHACH_O CKO on CPT.ID_CT_PT = CKO.ID_CT_PT
                                                      WHERE CPT.TT_THANH_TOAN = 0
                                                        AND DS.CMND = CKO.CMND
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

                                FETCH NEXT FROM @SO_PHONG_CURSOR INTO @SO_PHONG;
                            END

                        FETCH NEXT FROM @ID_CT_PD_CURSOR INTO @ID_PD, @ID_HANG_PHONG;
                    END

                SELECT 'Success';
                COMMIT TRANSACTION MyTransaction;
            END
        ELSE
            BEGIN
                DECLARE
                    @ErrorMessage NVARCHAR(max) = N'Phiếu đặt không hợp lệ! ';
                DECLARE
                    @ErrorSeverity INT = 16; -- Error severity level 16: user-defined error
                DECLARE
                    @ErrorState INT = 1; -- Error State code 1:

                RAISERROR
                    (@ErrorMessage, @ErrorSeverity, @ErrorState);
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
END
go

