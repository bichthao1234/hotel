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

        DECLARE @ID_CT_PT_CURSOR CURSOR;
        SET @ID_CT_PT_CURSOR = CURSOR FOR SELECT ID_CT_PT FROM @ID_CT_PT_TABLE;
        OPEN @ID_CT_PT_CURSOR;
        FETCH NEXT FROM @ID_CT_PT_CURSOR INTO @ID_CT_PT;

        WHILE @@FETCH_STATUS = 0
            BEGIN
                DECLARE @TRANG_THAI_TT BIT;
                SELECT @TRANG_THAI_TT = TT_THANH_TOAN FROM CT_PHIEU_THUE WHERE ID_CT_PT = @ID_CT_PT;
                IF @TRANG_THAI_TT = 0
                    BEGIN

                        UPDATE CT_PHU_THU SET TT_THANH_TOAN = 1 WHERE ID_CT_PT = @ID_CT_PT;
                        UPDATE CT_DICH_VU SET TT_THANH_TOAN = 1 WHERE ID_CT_PT = @ID_CT_PT;

                        UPDATE CT_PHIEU_THUE
                        SET NGAY_DI       = GETDATE(),
                            TT_THANH_TOAN = 1
                        WHERE ID_CT_PT = @ID_CT_PT;

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

-- TẠO STORED PROCEDURE
CREATE PROCEDURE [DBO].[GET_REVENUE](
    @TU DATE,
    @DEN DATE
)
AS
BEGIN
    DECLARE
        @RESULT TABLE
                (
                    THANG    INT,
                    NAM INT,
                    DOANH_THU DECIMAL
                );
    BEGIN TRY

        SELECT DISTINCT THANG AS THANG, NAM AS NAM, SUM(TONG) AS DOANH_THU INTO #TEMP_TABLE
        FROM (SELECT DISTINCT MONTH(NGAY_DI) AS THANG, YEAR(NGAY_DI) AS NAM,
                              SUM(CASE WHEN DATEDIFF(DAY, NGAY_DEN, NGAY_DI) = 0 THEN DON_GIA ELSE DON_GIA * DATEDIFF(DAY, NGAY_DEN, NGAY_DI) END) AS TONG
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
              WHERE TRANG_THAI <> 2
                AND (@TU IS NULL OR PD.NGAY_DAT >= @TU)
                AND (@DEN IS NULL OR PD.NGAY_DAT <= @DEN)
              GROUP BY MONTH(PD.NGAY_DAT), YEAR(PD.NGAY_DAT)
             ) AS TTTT
        GROUP BY THANG, NAM

        DECLARE @NAM_BD INT, @NAM_KT INT, @THANG_BD INT, @THANG_KT INT;
        IF @TU IS NULL
            SELECT @NAM_BD = YEAR(GETDATE()), @THANG_BD = 1;
        ELSE
            SELECT @NAM_BD = YEAR(@TU), @THANG_BD = MONTH(@TU);
        IF @DEN IS NULL
            SELECT @NAM_KT = YEAR(GETDATE()), @THANG_KT = 12;
        ELSE
            SELECT @NAM_KT = YEAR(@DEN), @THANG_KT = MONTH(@DEN);

        WHILE (@NAM_BD < @NAM_KT) OR (@NAM_BD = @NAM_KT AND @THANG_BD <= @THANG_KT)
            BEGIN
                IF EXISTS(
                        SELECT 1
                        FROM #TEMP_TABLE TEMP
                        WHERE TEMP.THANG = @THANG_BD AND TEMP.NAM = @NAM_BD
                    )
                    BEGIN
                        DECLARE @DOANH_THU DECIMAL;
                        SELECT @DOANH_THU = DOANH_THU FROM #TEMP_TABLE WHERE THANG = @THANG_BD AND NAM = @NAM_BD;
                        INSERT INTO @RESULT (THANG, NAM, DOANH_THU)
                        VALUES (@THANG_BD, @NAM_BD, @DOANH_THU)
                    END
                ELSE
                    INSERT INTO @RESULT (THANG, NAM, DOANH_THU)
                    VALUES (@THANG_BD, @NAM_BD, 0)

                SET @THANG_BD = @THANG_BD + 1;

                IF @THANG_BD > 12
                    BEGIN
                        SET @THANG_BD = 1;
                        SET @NAM_BD = @NAM_BD + 1;
                    END
            END

        DROP TABLE #TEMP_TABLE;
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
go

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
go

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
        EXEC dbo.SP_CHECK_ROOM
             @NGAY_BD_THUE = @NGAY_DEN,
             @NGAY_DI = @NGAY_DI,
             @ID_HANG_PHONG = @ID_HANG_PHONG,
             @SO_LUONG_PHONG_O = 1;

        INSERT INTO PHIEU_DAT(NGAY_DAT, NGAY_BD_THUE, NGAY_DI, TRANG_THAI, CMND, SO_TIEN_COC)
        VALUES (GETDATE(), @NGAY_DEN, @NGAY_DI, 2, @CMND, 0) -- Status = 2 = Đã thuê
        DECLARE @INSERTED_ID_PD BIGINT;
        SELECT @INSERTED_ID_PD = @@identity;

        INSERT INTO CT_PHIEU_DAT(ID_PD, ID_HANG_PHONG, SO_LUONG_PHONG_O, DON_GIA, TRANG_THAI)
        VALUES (@INSERTED_ID_PD, @ID_HANG_PHONG, 1, @DON_GIA, 1)

        INSERT INTO PHIEU_THUE (NGAY_LAP, ID_NV, ID_PD, CMND)
        VALUES (GETDATE(), @ID_NV, @INSERTED_ID_PD, @CMND);
        DECLARE @INSERTED_ID_PT BIGINT;
        SELECT @INSERTED_ID_PT = @@identity;

        INSERT INTO CT_PHIEU_THUE (NGAY_DEN, NGAY_DI, GIO_DEN, ID_PT, SO_PHONG, DON_GIA)
        VALUES (@NGAY_DEN, @NGAY_DI, CONVERT(TIME, GETDATE()), @INSERTED_ID_PT, @SO_PHONG, @DON_GIA);
        DECLARE @INSERTED_ID_CT_PT BIGINT;
        SELECT @INSERTED_ID_CT_PT = @@identity;

        UPDATE PHONG SET ID_TT = 'RS00002'
        WHERE SO_PHONG = @SO_PHONG;

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

