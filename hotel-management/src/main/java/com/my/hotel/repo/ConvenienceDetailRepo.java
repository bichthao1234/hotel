package com.my.hotel.repo;

import com.my.hotel.entity.ConvenienceDetail;
import com.my.hotel.entity.key.ConvenienceDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ConvenienceDetailRepo extends JpaRepository<ConvenienceDetail, ConvenienceDetailId> {
    @Modifying
    @Query(value = "INSERT INTO CT_TIEN_NGHI (ID_TN, ID_HANG_PHONG, SO_LUONG, MO_TA) VALUES (:convenienceId, :roomClassificationId, :quantity, :description)", nativeQuery = true)
    void saveWithQuery(@Param("convenienceId") String convenienceId,
                       @Param("roomClassificationId") Integer roomClassificationId,
                       @Param("quantity") Integer quantity,
                       @Param("description") String description);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CT_TIEN_NGHI SET MO_TA = :description, SO_LUONG = :quantity WHERE ID_TN = :convenienceId AND ID_HANG_PHONG = :roomClassificationId", nativeQuery = true)
    void update(
            @Param("convenienceId") String convenienceId,
            @Param("roomClassificationId") Integer roomClassificationId,
            @Param("description") String description,
            @Param("quantity") Integer quantity
    );
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CT_TIEN_NGHI WHERE ID_TN = :convenienceId AND ID_HANG_PHONG = :roomClassificationId", nativeQuery = true)
    void deleteByIds(
            @Param("convenienceId") String convenienceId,
            @Param("roomClassificationId") Integer roomClassificationId
    );
}
