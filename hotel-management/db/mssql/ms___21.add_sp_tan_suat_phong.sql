-- TẠO STORED PROCEDURE
CREATE PROCEDURE [DBO].[GET_ROOM_BOOKING_RATE](
    @TU DATE,
    @DEN DATE
)
AS
BEGIN
    DECLARE
        @RESULT TABLE
                (
                    ID_HANG_PHONG    BIGINT,
                    KIEU_PHONG NVARCHAR(MAX),
                    LOAI_PHONG NVARCHAR(MAX),
                    TONG_SO_LUOT_THUE INT,
                    SO_LUOT_THUE INT
                );

    BEGIN TRY

        DECLARE @TONG_SO_LUOT_THUE INT;
        SELECT @TONG_SO_LUOT_THUE = COUNT(ID_CT_PT)
        FROM CT_PHIEU_THUE
        WHERE (@TU IS NULL OR NGAY_DI > @TU)
          AND (@DEN IS NULL OR NGAY_DI < @DEN);

        IF @TONG_SO_LUOT_THUE <> 0
            BEGIN
                DECLARE @ID_HANG_PHONG_CURSOR CURSOR;
                SET @ID_HANG_PHONG_CURSOR = CURSOR FOR SELECT ID_HANG_PHONG FROM HANG_PHONG;
                OPEN @ID_HANG_PHONG_CURSOR;

                DECLARE @ID_HANG_PHONG BIGINT;
                FETCH NEXT FROM @ID_HANG_PHONG_CURSOR INTO @ID_HANG_PHONG;
                WHILE @@FETCH_STATUS = 0
                    BEGIN
                        INSERT INTO @RESULT (ID_HANG_PHONG, TONG_SO_LUOT_THUE, SO_LUOT_THUE)
                        SELECT @ID_HANG_PHONG, @TONG_SO_LUOT_THUE, COUNT(CPT.ID_CT_PT)
                        FROM CT_PHIEU_THUE CPT
                                 INNER JOIN PHONG P ON P.SO_PHONG = CPT.SO_PHONG
                                 INNER JOIN HANG_PHONG HP ON P.ID_HANG_PHONG = HP.ID_HANG_PHONG
                        WHERE TT_THANH_TOAN = 1
                          AND HP.ID_HANG_PHONG = @ID_HANG_PHONG
                          AND (@TU IS NULL OR NGAY_DI > @TU)
                          AND (@DEN IS NULL OR NGAY_DI < @DEN);

                        UPDATE @RESULT
                        SET KIEU_PHONG = KP, LOAI_PHONG = LP
                        FROM (
                                 SELECT KP.TEN_KP AS KP, LP.TEN_LP AS LP
                                 FROM HANG_PHONG
                                          INNER JOIN DBO.KIEU_PHONG KP ON KP.ID_KP = HANG_PHONG.ID_KP
                                          INNER JOIN DBO.LOAI_PHONG LP ON LP.ID_LP = HANG_PHONG.ID_LP
                                 WHERE ID_HANG_PHONG = @ID_HANG_PHONG
                             ) AS SUBQUERY
                        WHERE ID_HANG_PHONG = @ID_HANG_PHONG;

                        -- FETCH THE NEXT ROWS
                        FETCH NEXT FROM @ID_HANG_PHONG_CURSOR INTO @ID_HANG_PHONG;
                    END
            END

        SELECT * FROM @RESULT;
    END TRY
    BEGIN CATCH
        PRINT 'AN ERROR OCCURRED: ' + ERROR_MESSAGE();
        DECLARE
            @ERRORMSG NVARCHAR(4000);
        SET
            @ERRORMSG = N'LỖI: ' + ERROR_MESSAGE();
        THROW
            51000, @ERRORMSG, 1;
    END CATCH;
END;
GO

