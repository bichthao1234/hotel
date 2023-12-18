package com.my.hotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceRoomClassResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double roomClassPrice;
    private Double percentPromote;
    private Double priceAfterPromotion;
    private Boolean hasPromotion;
}
