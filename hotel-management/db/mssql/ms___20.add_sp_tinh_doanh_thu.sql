USE hotel_management;

-- Tạo Stored Procedure
CREATE PROCEDURE [dbo].[GET_REVENUE](
    @TU DATE,
    @DEN DATE
)
AS
BEGIN
    BEGIN TRY

        SELECT DISTINCT THANG, NAM, SUM(TONG) AS DOANH_THU
        FROM (SELECT DISTINCT MONTH(NGAY_DI) AS THANG, YEAR(NGAY_DI) AS NAM, SUM(DON_GIA * DATEDIFF(DAY, NGAY_DEN, NGAY_DI)) AS TONG
              FROM CT_PHIEU_THUE CTPT
              WHERE TT_THANH_TOAN = 1
                AND (@TU IS NULL OR NGAY_DI >= @TU)
                AND (@DEN IS NULL OR NGAY_DI <= @DEN)
              GROUP BY MONTH(NGAY_DI), YEAR(NGAY_DI)
              UNION
              SELECT DISTINCT MONTH(CPT.NGAY_DI) AS THANG, YEAR(CPT.NGAY_DI) AS NAM, SUM(GIA * SO_LUONG) AS TONG
              FROM CT_DICH_VU CDV
                       INNER JOIN CT_PHIEU_THUE CPT ON CPT.ID_CT_PT = CDV.ID_CT_PT
              WHERE CDV.TT_THANH_TOAN = 1 AND CPT.TT_THANH_TOAN = 1
                AND (@TU IS NULL OR NGAY_DI >= @TU)
                AND (@DEN IS NULL OR NGAY_DI <= @DEN)
              GROUP BY YEAR(CPT.NGAY_DI), MONTH(CPT.NGAY_DI)
              UNION
              SELECT DISTINCT MONTH(CPT.NGAY_DI) AS THANG, YEAR(CPT.NGAY_DI) AS NAM, SUM(CPT2.DON_GIA * CPT2.SO_LUONG) AS TONG
              FROM CT_PHU_THU CPT2
                       INNER JOIN CT_PHIEU_THUE CPT ON CPT.ID_CT_PT = CPT2.ID_CT_PT
              WHERE CPT.TT_THANH_TOAN = 1 AND CPT2.TT_THANH_TOAN = 1
                AND (@TU IS NULL OR NGAY_DI >= @TU)
                AND (@DEN IS NULL OR NGAY_DI <= @DEN)
              GROUP BY MONTH(CPT.NGAY_DI), YEAR(CPT.NGAY_DI)
              UNION
              SELECT DISTINCT MONTH(PD.NGAY_DAT) AS THANG, YEAR(PD.NGAY_DAT) AS NAM, SUM(SO_TIEN_COC) AS TONG
              FROM PHIEU_DAT PD
              WHERE TRANG_THAI = 2
                AND (@TU IS NULL OR PD.NGAY_DAT >= @TU)
                AND (@DEN IS NULL OR PD.NGAY_DAT <= @DEN)
              GROUP BY MONTH(PD.NGAY_DAT), YEAR(PD.NGAY_DAT)
             ) AS TTTT
        GROUP BY THANG, NAM

    END TRY
    BEGIN CATCH
        PRINT 'An error occurred: ' + ERROR_MESSAGE();
        DECLARE
            @ErrorMsg NVARCHAR(4000);
        SET
            @ErrorMsg = N'Lỗi: ' + ERROR_MESSAGE();
        THROW
            51000, @ErrorMsg, 1;
    END CATCH;
END;
go

