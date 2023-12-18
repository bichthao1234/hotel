-- Create the hotel_management database if it doesn't exist
CREATE DATABASE IF NOT EXISTS hotel_management;

USE hotel_management;

-- Create the BO_PHAN table
CREATE TABLE IF NOT EXISTS BO_PHAN (
                                       ID_BP VARCHAR(20) PRIMARY KEY,
                                       TEN_BP NVARCHAR(100) NOT NULL
);

-- Create the NHOM_QUYEN table
CREATE TABLE IF NOT EXISTS NHOM_QUYEN (
                                          ID_NQ VARCHAR(20) PRIMARY KEY,
                                          TEN_NQ NVARCHAR(100) NOT NULL
);

-- Create the NHAN_VIEN table
CREATE TABLE IF NOT EXISTS NHAN_VIEN (
                                         ID_NV VARCHAR(20) PRIMARY KEY,
                                         HO NVARCHAR(50) NOT NULL,
                                         TEN NVARCHAR(50) NOT NULL,
                                         PHAI TINYINT(1),
                                         NGAY_SINH DATE,
                                         DIA_CHI NVARCHAR(255),
                                         SDT NVARCHAR(20),
                                         EMAIL NVARCHAR(255),
                                         USERNAME NVARCHAR(50) NOT NULL,
                                         PASSWORD NVARCHAR(255) NOT NULL,
                                         HINH NVARCHAR(255),
                                         ID_BP VARCHAR(20),
                                         FOREIGN KEY (ID_BP) REFERENCES BO_PHAN(ID_BP)
);

-- Create the PHAN_QUYEN table
CREATE TABLE IF NOT EXISTS PHAN_QUYEN (
                                          ID_NQ VARCHAR(20),
                                          ID_BP VARCHAR(20),
                                          PRIMARY KEY (ID_NQ, ID_BP),
                                          FOREIGN KEY (ID_NQ) REFERENCES NHOM_QUYEN(ID_NQ),
                                          FOREIGN KEY (ID_BP) REFERENCES BO_PHAN(ID_BP)
);

-- Create the QUAN_LY table
CREATE TABLE IF NOT EXISTS QUAN_LY (
                                       ID_BP VARCHAR(20),
                                       NGAY_BD_QL DATE,
                                       ID_NV VARCHAR(20),
                                       PRIMARY KEY (ID_BP, NGAY_BD_QL),
                                       FOREIGN KEY (ID_BP) REFERENCES BO_PHAN(ID_BP),
                                       FOREIGN KEY (ID_NV) REFERENCES NHAN_VIEN(ID_NV)
);

-- Create the KHACH_HANG table
CREATE TABLE IF NOT EXISTS KHACH_HANG (
                                          CMND NVARCHAR(20) PRIMARY KEY,
                                          HO NVARCHAR(50) NOT NULL,
                                          TEN NVARCHAR(50) NOT NULL,
                                          SDT NVARCHAR(20),
                                          EMAIL NVARCHAR(255),
                                          DIA_CHI NVARCHAR(255),
                                          MA_SO_THUE NVARCHAR(50)
);

-- Create the LOAI_PHONG table
CREATE TABLE IF NOT EXISTS LOAI_PHONG (
                                          ID_LP VARCHAR(20) PRIMARY KEY,
                                          TEN_LP NVARCHAR(100) NOT NULL
);

-- Create the KIEU_PHONG table
CREATE TABLE IF NOT EXISTS KIEU_PHONG (
                                          ID_KP VARCHAR(20) PRIMARY KEY,
                                          TEN_KP NVARCHAR(100) NOT NULL
);

-- Create the HANG_PHONG table
CREATE TABLE IF NOT EXISTS HANG_PHONG (
                                          ID_HANG_PHONG BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          MO_TA NVARCHAR(255),
                                          SO_LUONG_KHACH_O INT,
                                          ID_KP VARCHAR(20),
                                          ID_LP VARCHAR(20),
                                          FOREIGN KEY (ID_KP) REFERENCES KIEU_PHONG(ID_KP),
                                          FOREIGN KEY (ID_LP) REFERENCES LOAI_PHONG(ID_LP)
);

