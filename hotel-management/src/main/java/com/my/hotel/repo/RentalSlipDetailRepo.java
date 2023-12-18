package com.my.hotel.repo;

import com.my.hotel.entity.RentalSlipDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalSlipDetailRepo extends JpaRepository<RentalSlipDetail, Integer> {

    @Query(value = "SELECT rsd FROM RentalSlipDetail rsd WHERE rsd.id = :rentalSlipDetailId")
    RentalSlipDetail getRentalSlipDetailWithId(@Param("rentalSlipDetailId") Integer rentalSlipDetailId);

    @Query(value = "SELECT rsd FROM RentalSlipDetail rsd WHERE rsd.invoice.id = :invoiceId")
    List<RentalSlipDetail> getRentalSlipDetailByInvoiceId(@Param("invoiceId") Integer invoiceId);

}
