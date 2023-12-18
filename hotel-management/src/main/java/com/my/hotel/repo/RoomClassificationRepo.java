package com.my.hotel.repo;

import com.my.hotel.entity.RoomClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoomClassificationRepo extends JpaRepository<RoomClassification, String> {

    @Query(value = "SELECT URL_ANH FROM ANH_HANG_PHONG WHERE ID_HANG_PHONG = :roomClassId", nativeQuery = true)
    String getImagePathById(@Param("roomClassId") Integer roomClassId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE HANG_PHONG SET MO_TA = :description, SO_LUONG_KHACH_O = :guestNum, ID_KP = :roomTypeId, ID_LP = :roomKindId WHERE ID_HANG_PHONG = :id", nativeQuery = true)
    void updateRoomClassification(@Param("id") Integer id,
                                  @Param("description") String description,
                                  @Param("guestNum") Integer guestNum,
                                  @Param("roomTypeId") String roomTypeId,
                                  @Param("roomKindId") String roomKindId);

}
