package com.my.hotel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRoomRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String customerId;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String email;
    private String taxNumber;
    private Float deposit;
    private Date startDate;
    private Date endDate;
    private List<String> roomClassList;
    private List<String> numberOfRoomsList;
    private String roomClassString;
    private String numberOfRoomsString;
    private String roomClass;
    private String numberOfRoom;

}
