package com.my.hotel.repo;

import com.my.hotel.entity.PriceService;
import com.my.hotel.entity.PriceSurcharge;
import com.my.hotel.entity.key.PriceServiceId;
import com.my.hotel.entity.key.PriceSurchargeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PriceSurchargeRepo extends JpaRepository<PriceSurcharge, PriceSurchargeId> {

    @Modifying
    @Query(value = "INSERT INTO GIA_PHU_THU (ID_PHU_THU, NGAY_AP_DUNG, GIA, ID_NV)" +
            " VALUES (:surchargeId, :appliedDate, :price, :createdBy)"
            , nativeQuery = true)
    void savePriceSurcharge(@Param("surchargeId") String surchargeId,
                          @Param("appliedDate") Date appliedDate,
                          @Param("price") Double price,
                          @Param("createdBy") String createdBy);

    @Modifying
    @Query(value = "DELETE GIA_PHU_THU WHERE ID_PHU_THU = :surchargeId", nativeQuery = true)
    void deletePriceSurcharge(@Param("surchargeId") String surchargeId);
}
