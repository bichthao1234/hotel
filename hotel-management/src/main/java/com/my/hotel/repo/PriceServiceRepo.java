package com.my.hotel.repo;

import com.my.hotel.entity.PriceRoomClassification;
import com.my.hotel.entity.PriceService;
import com.my.hotel.entity.key.PriceRoomClassificationId;
import com.my.hotel.entity.key.PriceServiceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PriceServiceRepo extends JpaRepository<PriceService, PriceServiceId> {

    @Modifying
    @Query(value = "INSERT INTO GIA_DICH_VU (ID_DV, NGAY_AP_DUNG, GIA, ID_NV)" +
            " VALUES (:serviceId, :appliedDate, :price, :createdBy)"
            , nativeQuery = true)
    void savePriceService(@Param("serviceId") String serviceId,
                          @Param("appliedDate") Date appliedDate,
                          @Param("price") Double price,
                          @Param("createdBy") String createdBy);

    @Modifying
    @Query(value = "DELETE GIA_DICH_VU WHERE ID_DV = :serviceId", nativeQuery = true)
    void deletePriceService(@Param("serviceId") String serviceId);
}
