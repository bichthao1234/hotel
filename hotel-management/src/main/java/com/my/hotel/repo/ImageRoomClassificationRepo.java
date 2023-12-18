package com.my.hotel.repo;

import com.my.hotel.entity.ImageRoomClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRoomClassificationRepo extends JpaRepository<ImageRoomClassification, String> {
    @Query("SELECT ir FROM ImageRoomClassification ir WHERE ir.roomClassification.id = :roomClassificationId")
    ImageRoomClassification findByRoomClassificationId(@Param("roomClassificationId") String roomClassificationId);

}
