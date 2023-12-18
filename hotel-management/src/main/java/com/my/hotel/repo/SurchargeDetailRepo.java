package com.my.hotel.repo;

import com.my.hotel.entity.SurchargeDetail;
import com.my.hotel.entity.key.SurchargeDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SurchargeDetailRepo extends JpaRepository<SurchargeDetail, SurchargeDetailId> {


    @Query(value = "SELECT s FROM SurchargeDetail s WHERE s.rentalSlipDetail.id = :rentalSlipDetailId AND s.surcharge.id = :surchargeId")
    SurchargeDetail getSurchargeDetailByKey(@Param("rentalSlipDetailId") Integer rentalSlipDetailId,
                                            @Param("surchargeId") String surchargeId);


    @Modifying
    @Query(value = "INSERT INTO CT_PHU_THU (ID_CT_PT, ID_PHU_THU, DON_GIA, SO_LUONG, TT_THANH_TOAN)" +
            " VALUES (:rentalSlipDetailId, :surchargeId, :price, :quantity, :status)"
            , nativeQuery = true)
    void saveSurchargeDetail(@Param("rentalSlipDetailId") Integer rentalSlipDetailId,
                             @Param("surchargeId") String surchargeId,
                             @Param("price") Float price,
                             @Param("quantity") Integer quantity,
                             @Param("status") Integer status);

    @Modifying
    @Query(value = "DELETE CT_PHU_THU " +
            "WHERE ID_CT_PT = :rentalSlipDetailId AND CT_PHU_THU.ID_PHU_THU NOT IN :surchargeIdList"
            , nativeQuery = true)
    void deleteSurchargeDetail(@Param("rentalSlipDetailId") Integer rentalSlipDetailId,
                               @Param("surchargeIdList") List<String> surchargeIdList);

    @Modifying
    @Query(value = " UPDATE CT_PHU_THU" +
            " SET TT_THANH_TOAN = 1" +
            " WHERE ID_CT_PT = :rentalSlipDetailId AND ID_PHU_THU = :surchargeId "
            , nativeQuery = true)
    void updateStatusSurchargeDetail(@Param("rentalSlipDetailId") Integer rentalSlipDetailId,
                                     @Param("surchargeId") String surchargeId);

    @Query(value = "SELECT COUNT(*) FROM CT_PHU_THU WHERE ID_CT_PT = :rentalSlipDetailId AND ID_PHU_THU = :surchargeId"
            , nativeQuery = true)
    Integer checkId(@Param("rentalSlipDetailId") Integer rentalSlipDetailId,
                    @Param("surchargeId") String surchargeId);

    @Modifying
    @Query(value = " update CT_PHU_THU "
            + "set TT_THANH_TOAN = :status, DON_GIA =:price, SO_LUONG = :quantity "
            + "where ID_CT_PT = :rentalSlipDetailId and ID_PHU_THU = :surchargeId "
            , nativeQuery = true)
    void updateSurchargeDetail(@Param("rentalSlipDetailId") Integer rentalSlipDetailId,
                               @Param("surchargeId") String surchargeId,
                               @Param("price") Float price,
                               @Param("quantity") Integer quantity,
                               @Param("status") Integer status);

    @Modifying
    @Transactional
    @Query(value = "delete "
            + "from CT_PHU_THU "
            + "where ID_PHU_THU = :surchargeDetailId and ID_CT_PT = :rentalSlipDetailId "
            , nativeQuery = true)
    void removeSurchargeDetail(@Param("surchargeDetailId") String surchargeDetailId,
                               @Param("rentalSlipDetailId") Integer rentalSlipDetailId);
}