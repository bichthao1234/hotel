-- ALTER the SP_CHECK_ROOM stored procedure
CREATE PROCEDURE
    [dbo].[SP_CHECK_ROOM](
    @NGAY_BD_THUE DATE,
    @NGAY_DI DATE,
    @ID_HANG_PHONG NVARCHAR(MAX),
    @SO_LUONG_PHONG_O NVARCHAR(MAX),
    @ID_PD BIGINT = NULL
)
AS
BEGIN
    DECLARE @OUTPUT TABLE
                    (
                        ID_HANG_PHONG  BIGINT,
                        SO_LUONG_PHONG INT
                    );
    BEGIN TRY
        IF @ID_HANG_PHONG IS NOT NULL
            BEGIN
                DECLARE @KIEU_PHONG NVARCHAR(MAX);
                DECLARE @LOAI_PHONG NVARCHAR(MAX);
                SELECT @LOAI_PHONG = LP.TEN_LP, @KIEU_PHONG = KP.TEN_KP
                FROM HANG_PHONG HP
                         INNER JOIN dbo.LOAI_PHONG LP on LP.ID_LP = HP.ID_LP
                         INNER JOIN dbo.KIEU_PHONG KP on KP.ID_KP = HP.ID_KP
                WHERE ID_HANG_PHONG = @ID_HANG_PHONG;
                PRINT '@KIEU_PHONG = ' + @KIEU_PHONG
                PRINT '@LOAI_PHONG = ' + @LOAI_PHONG

                DECLARE @TONG_SO_PHONG INT;
                SELECT @TONG_SO_PHONG = COUNT(*)
                FROM PHONG p
                WHERE ID_HANG_PHONG = @ID_HANG_PHONG
                  AND p.ID_TT <> 'RS00004' --Maintenance

                DECLARE
                    @SO_PHONG_DA_DAT INT;
                SELECT @SO_PHONG_DA_DAT = SUM(ctpd.SO_LUONG_PHONG_O)
                FROM CT_PHIEU_DAT ctpd
                         INNER JOIN PHIEU_DAT pd ON ctpd.ID_PD = pd.ID_PD
                WHERE ctpd.ID_HANG_PHONG = @ID_HANG_PHONG
                  AND (pd.TRANG_THAI = 1 -- chờ sác nhận   chưa toi
                    )
                  AND ((pd.NGAY_BD_THUE <= @NGAY_BD_THUE AND pd.NGAY_DI > @NGAY_BD_THUE)
                    OR (pd.NGAY_BD_THUE > @NGAY_BD_THUE AND pd.NGAY_BD_THUE < @NGAY_DI))
                  AND (@ID_PD IS NULL OR pd.ID_PD <> @ID_PD);

                DECLARE
                    @SO_PHONG_HET_HAN INT;
                SELECT @SO_PHONG_HET_HAN = COUNT(distinct p.SO_PHONG)
                FROM CT_PHIEU_THUE ctpt
                         INNER JOIN PHONG p on p.SO_PHONG = ctpt.SO_PHONG
                         INNER JOIN HANG_PHONG hp on hp.ID_HANG_PHONG = p.ID_HANG_PHONG
                WHERE hp.ID_HANG_PHONG = @ID_HANG_PHONG
                  AND ctpt.NGAY_DI > @NGAY_BD_THUE
                  AND ctpt.TT_THANH_TOAN = 0

                IF (@SO_LUONG_PHONG_O > (@TONG_SO_PHONG - ISNULL(@SO_PHONG_DA_DAT, 0) - @SO_PHONG_HET_HAN))
                    BEGIN
                        DECLARE
                            @ErrorMessage NVARCHAR(max) = N'Không đủ phòng cho hạng phòng với ID: '
                                + CAST(@ID_HANG_PHONG as VARCHAR)
                                + ' - ' + CAST(ISNULL(@LOAI_PHONG, 'NULL') AS VARCHAR)
                                + ' ' + CAST(ISNULL(@KIEU_PHONG, 'NULL') AS VARCHAR);
                        DECLARE
                            @ErrorSeverity INT = 16; -- Error severity level 16: user-defined error
                        DECLARE
                            @ErrorState INT = 1; -- Error State code 1:

                        RAISERROR
                            (@ErrorMessage, @ErrorSeverity, @ErrorState);
                    END
                INSERT INTO @OUTPUT (ID_HANG_PHONG, SO_LUONG_PHONG)
                VALUES (@ID_HANG_PHONG, (@TONG_SO_PHONG - ISNULL(@SO_PHONG_DA_DAT, 0) - @SO_PHONG_HET_HAN));

                SELECT * FROM @OUTPUT;
            END
        ELSE
            BEGIN
                PRINT 'SP_CHECK_ROOM.@ID_HANG_PHONG is NULL'
                DECLARE @ID_HANG_PHONG_CURSOR CURSOR;
                SET @ID_HANG_PHONG_CURSOR = CURSOR FOR SELECT ID_HANG_PHONG FROM HANG_PHONG;
                OPEN @ID_HANG_PHONG_CURSOR;

                FETCH NEXT FROM @ID_HANG_PHONG_CURSOR INTO @ID_HANG_PHONG;
                WHILE @@FETCH_STATUS = 0
                    BEGIN
                        SELECT @TONG_SO_PHONG = COUNT(*)
                        FROM PHONG p
                        WHERE ID_HANG_PHONG = @ID_HANG_PHONG
                          AND p.ID_TT <> 'RS00004' --Maintenance

                        SELECT @SO_PHONG_DA_DAT = SUM(ctpd.SO_LUONG_PHONG_O)
                        FROM CT_PHIEU_DAT ctpd
                                 INNER JOIN PHIEU_DAT pd ON ctpd.ID_PD = pd.ID_PD
                        WHERE ctpd.ID_HANG_PHONG = @ID_HANG_PHONG
                          AND (pd.TRANG_THAI = 1 -- chờ sác nhận   chưa toi
                            )
                          AND ((pd.NGAY_BD_THUE <= @NGAY_BD_THUE AND pd.NGAY_DI > @NGAY_BD_THUE)
                            OR (pd.NGAY_BD_THUE > @NGAY_BD_THUE AND pd.NGAY_BD_THUE < @NGAY_DI));

                        SELECT @SO_PHONG_HET_HAN = COUNT(distinct p.SO_PHONG)
                        FROM CT_PHIEU_THUE ctpt
                                 INNER JOIN PHONG p on p.SO_PHONG = ctpt.SO_PHONG
                                 INNER JOIN HANG_PHONG hp on hp.ID_HANG_PHONG = p.ID_HANG_PHONG
                        WHERE hp.ID_HANG_PHONG = @ID_HANG_PHONG
                          AND ctpt.NGAY_DI > @NGAY_BD_THUE
                          AND ctpt.TT_THANH_TOAN = 0

                        IF ((@TONG_SO_PHONG - ISNULL(@SO_PHONG_DA_DAT, 0) - @SO_PHONG_HET_HAN) > 0)
                            BEGIN
                                INSERT INTO @OUTPUT (ID_HANG_PHONG, SO_LUONG_PHONG)
                                VALUES (@ID_HANG_PHONG,
                                        (@TONG_SO_PHONG - ISNULL(@SO_PHONG_DA_DAT, 0) - @SO_PHONG_HET_HAN));
                            END

                        FETCH NEXT FROM @ID_HANG_PHONG_CURSOR INTO @ID_HANG_PHONG;
                    END

                DECLARE @TONG_SO_PHONG_KS INT;
                SELECT @TONG_SO_PHONG_KS = COUNT(SO_PHONG) FROM PHONG;

                DECLARE @TONG_SO_PHONG_TRONG INT;
                SELECT @TONG_SO_PHONG_TRONG = SUM(SO_LUONG_PHONG) FROM @OUTPUT;

                SELECT * FROM @OUTPUT;
            END

    END TRY
    BEGIN CATCH
        PRINT 'An error occurred: ' + ERROR_MESSAGE();
        DECLARE
            @ErrorMsg NVARCHAR(4000);
        SET
            @ErrorMsg = ERROR_MESSAGE();
        THROW
            51000, @ErrorMsg, 1;
    END CATCH;
END
go

