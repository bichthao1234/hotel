package com.my.hotel.dto;

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
public class PromotionDto implements Serializable {
    private String id;
    private String description;
    private Date startDate;
    private Date endDate;
    private Boolean canDelete;
    private List<PromotionDetailDto> promotionDetails;
}