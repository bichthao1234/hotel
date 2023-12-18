package com.my.hotel.repo;

import com.my.hotel.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepo extends JpaRepository<RoomType, String> {

    @Query("select rt from RoomType as rt  where rt.name like %:name%")
    List<RoomType> findByName(String name);
}
