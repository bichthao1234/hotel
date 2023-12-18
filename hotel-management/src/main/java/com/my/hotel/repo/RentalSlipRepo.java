package com.my.hotel.repo;

import com.my.hotel.entity.RentalSlip;
import com.my.hotel.entity.RentalSlipDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RentalSlipRepo extends JpaRepository<RentalSlip, Integer> {

    @Query(value = " dbo.SP_CONFIRM_RESERVATION @ID_PD = :reservationId, @ID_NV = :employeeId, @NGAY_DEN = :arrivalDate," +
            "    @SO_PHONG_LIST = :roomList "
            , nativeQuery = true)
    List<Object[]> convertFromReservation(@Param("reservationId") Integer reservationId,
                                          @Param("employeeId") String employeeId,
                                          @Param("arrivalDate") Date arrivalDate,
                                          @Param("roomList") String roomList);

    @Query(value = "SELECT rsd FROM RentalSlipDetail rsd WHERE rsd.rentalSlip.reservation.id = :reservationId")
    List<RentalSlipDetail> getRentalSlipDetailWithReservation(@Param("reservationId") Integer reservationId);

    @Modifying
    @Query(value = "exec dbo.CHECK_OUT_BY_ROOM " +
            "              @ID_CT_PT_LIST = :rentalSlipDetailId," +
            "              @ID_NV = :employeeId", nativeQuery = true)
    void checkOutByRoom(@Param("rentalSlipDetailId") String rentalSlipDetailId,
                        @Param("employeeId") String employeeId);

    @Modifying
    @Query(value = "exec dbo.QUICK_CHECK_IN " +
            "      @ID_NV = :employeeId," +
            "      @NGAY_DEN = :startDate," +
            "      @NGAY_DI = :endDate," +
            "      @ID_HANG_PHONG = :roomClassId," +
            "      @SO_PHONG = :roomId," +
            "      @DON_GIA = :price," +
            "      @CMND_KHACH_O_LIST = :customerIdList," +
            "      @CMND = :mainCustomerId", nativeQuery = true)
    void quickCheckIn(@Param("employeeId") String employeeId,
               @Param("startDate") Date startDate,
               @Param("endDate") Date endDate,
               @Param("roomClassId") Integer roomClassId,
               @Param("roomId") String roomId,
               @Param("price") Float price,
               @Param("customerIdList") String customerIdList,
               @Param("mainCustomerId") String mainCustomerId);

    @Query(value = "select rs from RentalSlip rs where :customerId is null or rs.customer.id = :customerId")
    List<RentalSlip> findAllByCustomerId(@Param("customerId") String customerId);

    @Query(value = "exec dbo.GET_REVENUE @TU = :dateFrom, @DEN = :dateTo", nativeQuery = true)
    List<Object[]> getRevenue(@Param("dateFrom") Date dateFrom,
                         @Param("dateTo") Date dateTo);

    @Query(value = "exec dbo.GET_ROOM_BOOKING_RATE @TU = :dateFrom, @DEN = :dateTo", nativeQuery = true)
    List<Object[]> getRoomBookingRate(@Param("dateFrom") Date dateFrom,
                                      @Param("dateTo") Date dateTo);
}