-- Create the GIA_HANG_PHONG table
CREATE TABLE IF NOT EXISTS GIA_HANG_PHONG (
                                              ID_HANG_PHONG BIGINT,
                                              NGAY_AP_DUNG DATE,
                                              NGAY_THIET_LAP DATE,
                                              GIA DECIMAL(10, 2) NOT NULL,
                                              ID_NV VARCHAR(20),
                                              PRIMARY KEY (ID_HANG_PHONG, NGAY_AP_DUNG),
                                              FOREIGN KEY (ID_HANG_PHONG) REFERENCES HANG_PHONG(ID_HANG_PHONG),
                                              FOREIGN KEY (ID_NV) REFERENCES NHAN_VIEN(ID_NV)
);

-- Create the TRANG_THAI table
CREATE TABLE IF NOT EXISTS TRANG_THAI (
                                          ID_TT VARCHAR(20) PRIMARY KEY,
                                          TEN_TRANG_THAI NVARCHAR(100) NOT NULL
);

-- Create the PHONG table
CREATE TABLE IF NOT EXISTS PHONG (
                                     SO_PHONG VARCHAR(10) PRIMARY KEY,
                                     TANG INT,
                                     ID_HANG_PHONG BIGINT,
                                     ID_TT VARCHAR(20),
                                     FOREIGN KEY (ID_HANG_PHONG) REFERENCES HANG_PHONG(ID_HANG_PHONG),
                                     FOREIGN KEY (ID_TT) REFERENCES TRANG_THAI(ID_TT)
);

-- Create the PHIEU_DAT table
CREATE TABLE IF NOT EXISTS PHIEU_DAT (
                                         ID_PD VARCHAR(20) PRIMARY KEY,
                                         NGAY_DAT DATE NOT NULL,
                                         NGAY_BD_THUE DATE NOT NULL,
                                         SO_NGAY_O INT NOT NULL,
                                         CMND NVARCHAR(20),
                                         ID_NV VARCHAR(20),
                                         FOREIGN KEY (CMND) REFERENCES KHACH_HANG(CMND),
                                         FOREIGN KEY (ID_NV) REFERENCES NHAN_VIEN(ID_NV)
);

-- Create the CT_PHIEU_DAT table
CREATE TABLE IF NOT EXISTS CT_PHIEU_DAT (
                                            ID_PD VARCHAR(20),
                                            ID_HANG_PHONG BIGINT,
                                            SO_LUONG_PHONG_O INT NOT NULL,
                                            DON_GIA DECIMAL(10, 2) NOT NULL,
                                            PRIMARY KEY (ID_PD, ID_HANG_PHONG),
                                            FOREIGN KEY (ID_PD) REFERENCES PHIEU_DAT(ID_PD),
                                            FOREIGN KEY (ID_HANG_PHONG) REFERENCES HANG_PHONG(ID_HANG_PHONG)
);

-- Create the PHIEU_THUE table
CREATE TABLE IF NOT EXISTS PHIEU_THUE (
                                          ID_PT VARCHAR(20) PRIMARY KEY,
                                          NGAY_LAP DATE NOT NULL,
                                          ID_NV VARCHAR(20),
                                          CMND NVARCHAR(20),
                                          ID_PD VARCHAR(20),
                                          FOREIGN KEY (ID_NV) REFERENCES NHAN_VIEN(ID_NV),
                                          FOREIGN KEY (CMND) REFERENCES KHACH_HANG(CMND),
                                          FOREIGN KEY (ID_PD) REFERENCES PHIEU_DAT(ID_PD)
);

