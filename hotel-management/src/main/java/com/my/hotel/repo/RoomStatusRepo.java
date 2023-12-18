package com.my.hotel.repo;

import com.my.hotel.entity.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomStatusRepo extends JpaRepository<RoomStatus, String> {

}
