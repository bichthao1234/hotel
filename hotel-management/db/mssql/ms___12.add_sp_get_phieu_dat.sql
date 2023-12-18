-- ALTER the SP_GET_RESERVATION_LIST stored procedure
CREATE PROCEDURE [dbo].[SP_GET_RESERVATION_LIST](
    @NGAY_DAT_FROM DATE = null,
    @NGAY_DAT_TO DATE = null,
    @NGAY_BD_THUE_FROM DATE = null,
    @NGAY_BD_THUE_TO DATE = null,
    @CMND NVARCHAR(MAX) = null,
    @STATUS INT = NULL
)
AS
BEGIN
    BEGIN TRY

        DECLARE @sql NVARCHAR(MAX);

        -- Initialize the SQL statement
        SET @sql = 'SELECT PD.ID_PD, PD.NGAY_DAT, PD.NGAY_BD_THUE, PD.NGAY_DI, DATEDIFF(DAY, PD.NGAY_BD_THUE, PD.NGAY_DI) AS SO_NGAY_O,
                    KH.CMND, KH.HO + '' '' + KH.TEN AS HO_TEN, PD.SO_TIEN_COC, PD.TRANG_THAI
                    FROM PHIEU_DAT PD
                    INNER JOIN KHACH_HANG KH ON KH.CMND = PD.CMND';

        -- Add conditions based on parameters
        IF @NGAY_DAT_FROM IS NOT NULL AND @NGAY_DAT_TO IS NOT NULL
            SET @sql = @sql + ' AND PD.NGAY_DAT between @NGAY_DAT_FROM and @NGAY_DAT_TO ';

        IF @NGAY_BD_THUE_FROM IS NOT NULL AND @NGAY_BD_THUE_TO IS NOT NULL
            SET @sql = @sql + ' AND PD.NGAY_BD_THUE between @NGAY_BD_THUE_FROM and @NGAY_BD_THUE_TO ';

        IF @CMND IS NOT NULL
            SET @sql = @sql + ' AND KH.CMND = @CMND ';

        IF @STATUS IS NOT NULL
            SET @sql = @sql + ' AND PD.TRANG_THAI = @STATUS ';

        PRINT '@sql = ' + @sql

        -- Execute the dynamic SQL
        EXEC sp_executesql @sql,
             N'@NGAY_DAT_FROM DATE, @NGAY_DAT_TO DATE, @NGAY_BD_THUE_FROM DATE, @NGAY_BD_THUE_TO DATE, @CMND NVARCHAR(MAX), @STATUS INT',
             @NGAY_DAT_FROM, @NGAY_DAT_TO, @NGAY_BD_THUE_FROM, @NGAY_BD_THUE_TO, @CMND, @STATUS;

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

