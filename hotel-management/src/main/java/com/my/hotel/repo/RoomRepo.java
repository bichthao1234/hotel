package com.my.hotel.repo;

import com.my.hotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface RoomRepo extends JpaRepository<Room, String> {

    @Query(value = " EXEC dbo.SP_GET_ROOM_LIST " +
            "              @LOAI_PHONG_ID_LIST = :roomKindString," +
            "              @KIEU_PHONG_ID_LIST = :roomTypeString," +
            "              @TRANG_THAI_ID_LIST = :roomStatusString," +
            "              @GIA_TU = :priceFrom," +
            "              @GIA_DEN = :priceTo," +
            "              @KHUYEN_MAI = :hasPromotion", nativeQuery = true)
    List<Object[]> getAllRooms(@Param("roomKindString") String roomKindString,
                               @Param("roomTypeString") String roomTypeString,
                               @Param("roomStatusString") String roomStatusString,
                               @Param("priceFrom") Float priceFrom,
                               @Param("priceTo") Float priceTo,
                               @Param("hasPromotion") Boolean hasPromotion);

    @Query(value = " SELECT * FROM PHONG WHERE ID_HANG_PHONG = :roomClassId " +
            " AND ID_TT IN ('RS00001', 'RS00003')", nativeQuery = true)
    List<Room> getAllAvailableRooms(@Param("roomClassId") String roomClassId);

    @Query(value = "select ctpt.ID_CT_PT, ctpt.NGAY_DEN, ctpt.GIO_DEN, ctpt.TT_THANH_TOAN, pd.NGAY_DI\n" +
            "from CT_PHIEU_THUE ctpt\n" +
            "inner join PHIEU_THUE PT on PT.ID_PT = ctpt.ID_PT\n" +
            "inner join PHIEU_DAT PD on PD.ID_PD = PT.ID_PD\n" +
            "where ctpt.SO_PHONG = :roomId and ctpt.TT_THANH_TOAN = 0", nativeQuery = true)
    List<Object[]> getRentalSlipDetailByRoom(@Param("roomId") String roomId);

    @Query(value = "select KH.CMND, KH.HO + ' ' + KH.TEN, KH.SDT, KH.EMAIL\n" +
            "from CT_KHACH_O ctko\n" +
            "inner join KHACH_HANG KH on KH.CMND = ctko.CMND\n" +
            "inner join CT_PHIEU_THUE CPT on CPT.ID_CT_PT = ctko.ID_CT_PT\n" +
            "where CPT.SO_PHONG = :roomId and CPT.TT_THANH_TOAN = 0", nativeQuery = true)
    List<Object[]> getGuestManifestByRoom(@Param("roomId") String roomId);

    @Query(value = "WITH LatestGIA AS (" +
            "    SELECT " +
            "        GDV.ID_DV, " +
            "        MAX(GDV.NGAY_AP_DUNG) AS NGAY_AP_DUNG " +
            "    FROM " +
            "        GIA_DICH_VU GDV " +
            "    WHERE GDV.NGAY_AP_DUNG <= GETDATE() " +
            "    GROUP BY GDV.ID_DV ) " +
            "SELECT DV.ID_DV, DV.TEN_DV, DV.DON_VI_TINH, CDV.SO_LUONG, GDV.GIA, CDV.TT_THANH_TOAN, CPT.ID_CT_PT " +
            "FROM CT_PHIEU_THUE CPT " +
            "INNER JOIN CT_DICH_VU CDV on CPT.ID_CT_PT = CDV.ID_CT_PT " +
            "INNER JOIN DICH_VU DV on DV.ID_DV = CDV.ID_DV " +
            "INNER JOIN LatestGIA GIA_DV on GIA_DV.ID_DV = DV.ID_DV " +
            "INNER JOIN GIA_DICH_VU GDV on GIA_DV.ID_DV = GDV.ID_DV AND GIA_DV.NGAY_AP_DUNG = GDV.NGAY_AP_DUNG " +
            "INNER JOIN PHONG P on P.SO_PHONG = CPT.SO_PHONG " +
            "WHERE CPT.SO_PHONG = :roomId and CPT.TT_THANH_TOAN = 0", nativeQuery = true)
    List<Object[]> getServiceByRoom(@Param("roomId") String roomId);

    @Query(value = "WITH LatestGIA AS (" +
            "    SELECT " +
            "        GPTHU.ID_PHU_THU, " +
            "        MAX(GPTHU.NGAY_AP_DUNG) AS NGAY_AP_DUNG " +
            "    FROM " +
            "        GIA_PHU_THU GPTHU " +
            "    WHERE GPTHU.NGAY_AP_DUNG <= GETDATE() " +
            "    GROUP BY GPTHU.ID_PHU_THU ) " +
            "SELECT PT.ID_PHU_THU, PT.TEN_PHU_THU, PT.LY_DO, CTPT.SO_LUONG, GPTHU.GIA, CTPT.TT_THANH_TOAN, CPT.ID_CT_PT " +
            "FROM CT_PHIEU_THUE CPT " +
            "INNER JOIN CT_PHU_THU CTPT on CPT.ID_CT_PT = CTPT.ID_CT_PT " +
            "INNER JOIN PHU_THU PT on PT.ID_PHU_THU = CTPT.ID_PHU_THU " +
            "INNER JOIN LatestGIA GIA_PT on GIA_PT.ID_PHU_THU = PT.ID_PHU_THU " +
            "INNER JOIN GIA_PHU_THU GPTHU on GIA_PT.ID_PHU_THU = GPTHU.ID_PHU_THU AND GIA_PT.NGAY_AP_DUNG = GPTHU.NGAY_AP_DUNG " +
            "INNER JOIN PHONG P on P.SO_PHONG = CPT.SO_PHONG " +
            "WHERE CPT.SO_PHONG = :roomId and CPT.TT_THANH_TOAN = 0", nativeQuery = true)
    List<Object[]> getSurchargeByRoom(@Param("roomId") String roomId);
    
    @Modifying
    @Query(value = "update PHONG set ID_TT = :statusId where SO_PHONG = :roomId", nativeQuery = true)
    void changeRoomStatus(@Param("roomId") String roomId, @Param("statusId") String statusId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO PHONG (SO_PHONG, TANG, ID_HANG_PHONG, ID_TT) VALUES (:roomNumber, :floor, :roomClassificationId, :roomStatusId)", nativeQuery = true)
    void insertRoom(@Param("roomNumber") String roomNumber,
            @Param("floor") Integer floor,
            @Param("roomClassificationId") Integer roomClassificationId,
            @Param("roomStatusId") String roomStatusId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE PHONG SET TANG = :floor, ID_HANG_PHONG = :roomClassificationId, ID_TT = :roomStatusId where SO_PHONG = :roomNumber", nativeQuery = true)
    void modifyRoom(@Param("roomNumber") String roomNumber,
                    @Param("floor") Integer floor,
                    @Param("roomClassificationId") Integer roomClassificationId,
                    @Param("roomStatusId") String roomStatusId);
}
