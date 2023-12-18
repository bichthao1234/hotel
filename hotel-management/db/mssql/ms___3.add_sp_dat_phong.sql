USE
    [hotel_management]
GO
/****** Object:  StoredProcedure [dbo].[SP_BOOK_ROOM]    Script Date: 10/10/2023 10:24:00 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- ALTER the SP_BOOK_ROOM stored procedure
ALTER PROCEDURE
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
        -- Check Customer with @CMND
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

        -- Declare a cursor to iterate through the tables
        DECLARE @ID_HANG_PHONG_CURSOR CURSOR;
        DECLARE @SO_LUONG_PHONG_O_CURSOR CURSOR;


        DECLARE @ID_HANG_PHONG BIGINT;
        DECLARE @SO_LUONG_PHONG_O INT;

        -- Split the delimited strings into tables
        DECLARE @ID_HANG_PHONG_TABLE TABLE (ID BIGINT);
        DECLARE @SO_LUONG_PHONG_O_TABLE TABLE (SO_LUONG INT);

        INSERT INTO @ID_HANG_PHONG_TABLE
        SELECT CAST(value AS BIGINT)
        FROM STRING_SPLIT(@ID_HANG_PHONG_LIST, ',');

        INSERT INTO @SO_LUONG_PHONG_O_TABLE
        SELECT CAST(value AS INT)
        FROM STRING_SPLIT(@SO_LUONG_PHONG_O_LIST, ',');

-- Insert into PHIEU_DAT

        INSERT INTO PHIEU_DAT(NGAY_DAT, NGAY_BD_THUE, NGAY_DI, TRANG_THAI, CMND)
        VALUES (GETDATE(), @NGAY_BD_THUE, @NGAY_DI, 1, @CMND) -- Status = 1 = Da dat
        PRINT 'Inserted into PHIEU_DAT complete'

-- Get the inserted ID_PD
        DECLARE @INSERTED_ID_PD BIGINT;
        SELECT @INSERTED_ID_PD = @@identity
        PRINT 'Get the inserted ID_PD complete ' + CAST(@INSERTED_ID_PD as VARCHAR)

        -- Initialize the cursors
        SET @ID_HANG_PHONG_CURSOR = CURSOR FOR SELECT ID FROM @ID_HANG_PHONG_TABLE;
        SET @SO_LUONG_PHONG_O_CURSOR = CURSOR FOR SELECT SO_LUONG FROM @SO_LUONG_PHONG_O_TABLE;

        OPEN @ID_HANG_PHONG_CURSOR;
        OPEN @SO_LUONG_PHONG_O_CURSOR;

-- Fetch the initial row
        FETCH NEXT FROM @ID_HANG_PHONG_CURSOR INTO @ID_HANG_PHONG;
        FETCH NEXT FROM @SO_LUONG_PHONG_O_CURSOR INTO @SO_LUONG_PHONG_O;

-- Loop through the tables and process the values
        PRINT
            'Start the loop'
        WHILE @@FETCH_STATUS = 0
            BEGIN
                INSERT INTO @Result (ID_HANG_PHONG, SO_LUONG_PHONG_O)
                VALUES (@ID_HANG_PHONG, @SO_LUONG_PHONG_O);
                PRINT
                        '---@ID_HANG_PHONG: ' + CAST(@ID_HANG_PHONG as VARCHAR) +
                        ', @SO_LUONG_PHONG_O: ' + CAST(@SO_LUONG_PHONG_O as VARCHAR) + '---'

                EXEC dbo.SP_CHECK_ROOM
                     @NGAY_BD_THUE = @NGAY_BD_THUE,
                     @NGAY_DI = @NGAY_DI,
                     @ID_HANG_PHONG = @ID_HANG_PHONG,
                     @SO_LUONG_PHONG_O = @SO_LUONG_PHONG_O;

                -- Get the price of room
                DECLARE @GIA_HP DECIMAL(10, 2);
                SELECT TOP (1) @GIA_HP = GIA
                FROM GIA_HANG_PHONG ghp
                         INNER JOIN HANG_PHONG h on h.ID_HANG_PHONG = ghp.ID_HANG_PHONG
                WHERE h.ID_HANG_PHONG = @ID_HANG_PHONG
                  AND ghp.NGAYAPDUNG <= @NGAY_BD_THUE
                ORDER BY ghp.NGAYAPDUNG DESC
                PRINT 'Get the price of room complete ' + CAST(@GIA_HP as VARCHAR)

                -- Get the promotion
                DECLARE @PHAN_TRAM_GIAM DECIMAL(10,2);
                SELECT @PHAN_TRAM_GIAM = MAX(CT.PHAN_TRAM_GIAM)
                FROM CT_KHUYEN_MAI CT
                         INNER JOIN KHUYEN_MAI KM ON CT.ID_KM = KM.ID_KM
                         INNER JOIN HANG_PHONG HP on HP.ID_HANG_PHONG = CT.ID_HANG_PHONG
                WHERE CT.ID_HANG_PHONG = @ID_HANG_PHONG
                  AND GETDATE() BETWEEN KM.NGAY_BAT_DAU AND KM.NGAY_KET_THUC;
                PRINT 'Get the promotion complete ' + CAST(@PHAN_TRAM_GIAM as VARCHAR)

                DECLARE @DON_GIA DECIMAL(10,2);
                SELECT @DON_GIA = @GIA_HP * (1 - ISNULL(@PHAN_TRAM_GIAM, 0) / 100)
                PRINT 'Get the price after promotion complete ' + CAST(@DON_GIA as VARCHAR)

                -- Insert into CT_PHIEU_DAT table
                INSERT
                INTO CT_PHIEU_DAT(ID_PD, ID_HANG_PHONG, SO_LUONG_PHONG_O, DON_GIA)
                VALUES (@INSERTED_ID_PD, @ID_HANG_PHONG, @SO_LUONG_PHONG_O, @DON_GIA)


                -- Fetch the next rows
                FETCH NEXT FROM @ID_HANG_PHONG_CURSOR INTO @ID_HANG_PHONG;
                FETCH NEXT FROM @SO_LUONG_PHONG_O_CURSOR INTO @SO_LUONG_PHONG_O;
            END;

        -- Close and deallocate the cursors
        CLOSE @ID_HANG_PHONG_CURSOR;
        DEALLOCATE
            @ID_HANG_PHONG_CURSOR;
        CLOSE @SO_LUONG_PHONG_O_CURSOR;
        DEALLOCATE
            @SO_LUONG_PHONG_O_CURSOR;

        -- Get the statying days of the reservation
        DECLARE @SO_NGAY_O INT;
        SELECT @SO_NGAY_O = DATEDIFF(DAY, PD.NGAY_BD_THUE, PD.NGAY_DI)
        FROM PHIEU_DAT PD
        WHERE PD.ID_PD = @INSERTED_ID_PD;
        PRINT 'Get the statying days of the reservation complete ' + CAST(@SO_NGAY_O as VARCHAR)

        -- Get the total price of the reservation
        DECLARE @TONG_GIA_PHIEU_DAT INT;
        SELECT @TONG_GIA_PHIEU_DAT = SUM(DON_GIA * SO_LUONG_PHONG_O) * @SO_NGAY_O
        FROM CT_PHIEU_DAT
        WHERE ID_PD = @INSERTED_ID_PD
        PRINT 'Get the total price of the reservation complete ' + CAST(@TONG_GIA_PHIEU_DAT as VARCHAR)

-- Update the deposit
        IF @SO_TIEN_COC < (@TONG_GIA_PHIEU_DAT * 0.2)
            BEGIN
                DECLARE
                    @ErrorMessage NVARCHAR(max) = 'Invalid deposit! It must be greater than '
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


-- Select the result table
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

