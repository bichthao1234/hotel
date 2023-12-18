package com.my.hotel.repo;

import com.my.hotel.entity.ReservationDetail;
import com.my.hotel.entity.key.ReservationDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationDetailRepo extends JpaRepository<ReservationDetail, ReservationDetailId> {
    List<ReservationDetail> findAllByReservationId(Integer reservationId);

}
