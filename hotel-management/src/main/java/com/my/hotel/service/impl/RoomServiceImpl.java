package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.ChangeRoomStatusDto;
import com.my.hotel.dto.NewRoomReqDto;
import com.my.hotel.dto.RoomDto;
import com.my.hotel.dto.request.GetRoomsRequestDto;
import com.my.hotel.entity.RentalSlipDetail;
import com.my.hotel.entity.Room;
import com.my.hotel.repo.RentalSlipDetailRepo;
import com.my.hotel.repo.RoomRepo;
import com.my.hotel.service.RoomService;
import com.my.hotel.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepo repo;

    @Autowired
    private RentalSlipDetailRepo rentalSlipDetailRepo;

    @Override
    public List<RoomDto> getAllRooms(GetRoomsRequestDto requestDto) throws Throwable {
        try {
            requestDto.setRoomTypeString(Utilities.listToCsv(requestDto.getRoomTypeList()));
            requestDto.setRoomKindString(Utilities.listToCsv(requestDto.getRoomKindList()));
            requestDto.setRoomStatusString(Utilities.listToCsv(requestDto.getRoomStatusList()));
            List<Object[]> allRooms = repo.getAllRooms(requestDto.getRoomKindString(), requestDto.getRoomTypeString(),
                    requestDto.getRoomStatusString(), requestDto.getPriceFrom(), requestDto.getPriceTo(),
                    requestDto.getHasPromotion());
            if (Utilities.nonEmptyList(allRooms)) {
                List<RoomDto> roomDtos = allRooms.stream().map(RoomDto::new).collect(Collectors.toList());
                if (Utilities.nonNull(requestDto.getFloor())) {
                    roomDtos = roomDtos.stream().filter(item -> requestDto.getFloor().equals(item.getFloor()))
                            .collect(Collectors.toList());
                }
                return roomDtos;
            }
            return null;
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
        }
    }

    @Override
    public List<RoomDto> getAllAvailableRooms(String roomClassId) throws Throwable {
        try {
            List<RoomDto> ret = new ArrayList<>();
            List<Room> resultList = repo.getAllAvailableRooms(roomClassId);
            if (Utilities.nonEmptyList(resultList)) {
                ret = ObjectMapperUtils.mapAll(resultList, RoomDto.class);
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
    public List<Map<String, Object>> getRentalSlipDetailByRoom(String roomId) throws Throwable {
        try {
            List<Map<String, Object>> ret = new ArrayList<>();
            List<Object[]> resultList = repo.getRentalSlipDetailByRoom(roomId);
            if (Utilities.nonEmptyList(resultList)) {
                for (Object[] objects : resultList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("rentalSlipId", objects[0]); // rentalSlipDetailId
                    map.put("arrivalDate", objects[1]);
                    map.put("arrivalTime", objects[2]);
                    map.put("status", objects[3]);
                    map.put("departureDate", objects[4]);
                    RentalSlipDetail rentalSlip = rentalSlipDetailRepo.findById(Utilities.parseInt(map.get("rentalSlipId")))
                            .orElse(null);
                    map.put("id", rentalSlip != null ? rentalSlip.getRentalSlip().getId() : null); //rentalSlipId
                    map.put("haveService", rentalSlip != null && Utilities.nonEmptyList(rentalSlip.getServiceDetails()));
                    map.put("haveSurcharge", rentalSlip != null && Utilities.nonEmptyList(rentalSlip.getSurchargeDetails()));
                    ret.add(map);
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
    public List<Map<String, Object>> getGuestManifestByRoom(String roomId) throws Throwable {
        try {
            List<Map<String, Object>> ret = new ArrayList<>();
            List<Object[]> resultList = repo.getGuestManifestByRoom(roomId);
            if (Utilities.nonEmptyList(resultList)) {
                for (Object[] objects : resultList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("customerId", objects[0]);
                    map.put("customerDisplayName", objects[1]);
                    map.put("phone", objects[2]);
                    map.put("email", objects[3]);
                    ret.add(map);
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
    public List<Map<String, Object>> getServiceByRoom(String roomId) throws Throwable {
        try {
            List<Map<String, Object>> ret = new ArrayList<>();
            List<Object[]> resultList = repo.getServiceByRoom(roomId);
            if (Utilities.nonEmptyList(resultList)) {
                for (Object[] objects : resultList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("serviceId", objects[0]);
                    map.put("serviceName", objects[1]);
                    map.put("unit", objects[2]);
                    map.put("quantity", objects[3]);
                    map.put("price", objects[4]);
                    map.put("status", objects[5]);
                    map.put("rentalSlipDetailId", objects[6]);
                    ret.add(map);
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
    public List<Map<String, Object>> getSurchargeByRoom(String roomId) throws Throwable {
        try {
            List<Map<String, Object>> ret = new ArrayList<>();
            List<Object[]> resultList = repo.getSurchargeByRoom(roomId);
            if (Utilities.nonEmptyList(resultList)) {
                for (Object[] objects : resultList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("surchargeId", objects[0]);
                    map.put("surchargeName", objects[1]);
                    map.put("description", objects[2]);
                    map.put("quantity", objects[3]);
                    map.put("price", objects[4]);
                    map.put("status", objects[5]);
                    map.put("rentalSlipDetailId", objects[6]);
                    ret.add(map);
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
    @Transactional
    public boolean changeRoomStatus(ChangeRoomStatusDto requestDto) throws Exception {
        try {
            this.repo.changeRoomStatus(requestDto.getRoomId(), requestDto.getStatusId());
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean createRoom(NewRoomReqDto requestDto) {
        try {
            boolean isExistRoom = this.checkExistRoom(requestDto);
            if (isExistRoom) {
                throw new RuntimeException("Số phòng đã tồn tại");
            } else {
                this.repo.insertRoom(requestDto.getId(),
                        requestDto.getFloor(), Integer.parseInt(requestDto.getRoomClassificationId()),
                        requestDto.getRoomStatusId());
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean modifyRoom(NewRoomReqDto requestDto) {
        try {
            this.repo.modifyRoom(requestDto.getId(),
                    requestDto.getFloor(), Integer.parseInt(requestDto.getRoomClassificationId()),
                    requestDto.getRoomStatusId());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean checkExistRoom(NewRoomReqDto requestDto) {
        return this.repo.findAll().stream().anyMatch((room -> room.getId().equals(requestDto.getId())));
    }
}
