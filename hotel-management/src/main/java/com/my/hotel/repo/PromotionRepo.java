package com.my.hotel.repo;

import com.my.hotel.entity.Promotion;
import com.my.hotel.entity.Service;
import com.my.hotel.entity.ServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepo extends JpaRepository<Promotion, String> {
    @Modifying
    @Query(value = "DELETE KHUYEN_MAI WHERE ID_KM = :promotionId", nativeQuery = true)
    void deletePromotion(@Param("promotionId") String promotionId);
}
