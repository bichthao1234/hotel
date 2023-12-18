package com.my.hotel.repo;

import com.my.hotel.dto.request.BookRoomRequestDto;
import com.my.hotel.dto.request.CheckRoomRequestDto;
import com.my.hotel.entity.Reservation;
import com.my.hotel.entity.ReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {

    @Query(value = "EXEC dbo.SP_BOOK_ROOM " +
            "                  @CMND = :#{#request.customerId}," +
            "                  @HO = :#{#request.firstName}," +
            "                  @TEN = :#{#request.lastName}," +
            "                  @SDT = :#{#request.phoneNumber}," +
            "                  @EMAIL = :#{#request.email}," +
            "                  @DIA_CHI = :#{#request.address}," +
            "                  @MA_SO_THUE = :#{#request.taxNumber}," +
            "                  @SO_TIEN_COC = :#{#request.deposit}," +
            "                  @NGAY_BD_THUE = :#{#request.startDate}," +
            "                  @NGAY_DI = :#{#request.endDate}," +
            "                  @ID_HANG_PHONG_LIST = :#{#request.roomClassString}," +
            "                  @SO_LUONG_PHONG_O_LIST = :#{#request.numberOfRoomsString}", nativeQuery = true)
    List<Object[]> bookRoom(@Param("request") BookRoomRequestDto request);

    @Query(value = " EXEC dbo.SP_CHECK_ROOM " +
            "                  @NGAY_BD_THUE = :#{#request.startDate}," +
            "                  @NGAY_DI = :#{#request.endDate}," +
            "                  @ID_HANG_PHONG = :#{#request.roomClass}," +
            "                  @SO_LUONG_PHONG_O = :#{#request.numberOfRoom}," +
            "                  @ID_PD = :#{#request.reservationId}", nativeQuery = true)
    List<Object[]> checkRoom(@Param("request") CheckRoomRequestDto request);

    @Query(value = " EXEC dbo.SP_GET_RESERVATION_LIST " +
            "            @NGAY_DAT_FROM = :createdDateFrom, " +
            "            @NGAY_DAT_TO = :createdDateTo, " +
            "            @NGAY_BD_THUE_FROM = :startDateFrom, " +
            "            @NGAY_BD_THUE_TO = :startDateTo, " +
            "            @CMND = :customerId, " +
            "            @STATUS = :status", nativeQuery = true)
    List<Object[]> getReservationList(@Param("createdDateFrom") Date createdDateFrom,
                                      @Param("createdDateTo") Date createdDateTo,
                                      @Param("startDateFrom") Date startDateFrom,
                                      @Param("startDateTo") Date startDateTo,
                                      @Param("customerId") String customerId,
                                      @Param("status") Integer status);


    @Query(value = "select rd from ReservationDetail rd where rd.reservation.id = :reservationId")
    List<ReservationDetail> getReservationDetail(@Param("reservationId") Integer reservationId);

    @Query(value = "SELECT PT.ID_PT " +
            "FROM PHIEU_DAT PD " +
            "INNER JOIN PHIEU_THUE PT on PD.ID_PD = PT.ID_PD " +
            "WHERE PT.ID_PD = :reservationId", nativeQuery = true)
    Integer getRentalSlipId(@Param("reservationId") Integer reservationId);

    @Modifying
    @Query(value = "UPDATE PHIEU_DAT" +
            " SET TRANG_THAI = 0, ID_NV = :employeeId " +
            " WHERE ID_PD = :reservationId", nativeQuery = true)
    void cancelReservation(@Param("reservationId") Integer reservationId,
                           @Param("employeeId") String employeeId);

    @Modifying
    @Query(value = "UPDATE PHIEU_DAT" +
            " SET NGAY_BD_THUE = :startDate, NGAY_DI = :endDate " +
            " WHERE ID_PD = :reservationId", nativeQuery = true)
    void changeDateRangeReservation(@Param("reservationId") Integer reservationId,
                           @Param("startDate") Date startDate,
                           @Param("endDate") Date endDate);
}
