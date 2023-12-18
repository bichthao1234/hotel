package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.my.hotel.entity.Service}
 */
@Data
@Getter   
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ServiceDto implements Serializable {
    private String id;
    private String name;
    private String description;
    private String unit;
    private Double priceService;
    private Integer quantity;
    private Boolean status;
    private String note;
    private List<PriceServiceDto> priceServiceDtos;
    private Boolean canDelete;
}