-- Create the HOA_DON table
CREATE TABLE IF NOT EXISTS HOA_DON (
                                       ID_HD VARCHAR(20) PRIMARY KEY,
                                       NGAY_LAP DATE NOT NULL,
                                       ID_NV VARCHAR(20),
                                       ID_PT VARCHAR(20),
                                       FOREIGN KEY (ID_NV) REFERENCES NHAN_VIEN(ID_NV),
                                       FOREIGN KEY (ID_PT) REFERENCES PHIEU_THUE(ID_PT)
);

-- Create the CT_PHIEU_THUE table
CREATE TABLE IF NOT EXISTS CT_PHIEU_THUE (
                                             ID_CT_PT BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             NGAY_DEN DATE NOT NULL,
                                             GIO_DEN TIME,
                                             NGAY_DI DATE,
                                             TT_THANH_TOAN TINYINT(1) NOT NULL DEFAULT 0,
                                             ID_PT VARCHAR(20),
                                             SO_PHONG VARCHAR(10),
                                             ID_HD VARCHAR(20),
                                             FOREIGN KEY (ID_PT) REFERENCES PHIEU_THUE(ID_PT),
                                             FOREIGN KEY (SO_PHONG) REFERENCES PHONG(SO_PHONG),
                                             FOREIGN KEY (ID_HD) REFERENCES HOA_DON(ID_HD)
);

-- Create the CT_KHACH_O table
CREATE TABLE IF NOT EXISTS CT_KHACH_O (
                                          ID_CT_PT BIGINT,
                                          CMND NVARCHAR(20),
                                          PRIMARY KEY (ID_CT_PT, CMND),
                                          FOREIGN KEY (ID_CT_PT) REFERENCES CT_PHIEU_THUE(ID_CT_PT),
                                          FOREIGN KEY (CMND) REFERENCES KHACH_HANG(CMND)
);

-- Create the DOI_PHONG table
CREATE TABLE IF NOT EXISTS DOI_PHONG (
                                         ID_CT_PT BIGINT,
                                         SOPHONGMOI VARCHAR(10),
                                         NGAY_DEN DATE,
                                         NGAY_DI DATE,
                                         PRIMARY KEY (ID_CT_PT, SOPHONGMOI),
                                         FOREIGN KEY (ID_CT_PT) REFERENCES CT_PHIEU_THUE(ID_CT_PT),
                                         FOREIGN KEY (SOPHONGMOI) REFERENCES PHONG(SO_PHONG)
);

-- Create the DICH_VU table
CREATE TABLE IF NOT EXISTS DICH_VU (
                                       ID_DV VARCHAR(20) PRIMARY KEY,
                                       TEN_DV NVARCHAR(100) NOT NULL,
                                       MO_TA NVARCHAR(255)
);

-- Create the CT_DICH_VU table
CREATE TABLE IF NOT EXISTS CT_DICH_VU (
                                          ID_CT_PT BIGINT,
                                          ID_DV VARCHAR(20),
                                          NGAY_SU_DUNG DATE NOT NULL,
                                          GHI_CHU NVARCHAR(255),
                                          TT_THANH_TOAN TINYINT(1) NOT NULL DEFAULT 0,
                                          GIA DECIMAL(10, 2) NOT NULL,
                                          SO_LUONG INT NOT NULL,
                                          PRIMARY KEY (ID_CT_PT, ID_DV),
                                          FOREIGN KEY (ID_CT_PT) REFERENCES CT_PHIEU_THUE(ID_CT_PT),
                                          FOREIGN KEY (ID_DV) REFERENCES DICH_VU(ID_DV)
);

-- Create the GIA_DICH_VU table
CREATE TABLE IF NOT EXISTS GIA_DICH_VU (
                                           ID_DV VARCHAR(20),
                                           NGAY_AP_DUNG DATE,
                                           GIA DECIMAL(10, 2) NOT NULL,
                                           ID_NV VARCHAR(20),
                                           PRIMARY KEY (ID_DV, NGAY_AP_DUNG),
                                           FOREIGN KEY (ID_DV) REFERENCES DICH_VU(ID_DV),
                                           FOREIGN KEY (ID_NV) REFERENCES NHAN_VIEN(ID_NV)
);

