package com.my.hotel.repo;

import com.my.hotel.entity.RentalSlipDetail;
import com.my.hotel.entity.RoomType;
import com.my.hotel.entity.Service;
import com.my.hotel.entity.ServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepo extends JpaRepository<Service, String> {

    @Query(value = "SELECT s FROM ServiceDetail s WHERE s.rentalSlipDetail.id = :rentalSlipDetailId AND s.service.id = :serviceId")
    ServiceDetail getServiceDetailByKey(@Param("rentalSlipDetailId") Integer rentalSlipDetailId,
                                        @Param("serviceId") String serviceId);

    @Modifying
    @Query(value = "DELETE DICH_VU WHERE ID_DV = :serviceId", nativeQuery = true)
    void deleteService(@Param("serviceId") String serviceId);
}
