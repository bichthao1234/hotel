package com.my.hotel.repo;

import com.my.hotel.entity.Invoice;
import com.my.hotel.entity.RentalSlipDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Integer> {

    @Query(value = "select rsd from RentalSlipDetail rsd where rsd.id in :rentalSlipDetailIdList")
    List<RentalSlipDetail> getRentalSlipDetailById(@Param("rentalSlipDetailIdList") List<Integer> rentalSlipDetailIdList);

}