-- Create the TIEN_NGHI table
CREATE TABLE IF NOT EXISTS TIEN_NGHI (
                                         ID_TN VARCHAR(20) PRIMARY KEY,
                                         TEN_TN NVARCHAR(255) NOT NULL
);

-- Create the CT_TIEN_NGHI table
CREATE TABLE IF NOT EXISTS CT_TIEN_NGHI (
                                            ID_TN VARCHAR(20),
                                            ID_HANG_PHONG BIGINT,
                                            MO_TA NVARCHAR(255),
                                            SO_LUONG INT NOT NULL,
                                            PRIMARY KEY (ID_TN, ID_HANG_PHONG),
                                            FOREIGN KEY (ID_TN) REFERENCES TIEN_NGHI(ID_TN),
                                            FOREIGN KEY (ID_HANG_PHONG) REFERENCES HANG_PHONG(ID_HANG_PHONG)
);

-- Create the KHUYEN_MAI table
CREATE TABLE IF NOT EXISTS KHUYEN_MAI (
                                          ID_KM VARCHAR(20) PRIMARY KEY,
                                          MO_TA_KM NVARCHAR(255) NOT NULL
);

-- Create the CT_KHUYEN_MAI table
CREATE TABLE IF NOT EXISTS CT_KHUYEN_MAI (
                                             ID_KM VARCHAR(20),
                                             ID_HANG_PHONG BIGINT,
                                             PHAN_TRAM_GIAM DECIMAL(5, 2) NOT NULL,
                                             PRIMARY KEY (ID_KM, ID_HANG_PHONG),
                                             FOREIGN KEY (ID_KM) REFERENCES KHUYEN_MAI(ID_KM),
                                             FOREIGN KEY (ID_HANG_PHONG) REFERENCES HANG_PHONG(ID_HANG_PHONG)
);

-- Create the PHU_THU table
CREATE TABLE IF NOT EXISTS PHU_THU (
                                       ID_PHU_THU VARCHAR(20) PRIMARY KEY,
                                       TEN_PHU_THU NVARCHAR(100) NOT NULL,
                                       LY_DO NVARCHAR(255) NOT NULL
);

-- Create the GIA_PHU_THU table
CREATE TABLE IF NOT EXISTS GIA_PHU_THU (
                                           ID_PHU_THU VARCHAR(20),
                                           NGAY_AP_DUNG DATE,
                                           GIA DECIMAL(10, 2) NOT NULL,
                                           ID_NV VARCHAR(20),
                                           PRIMARY KEY (ID_PHU_THU, NGAY_AP_DUNG),
                                           FOREIGN KEY (ID_PHU_THU) REFERENCES PHU_THU(ID_PHU_THU),
                                           FOREIGN KEY (ID_NV) REFERENCES NHAN_VIEN(ID_NV)
);

-- Create the CT_PHU_THU table
CREATE TABLE IF NOT EXISTS CT_PHU_THU (
                                          ID_PHU_THU VARCHAR(20),
                                          ID_CT_PT BIGINT,
                                          TT_THANH_TOAN TINYINT(1) NOT NULL DEFAULT 0,
                                          DON_GIA DECIMAL(10, 2) NOT NULL,
                                          SO_LUONG INT NOT NULL,
                                          PRIMARY KEY (ID_CT_PT, ID_PHU_THU),
                                          FOREIGN KEY (ID_CT_PT) REFERENCES CT_PHIEU_THUE(ID_CT_PT),
                                          FOREIGN KEY (ID_PHU_THU) REFERENCES PHU_THU(ID_PHU_THU)
);