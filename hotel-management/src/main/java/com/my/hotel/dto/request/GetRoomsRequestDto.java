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
public class GetRoomsRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<String> roomKindList;
    private List<String> roomTypeList;
    private List<String> roomStatusList;
    private Float priceFrom;
    private Float priceTo;
    private Boolean hasPromotion;
    private Integer floor;
    private String roomKindString;
    private String roomTypeString;
    private String roomStatusString;

}
