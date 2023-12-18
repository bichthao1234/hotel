package com.my.hotel.repo;

import com.my.hotel.entity.ServiceDetail;
import com.my.hotel.entity.key.ServiceDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ServiceDetailRepo extends JpaRepository<ServiceDetail, ServiceDetailId> {

    @Modifying
    @Query(value = "INSERT INTO CT_DICH_VU (ID_CT_PT, ID_DV, NGAY_SU_DUNG, GHI_CHU, GIA, SO_LUONG, TT_THANH_TOAN)" +
            " VALUES (:rentalSlipDetailId, :serviceId, :usingDay, :note, :price, :quantity, :status)"
            , nativeQuery = true)
    void saveServiceDetail(@Param("rentalSlipDetailId") Integer rentalSlipDetailId,
                           @Param("serviceId") String serviceId,
                           @Param("usingDay") Date usingDay,
                           @Param("note") String note,
                           @Param("price") Float price,
                           @Param("quantity") Integer quantity,
                           @Param("status") Integer status);

    @Modifying
    @Query(value = "DELETE CT_DICH_VU " +
            "WHERE ID_CT_PT = :rentalSlipDetailId AND ID_DV NOT IN :serviceIdList"
            , nativeQuery = true)
    void deleteServiceDetail(@Param("rentalSlipDetailId") Integer rentalSlipDetailId,
                             @Param("serviceIdList") List<String> serviceIdList);

    @Modifying
    @Query(value = " UPDATE CT_DICH_VU" +
            " SET TT_THANH_TOAN = 1" +
            " WHERE ID_CT_PT = :rentalSlipDetailId AND ID_DV = :serviceId "
            , nativeQuery = true)
    void updateStatusServiceDetail(@Param("rentalSlipDetailId") Integer rentalSlipDetailId,
                                   @Param("serviceId") String serviceId);

    @Query(value = "SELECT COUNT(*) FROM CT_DICH_VU WHERE ID_CT_PT = :rentalSlipDetailId AND ID_DV = :serviceId"
            , nativeQuery = true)
    Integer checkId(@Param("rentalSlipDetailId") Integer rentalSlipDetailId,
                    @Param("serviceId") String serviceId);

    @Modifying
    @Query(value = " update CT_DICH_VU"
            + " set NGAY_SU_DUNG = :usingDay, GHI_CHU = :note, TT_THANH_TOAN = :status, GIA =:price, SO_LUONG = :quantity"
            + " where ID_CT_PT = :rentalSlipDetailId and ID_DV = :serviceId "
            , nativeQuery = true)
    void updateServiceDetail(@Param("rentalSlipDetailId") Integer rentalSlipDetailId,
                           @Param("serviceId") String serviceId,
                           @Param("usingDay") Date usingDay,
                           @Param("note") String note,
                           @Param("price") Float price,
                           @Param("quantity") Integer quantity,
                           @Param("status") Integer status);
    @Modifying
    @Transactional
    @Query(value = "delete "
            + "from CT_DICH_VU "
            + "where ID_DV = :serviceId and ID_CT_PT = :rentalSlipDetailId "
            , nativeQuery = true)
    void removeServiceDetail(@Param("serviceId") String serviceId,
                             @Param("rentalSlipDetailId") Integer rentalSlipDetailId);
}
