package com.my.hotel.repo;

import com.my.hotel.entity.Service;
import com.my.hotel.entity.Surcharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SurchargeRepo extends JpaRepository<Surcharge, String> {
    @Modifying
    @Query(value = "DELETE PHU_THU WHERE ID_PHU_THU = :surchargeId", nativeQuery = true)
    void deleteSurcharge(@Param("surchargeId") String surchargeId);
}
