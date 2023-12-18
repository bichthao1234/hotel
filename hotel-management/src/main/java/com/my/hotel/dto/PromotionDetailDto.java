package com.my.hotel.dto;

import com.my.hotel.entity.Promotion;
import com.my.hotel.entity.RoomClassification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link com.my.hotel.entity.Promotion}
 */
@Data
@Getter   
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PromotionDetailDto implements Serializable {
    private String promotionId;
    private RoomClassificationDto roomClassification;
    private String roomClassId;
    private Float percent;
}