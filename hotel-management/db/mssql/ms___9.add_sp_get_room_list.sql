USE
    [hotel_management]
GO
/****** Object:  StoredProcedure [dbo].[SP_GET_ROOM_LIST]    Script Date: 10/10/2023 10:24:00 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- ALTER the SP_GET_ROOM_LIST stored procedure
ALTER PROCEDURE [dbo].[SP_GET_ROOM_LIST](
    @LOAI_PHONG_ID_LIST NVARCHAR(MAX),
    @KIEU_PHONG_ID_LIST NVARCHAR(MAX),
    @TRANG_THAI_ID_LIST NVARCHAR(MAX),
    @GIA_TU FLOAT,
    @GIA_DEN FLOAT,
    @KHUYEN_MAI BIT = NULL
)
AS
BEGIN
    BEGIN TRY

        -- Create temporary tables to hold the data from table variables
        CREATE TABLE #Temp_LOAI_PHONG_ID (ID_LP NVARCHAR(20));
        INSERT INTO #Temp_LOAI_PHONG_ID
        SELECT value
        FROM STRING_SPLIT(@LOAI_PHONG_ID_LIST, ',');

        CREATE TABLE #Temp_KIEU_PHONG_ID (ID_KP NVARCHAR(20));
        INSERT INTO #Temp_KIEU_PHONG_ID
        SELECT value
        FROM STRING_SPLIT(@KIEU_PHONG_ID_LIST, ',');

        CREATE TABLE #Temp_TRANG_THAI_ID (ID_TT NVARCHAR(20));
        INSERT INTO #Temp_TRANG_THAI_ID
        SELECT value
        FROM STRING_SPLIT(@TRANG_THAI_ID_LIST, ',');

        DECLARE @sql NVARCHAR(MAX);

        -- Initialize the SQL statement
        SET @sql = 'WITH LatestGIA AS (
                    SELECT
                        GHP.ID_HANG_PHONG,
                        MAX(GHP.NGAYAPDUNG) AS NGAY_AP_DUNG
                    FROM
                        GIA_HANG_PHONG GHP
                    WHERE
                            GHP.NGAYAPDUNG <= GETDATE()
                    GROUP BY
                        GHP.ID_HANG_PHONG
                ),
                     DiscountInfo AS (
                         SELECT
                             HP.ID_HANG_PHONG,
                             MAX(CT.PHAN_TRAM_GIAM) AS PHAN_TRAM_GIAM
                         FROM
                             HANG_PHONG HP
                                 LEFT JOIN CT_KHUYEN_MAI CT ON HP.ID_HANG_PHONG = CT.ID_HANG_PHONG
                                 LEFT JOIN KHUYEN_MAI KM ON CT.ID_KM = KM.ID_KM
                         WHERE
                             GETDATE() BETWEEN KM.NGAY_BAT_DAU AND KM.NGAY_KET_THUC
                         GROUP BY
                             HP.ID_HANG_PHONG
                     )
                SELECT DISTINCT
                    P.*,
                    TT.TEN_TRANG_THAI,
                    LP.TEN_LP,
                    KP.TEN_KP,
                    GHP.NGAYAPDUNG, GHP.GIA,
                    GHP.GIA * (1 - ISNULL(DI.PHAN_TRAM_GIAM, 0) / 100) AS DISCOUNTED_PRICE
                FROM
                    PHONG P
                        INNER JOIN dbo.HANG_PHONG HP ON P.ID_HANG_PHONG = HP.ID_HANG_PHONG
                        INNER JOIN LatestGIA GIA_HP ON HP.ID_HANG_PHONG = GIA_HP.ID_HANG_PHONG
                        INNER JOIN GIA_HANG_PHONG GHP ON GIA_HP.ID_HANG_PHONG = GHP.ID_HANG_PHONG AND GIA_HP.NGAY_AP_DUNG = GHP.NGAYAPDUNG
                        INNER JOIN KIEU_PHONG KP ON KP.ID_KP = HP.ID_KP
                        INNER JOIN LOAI_PHONG LP ON LP.ID_LP = HP.ID_LP
                        INNER JOIN TRANG_THAI TT ON P.ID_TT = TT.ID_TT
                        LEFT JOIN DiscountInfo DI ON HP.ID_HANG_PHONG = DI.ID_HANG_PHONG
                WHERE P.ID_TT IS NOT NULL ';

        -- Add conditions based on parameters
        IF @LOAI_PHONG_ID_LIST IS NOT NULL
            SET @sql = @sql + ' AND LP.ID_LP IN (SELECT ID_LP FROM #Temp_LOAI_PHONG_ID)';

        IF @KIEU_PHONG_ID_LIST IS NOT NULL
            SET @sql = @sql + ' AND KP.ID_KP IN (SELECT ID_KP FROM #Temp_KIEU_PHONG_ID)';

        IF @TRANG_THAI_ID_LIST IS NOT NULL
            SET @sql = @sql + ' AND TT.ID_TT IN (SELECT ID_TT FROM #Temp_TRANG_THAI_ID)';

        IF @GIA_TU IS NOT NULL AND @GIA_DEN IS NOT NULL
            SET @sql = @sql + ' AND GHP.GIA BETWEEN @GIA_TU AND @GIA_DEN ';

        IF @KHUYEN_MAI IS NOT NULL AND @KHUYEN_MAI = 1
            SET @sql = @sql + ' AND EXISTS (SELECT 1 FROM CT_KHUYEN_MAI CT
                                INNER JOIN KHUYEN_MAI KM ON CT.ID_KM = KM.ID_KM
                                WHERE CT.ID_HANG_PHONG = HP.ID_HANG_PHONG
                                AND GETDATE() BETWEEN KM.NGAY_BAT_DAU AND KM.NGAY_KET_THUC
                        )';

        PRINT '@sql = ' + @sql

        -- Execute the dynamic SQL
        EXEC sp_executesql @sql,
             N'@GIA_TU FLOAT, @GIA_DEN FLOAT',
             @GIA_TU, @GIA_DEN;

        -- Clean up temporary tables
        DROP TABLE #Temp_LOAI_PHONG_ID;
        DROP TABLE #Temp_KIEU_PHONG_ID;
        DROP TABLE #Temp_TRANG_THAI_ID;

    END TRY
    BEGIN CATCH
        PRINT 'An error occurred: ' + ERROR_MESSAGE();
        DECLARE
            @ErrorMsg NVARCHAR(4000);
        SET
            @ErrorMsg = 'An error occurred: ' + ERROR_MESSAGE();
        THROW
            51000, @ErrorMsg, 1;
    END CATCH;
END
go