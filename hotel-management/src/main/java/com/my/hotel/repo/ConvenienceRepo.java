package com.my.hotel.repo;

import com.my.hotel.entity.Convenience;
import com.my.hotel.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConvenienceRepo extends JpaRepository<Convenience, String> {
    @Query("select c from Convenience as c where c.name like %:name%")
    List<Convenience> findByName(String name);

}
