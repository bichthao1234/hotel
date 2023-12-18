package com.my.hotel.repo;

import com.my.hotel.entity.PriceRoomClassification;
import com.my.hotel.entity.key.PriceRoomClassificationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PriceRoomClassificationRepo extends JpaRepository<PriceRoomClassification, PriceRoomClassificationId> {

    @Query(value = "SELECT TOP (1) GIA " +
            "                FROM GIA_HANG_PHONG ghp " +
            "                         INNER JOIN HANG_PHONG h on h.ID_HANG_PHONG = ghp.ID_HANG_PHONG " +
            "                WHERE h.ID_HANG_PHONG = :roomClassId " +
            "                  AND ghp.NGAYAPDUNG <= :startDate " +
            "                ORDER BY ghp.NGAYAPDUNG DESC", nativeQuery = true)
    Double getRoomClassPrice(@Param("roomClassId") Integer roomClassId,
                                       @Param("startDate") Date startDate);

    @Query(value = "SELECT MAX(CT.PHAN_TRAM_GIAM) FROM CT_KHUYEN_MAI CT " +
            "    INNER JOIN KHUYEN_MAI KM ON CT.ID_KM = KM.ID_KM " +
            "    INNER JOIN HANG_PHONG HP on HP.ID_HANG_PHONG = CT.ID_HANG_PHONG " +
            " WHERE CT.ID_HANG_PHONG = :roomClassId " +
            "  AND GETDATE() BETWEEN KM.NGAY_BAT_DAU AND KM.NGAY_KET_THUC", nativeQuery = true)
    Double getPromotion(@Param("roomClassId") Integer roomClassId);

    @Modifying
    @Query(value = "INSERT INTO GIA_HANG_PHONG (ID_HANG_PHONG, NGAYAPDUNG, NGAY_THIET_LAP, GIA, ID_NV) VALUES (:roomClassificationId, :appliedDate, :createdDate, :price, :createdById)", nativeQuery = true)
    void saveWithQuery(@Param("roomClassificationId") Integer roomClassificationId,
                                       @Param("appliedDate") Date appliedDate,
                                       @Param("createdDate") Date createdDate,
                                       @Param("price") Double price,
                                       @Param("createdById") String createdById);


    @Modifying
    @Query(value = "INSERT INTO GIA_HANG_PHONG (ID_HANG_PHONG, NGAYAPDUNG, NGAY_THIET_LAP, GIA, ID_NV)" +
            " VALUES (:roomClassId, :appliedDate, :createdDate, :price, :createdBy)"
            , nativeQuery = true)
    void savePriceRoomClassification(@Param("roomClassId") String roomClassId,
                                     @Param("appliedDate") Date appliedDate,
                                     @Param("createdDate") Date createdDate,
                                     @Param("price") Double price,
                                     @Param("createdBy") String createdBy);
}
