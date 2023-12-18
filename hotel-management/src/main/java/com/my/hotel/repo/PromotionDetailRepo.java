package com.my.hotel.repo;

import com.my.hotel.entity.PromotionDetail;
import com.my.hotel.entity.ServiceDetail;
import com.my.hotel.entity.key.PromotionDetailId;
import com.my.hotel.entity.key.ServiceDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PromotionDetailRepo extends JpaRepository<PromotionDetail, PromotionDetailId> {
    @Modifying
    @Query(value = "INSERT INTO CT_KHUYEN_MAI (ID_KM, ID_HANG_PHONG, PHAN_TRAM_GIAM)" +
            " VALUES (:promotionId, :roomClassId, :percent)", nativeQuery = true)
    void savePromotionDetail(@Param("promotionId") String promotionId,
                             @Param("roomClassId") String roomClassId,
                             @Param("percent") Float percent);
}
