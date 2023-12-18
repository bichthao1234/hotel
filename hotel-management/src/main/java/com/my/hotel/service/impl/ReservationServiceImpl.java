package com.my.hotel.service.impl;

import com.my.hotel.common.Status;
import com.my.hotel.dto.ReservationDetailDto;
import com.my.hotel.dto.ReservationDto;
import com.my.hotel.dto.request.BookRoomRequestDto;
import com.my.hotel.dto.request.ChangeDateReservationRequestDto;
import com.my.hotel.dto.request.CheckRoomRequestDto;
import com.my.hotel.dto.request.GetReservationListRequestDto;
import com.my.hotel.dto.request.PriceRoomClassRequestDto;
import com.my.hotel.dto.response.PriceRoomClassResponseDto;
import com.my.hotel.entity.RentalSlip;
import com.my.hotel.entity.RentalSlipDetail;
import com.my.hotel.entity.ReservationDetail;
import com.my.hotel.entity.RoomClassification;
import com.my.hotel.repo.PriceRoomClassificationRepo;
import com.my.hotel.repo.ReservationDetailRepo;
import com.my.hotel.repo.ReservationRepo;
import com.my.hotel.repo.RoomClassificationRepo;
import com.my.hotel.service.ReservationService;
import com.my.hotel.utils.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	final private ReservationRepo reservationRepo;
	final private ReservationDetailRepo reservationDetailRepo;
	final private PriceRoomClassificationRepo priceRoomClassificationRepo;
	final private RoomClassificationRepo roomClassificationRepo;
	final private PlatformTransactionManager platformTransactionManager;

	@Override
	public boolean bookRoom(BookRoomRequestDto requestDto) throws Throwable {
		try {
			requestDto.setRoomClassString(Utilities.listToCsv(requestDto.getRoomClassList()));
			requestDto.setNumberOfRoomsString(Utilities.listToCsv(requestDto.getNumberOfRoomsList()));
			List<Object[]> objects = reservationRepo.bookRoom(requestDto);
			return true;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
		}
	}

	@Override
	public List<Map<String, Object>> checkRoom(CheckRoomRequestDto requestDto) throws Throwable {
		try {
			List<Map<String, Object>> ret = new ArrayList<>();
			List<Object[]> resultList = reservationRepo.checkRoom(requestDto);
			for (Object[] objects : resultList) {
				Map<String, Object> map = new HashMap<>();
				map.put("roomClassId", Utilities.parseInt(objects[0]));
				map.put("numberOfRoom", Utilities.parseInt(objects[1]));
				RoomClassification roomClassification = roomClassificationRepo.findById(map.get("roomClassId")
						.toString()).orElse(null);
				map.put("roomTypeId", roomClassification.getRoomType().getId());
				map.put("roomKindId", roomClassification.getRoomKind().getId());
				map.put("price", getRoomClassPrice(Integer.parseInt(roomClassification.getId()), new Date()));
				ret.add(map);
			}
			return ret;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
		}
	}

	@Override
	public Double getRoomClassPrice(Integer roomClassId, Date startDate) {
		return priceRoomClassificationRepo.getRoomClassPrice(roomClassId, startDate);
	}

	@Override
	public PriceRoomClassResponseDto getRoomClassPrice(PriceRoomClassRequestDto requestDto) {
		Double roomClassPrice = this.getRoomClassPrice(requestDto.getRoomClassId(), requestDto.getStartDate());
		Double promotionPercent = priceRoomClassificationRepo.getPromotion(requestDto.getRoomClassId());
		Double priceAfterPromotion;
		if (Utilities.isNull(promotionPercent)) {
			promotionPercent = (double) 0;
			priceAfterPromotion = roomClassPrice;
		} else {
			priceAfterPromotion = roomClassPrice - Utilities.calculateObtained(roomClassPrice, promotionPercent);
		}
		return PriceRoomClassResponseDto.builder()
				.roomClassPrice(roomClassPrice)
				.hasPromotion(!priceAfterPromotion.equals(roomClassPrice))
				.percentPromote(promotionPercent)
				.priceAfterPromotion(priceAfterPromotion)
				.build();
	}

	@Override
	public String getImagePathById(Integer roomClassId) {
		return roomClassificationRepo.getImagePathById(roomClassId);
	}

	@Override
	public List<ReservationDto> getReservationList(GetReservationListRequestDto requestDto) throws Throwable {
		try {
			List<ReservationDto> ret = new ArrayList<>();
			Integer status = null;
			if (Utilities.nonNull(requestDto.getStatus())) {
				status = Status.getStatusValue(requestDto.getStatus()).getValue();
			}
			List<Object[]> resultList = reservationRepo.getReservationList(requestDto.getCreatedDateFrom(), requestDto.getCreatedDateTo(),
					requestDto.getStartDateFrom(), requestDto.getStartDateTo(), requestDto.getCustomerId(), status);
			if (Utilities.nonEmptyList(resultList)) {
				ret = resultList.stream().map(ReservationDto::new).collect(Collectors.toList());
			}
			return ret;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
		}
	}

	@Override
	public List<ReservationDetailDto> getReservationDetail(Integer reservationId) throws Throwable {
		try {
			List<ReservationDetailDto> ret = new ArrayList<>();
			List<ReservationDetail> resultList = reservationRepo.getReservationDetail(reservationId);
			if (Utilities.nonEmptyList(resultList)) {
				int totalRooms = resultList.stream().mapToInt(ReservationDetail::getNumberOfRooms).sum();
				// chưa có phiếu thuê
				if (resultList.stream().allMatch(item -> Utilities.isEmptyList(item.getReservation().getRentalSlips()))) {
					for (ReservationDetail reservationDetail : resultList) {
						for (int i = 0; i < reservationDetail.getNumberOfRooms(); i++) {
							ReservationDetailDto dto = new ReservationDetailDto(reservationDetail);
							dto.setStatus(0);
							ret.add(dto);
						}
					}
					// có phiếu thuê
				} else {
					RentalSlip rentalSlip = resultList.get(0).getReservation().getRentalSlips().get(0);
					// phiêu thuê đã check in
					for (RentalSlipDetail rentalSlipDetail : rentalSlip.getRentalSlipDetails()) {
						ReservationDetailDto dto = new ReservationDetailDto(rentalSlipDetail);
						ret.add(dto);
					}

					if (ret.size() < totalRooms) {
						Map<RoomClassification, List<ReservationDetail>> collect1 = resultList.stream().collect(Collectors.groupingBy(ReservationDetail::getRoomClassification));
						for (Map.Entry<RoomClassification, List<ReservationDetail>> entry : collect1.entrySet()) {
							int numberOfRooms = entry.getValue().get(0).getNumberOfRooms();
							int checkedInReservation = (int) ret.stream()
									.filter(item -> item.getRoomClassId().equals(entry.getKey().getId())).count();
							int remain = numberOfRooms - checkedInReservation;
							for (int i = 0; i < remain; i++) {
								ReservationDetail reservationDetail = entry.getValue().get(0);
								ReservationDetailDto dto = new ReservationDetailDto(reservationDetail);
								dto.setStatus(0);
								ret.add(dto);
							}
						}
					}
				}
			}
			return ret;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
		}
	}

	@Override
	public Integer getRentalSlipId(Integer reservationId) {
		return reservationRepo.getRentalSlipId(reservationId);
	}

	@Transactional
	@Override
	public boolean cancelReservation(Integer reservationId, String employeeId) throws Throwable {
		try {
			reservationRepo.cancelReservation(reservationId, employeeId);
			return true;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause().getCause() != null) {
				throw e.getCause().getCause();
			}
			throw e;
		}
	}

    @Override
    public boolean changeDateRangeReservation(ChangeDateReservationRequestDto requestDto) throws Throwable {
        try {
			List<ReservationDetail> allByReservationId = reservationDetailRepo.findAllByReservationId(requestDto.getReservationId());
			for (ReservationDetail reservationDetail : allByReservationId) {
				CheckRoomRequestDto checkRoomRequestDto = new CheckRoomRequestDto();
				checkRoomRequestDto.setStartDate(requestDto.getStartDate());
				checkRoomRequestDto.setEndDate(requestDto.getEndDate());
				checkRoomRequestDto.setNumberOfRoom(String.valueOf(reservationDetail.getNumberOfRooms()));
				checkRoomRequestDto.setRoomClass(reservationDetail.getRoomClassification().getId());
				checkRoomRequestDto.setReservationId(reservationDetail.getReservation().getId());
				reservationRepo.checkRoom(checkRoomRequestDto);
			}
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
        }
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		reservationRepo.changeDateRangeReservation(requestDto.getReservationId(), requestDto.getStartDate(), requestDto.getEndDate());
		platformTransactionManager.commit(status);
		return true;
    }
}