-- ALTER the SP_BOOK_ROOM stored procedure
CREATE PROCEDURE
    [dbo].[SP_BOOK_ROOM](
    @CMND NVARCHAR(20),
    @HO NVARCHAR(50) = '',
    @TEN NVARCHAR(50) = '',
    @SDT NVARCHAR(20) = '',
    @EMAIL NVARCHAR(255) = '',
    @DIA_CHI NVARCHAR(255) = '',
    @MA_SO_THUE NVARCHAR(50) = '',
    @SO_TIEN_COC DECIMAL(10, 2),
    @NGAY_BD_THUE DATE,
    @NGAY_DI DATE,
    @ID_HANG_PHONG_LIST NVARCHAR(MAX),
    @SO_LUONG_PHONG_O_LIST NVARCHAR(MAX)
)
AS
BEGIN
    BEGIN TRY
        PRINT 'Start the SP_BOOK_ROOM...'
        BEGIN
            TRANSACTION MyTransaction;

        DECLARE
            @Result TABLE
                    (
                        ID_HANG_PHONG    BIGINT,
                        SO_LUONG_PHONG_O INT
                    );
        DECLARE
            @ROW_COUNT_CUSTOMER INT;
        SELECT @ROW_COUNT_CUSTOMER = COUNT(*)
        FROM KHACH_HANG
        WHERE CMND = @CMND;
        IF
                @ROW_COUNT_CUSTOMER = 0
            BEGIN
                INSERT INTO KHACH_HANG (CMND, HO, TEN, SDT, EMAIL, DIA_CHI, MA_SO_THUE)
                VALUES (@CMND, @HO, @TEN, @SDT, @EMAIL, @DIA_CHI, @MA_SO_THUE);
            END

        DECLARE @ID_HANG_PHONG_CURSOR CURSOR;
        DECLARE @SO_LUONG_PHONG_O_CURSOR CURSOR;


        DECLARE @ID_HANG_PHONG BIGINT;
        DECLARE @SO_LUONG_PHONG_O INT;

        DECLARE @ID_HANG_PHONG_TABLE TABLE (ID BIGINT);
        DECLARE @SO_LUONG_PHONG_O_TABLE TABLE (SO_LUONG INT);

        INSERT INTO @ID_HANG_PHONG_TABLE
        SELECT CAST(value AS BIGINT)
        FROM STRING_SPLIT(@ID_HANG_PHONG_LIST, ',');

        INSERT INTO @SO_LUONG_PHONG_O_TABLE
        SELECT CAST(value AS INT)
        FROM STRING_SPLIT(@SO_LUONG_PHONG_O_LIST, ',');

        INSERT INTO PHIEU_DAT(NGAY_DAT, NGAY_BD_THUE, NGAY_DI, TRANG_THAI, CMND)
        VALUES (GETDATE(), @NGAY_BD_THUE, @NGAY_DI, 1, @CMND) -- Status = 1 = Da dat
        PRINT 'Inserted into PHIEU_DAT complete'

        DECLARE @INSERTED_ID_PD BIGINT;
        SELECT @INSERTED_ID_PD = @@identity
        PRINT 'Get the inserted ID_PD complete ' + CAST(@INSERTED_ID_PD as VARCHAR)

        SET @ID_HANG_PHONG_CURSOR = CURSOR FOR SELECT ID FROM @ID_HANG_PHONG_TABLE;
        SET @SO_LUONG_PHONG_O_CURSOR = CURSOR FOR SELECT SO_LUONG FROM @SO_LUONG_PHONG_O_TABLE;

        OPEN @ID_HANG_PHONG_CURSOR;
        OPEN @SO_LUONG_PHONG_O_CURSOR;

        FETCH NEXT FROM @ID_HANG_PHONG_CURSOR INTO @ID_HANG_PHONG;
        FETCH NEXT FROM @SO_LUONG_PHONG_O_CURSOR INTO @SO_LUONG_PHONG_O;

        WHILE @@FETCH_STATUS = 0
            BEGIN
                INSERT INTO @Result (ID_HANG_PHONG, SO_LUONG_PHONG_O)
                VALUES (@ID_HANG_PHONG, @SO_LUONG_PHONG_O);

                EXEC dbo.SP_CHECK_ROOM
                     @NGAY_BD_THUE = @NGAY_BD_THUE,
                     @NGAY_DI = @NGAY_DI,
                     @ID_HANG_PHONG = @ID_HANG_PHONG,
                     @SO_LUONG_PHONG_O = @SO_LUONG_PHONG_O;

                DECLARE @GIA_HP DECIMAL(10, 2);
                SELECT TOP (1) @GIA_HP = GIA
                FROM GIA_HANG_PHONG ghp
                         INNER JOIN HANG_PHONG h on h.ID_HANG_PHONG = ghp.ID_HANG_PHONG
                WHERE h.ID_HANG_PHONG = @ID_HANG_PHONG
                  AND ghp.NGAYAPDUNG <= @NGAY_BD_THUE
                ORDER BY ghp.NGAYAPDUNG DESC

                -- Get the promotion
                DECLARE @PHAN_TRAM_GIAM DECIMAL(10,2);
                SELECT @PHAN_TRAM_GIAM = MAX(CT.PHAN_TRAM_GIAM)
                FROM CT_KHUYEN_MAI CT
                         INNER JOIN KHUYEN_MAI KM ON CT.ID_KM = KM.ID_KM
                         INNER JOIN HANG_PHONG HP on HP.ID_HANG_PHONG = CT.ID_HANG_PHONG
                WHERE CT.ID_HANG_PHONG = @ID_HANG_PHONG
                  AND GETDATE() BETWEEN KM.NGAY_BAT_DAU AND KM.NGAY_KET_THUC;

                DECLARE @DON_GIA DECIMAL(10,2);
                SELECT @DON_GIA = @GIA_HP * (1 - ISNULL(@PHAN_TRAM_GIAM, 0) / 100)

                INSERT INTO CT_PHIEU_DAT(ID_PD, ID_HANG_PHONG, SO_LUONG_PHONG_O, DON_GIA)
                VALUES (@INSERTED_ID_PD, @ID_HANG_PHONG, @SO_LUONG_PHONG_O, @DON_GIA)

                FETCH NEXT FROM @ID_HANG_PHONG_CURSOR INTO @ID_HANG_PHONG;
                FETCH NEXT FROM @SO_LUONG_PHONG_O_CURSOR INTO @SO_LUONG_PHONG_O;
            END;

        CLOSE @ID_HANG_PHONG_CURSOR;
        DEALLOCATE @ID_HANG_PHONG_CURSOR;
        CLOSE @SO_LUONG_PHONG_O_CURSOR;
        DEALLOCATE @SO_LUONG_PHONG_O_CURSOR;

        DECLARE @SO_NGAY_O INT;
        SELECT @SO_NGAY_O = DATEDIFF(DAY, PD.NGAY_BD_THUE, PD.NGAY_DI)
        FROM PHIEU_DAT PD
        WHERE PD.ID_PD = @INSERTED_ID_PD;

        DECLARE @TONG_GIA_PHIEU_DAT INT;
        SELECT @TONG_GIA_PHIEU_DAT = SUM(DON_GIA * SO_LUONG_PHONG_O) * @SO_NGAY_O
        FROM CT_PHIEU_DAT
        WHERE ID_PD = @INSERTED_ID_PD

        IF @SO_TIEN_COC < (@TONG_GIA_PHIEU_DAT * 0.2)
            BEGIN
                DECLARE
                    @ErrorMessage NVARCHAR(max) = 'Tiền cọc không hợp lệ! Tiền cọc phải lớn hơn '
                        + CAST((@TONG_GIA_PHIEU_DAT * 0.2) as VARCHAR);
                DECLARE
                    @ErrorSeverity INT = 16; -- Error severity level 16: user-defined error
                DECLARE
                    @ErrorState INT = 1; -- Error State code 1:

                RAISERROR
                    (@ErrorMessage, @ErrorSeverity, @ErrorState);
            END
        UPDATE PHIEU_DAT
        SET SO_TIEN_COC = @SO_TIEN_COC
        WHERE ID_PD = @INSERTED_ID_PD

        SELECT * FROM @Result;

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
                                + CAST(@ID_HANG_PHONG as VARCHAR);
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
        SELECT @TRANG_THAI = TRANG_THAI, @NGAY_DEN_PD = NGAY_BD_THUE, @NGAY_DI_PD = NGAY_DI FROM PHIEU_DAT WHERE ID_PD = @ID_PD;
        SELECT @TONG_SO_PHONG_DA_DAT = SUM(SO_LUONG_PHONG_O) FROM CT_PHIEU_DAT WHERE ID_PD = @ID_PD;
        SELECT @TONG_SO_DA_CHECK_IN = COUNT(CTPT.ID_CT_PT) FROM CT_PHIEU_THUE CTPT
                                                                    INNER JOIN dbo.PHIEU_THUE PT on PT.ID_PT = CTPT.ID_PT
                                                                    INNER JOIN PHIEU_DAT PD on PD.ID_PD = PT.ID_PD
        WHERE PD.ID_PD = @ID_PD;

        IF (@TONG_SO_DA_CHECK_IN < @TONG_SO_PHONG_DA_DAT) AND @NGAY_DEN < @NGAY_DI_PD
            BEGIN

                DECLARE @SO_PHONG_TABLE TABLE (ID_HANG_PHONG BIGINT, SO_PHONG VARCHAR(10), DANH_SACH_KHACH_O VARCHAR(MAX));

                INSERT INTO @SO_PHONG_TABLE (ID_HANG_PHONG, SO_PHONG, DANH_SACH_KHACH_O)
                SELECT
                    CAST(JSON_VALUE(value, '$.roomClassId') AS BIGINT) AS ID_HANG_PHONG,
                    CAST(JSON_VALUE(value, '$.roomId') AS VARCHAR) AS SO_PHONG,
                    CAST(JSON_VALUE(value, '$.customerIdsString') AS VARCHAR) AS DANH_SACH_KHACH_O
                FROM OPENJSON(@SO_PHONG_LIST);

                DECLARE @CMND NVARCHAR(MAX);
                SELECT @CMND = CMND FROM PHIEU_DAT WHERE ID_PD = @ID_PD;

                UPDATE PHIEU_DAT
                SET TRANG_THAI = 2, ID_NV = @ID_NV
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
                        SET @SO_PHONG_CURSOR = CURSOR FOR SELECT SO_PHONG FROM @SO_PHONG_TABLE WHERE ID_HANG_PHONG = @ID_HANG_PHONG;

                        OPEN @SO_PHONG_CURSOR;

                        DECLARE @SO_PHONG VARCHAR(10);
                        FETCH NEXT FROM @SO_PHONG_CURSOR INTO @SO_PHONG;

                        WHILE @@FETCH_STATUS = 0
                            BEGIN

                                DECLARE @DON_GIA DECIMAL(10,2);
                                SELECT @DON_GIA = DON_GIA FROM CT_PHIEU_DAT WHERE ID_PD = @ID_PD AND ID_HANG_PHONG = @ID_HANG_PHONG;

                                UPDATE CT_PHIEU_DAT
                                SET TRANG_THAI = 1
                                WHERE ID_PD = @ID_PD AND ID_HANG_PHONG = @ID_HANG_PHONG;

                                INSERT INTO CT_PHIEU_THUE (NGAY_DEN, NGAY_DI, GIO_DEN, ID_PT, SO_PHONG, DON_GIA)
                                VALUES (@NGAY_DEN, @NGAY_DI_PD, CONVERT(TIME, GETDATE()), @INSERTED_ID_PT, @SO_PHONG, @DON_GIA);
                                DECLARE @INSERTED_ID_CT_PT BIGINT;
                                SELECT @INSERTED_ID_CT_PT = @@identity

                                UPDATE PHONG
                                SET ID_TT = 'RS00002'
                                WHERE SO_PHONG = @SO_PHONG;

                                CREATE TABLE #DANH_SACH_KHACH_O_TABLE (CMND NVARCHAR(20));

                                INSERT INTO #DANH_SACH_KHACH_O_TABLE
                                SELECT CAST(value AS NVARCHAR(20))
                                FROM STRING_SPLIT((SELECT DANH_SACH_KHACH_O FROM @SO_PHONG_TABLE WHERE SO_PHONG = @SO_PHONG), ',');

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

        IF @NGAY_DAT_FROM IS NOT NULL AND @NGAY_DAT_TO IS NOT NULL
            SET @sql = @sql + ' AND PD.NGAY_DAT between @NGAY_DAT_FROM and @NGAY_DAT_TO ';

        IF @NGAY_BD_THUE_FROM IS NOT NULL AND @NGAY_BD_THUE_TO IS NOT NULL
            SET @sql = @sql + ' AND PD.NGAY_BD_THUE between @NGAY_BD_THUE_FROM and @NGAY_BD_THUE_TO ';

        IF @CMND IS NOT NULL
            SET @sql = @sql + ' AND KH.CMND = @CMND ';

        IF @STATUS IS NOT NULL
            SET @sql = @sql + ' AND PD.TRANG_THAI = @STATUS ';

        PRINT '@sql = ' + @sql

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

-- ALTER the SP_GET_ROOM_LIST stored procedure
CREATE PROCEDURE [dbo].[SP_GET_ROOM_LIST](
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

        EXEC sp_executesql @sql,
             N'@GIA_TU FLOAT, @GIA_DEN FLOAT',
             @GIA_TU, @GIA_DEN;

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


-- Tạo Stored Procedure
CREATE PROCEDURE [dbo].[SP_UPDATE_STATUS_RESERVATION]
AS
BEGIN
    UPDATE dbo.PHIEU_DAT
    SET TRANG_THAI = 0
    WHERE GETDATE() > NGAY_DI and TRANG_THAI <> 2;
END;
go

