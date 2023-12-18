package com.my.hotel.repo;

import com.my.hotel.entity.RoomKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoomKindRepo extends JpaRepository<RoomKind, String> {

    @Query("select rk from RoomKind as rk  where rk.name like %:name%")
    List<RoomKind> findByName(String name);
}
