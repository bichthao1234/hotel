package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.my.hotel.entity.Convenience}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvenienceDto implements Serializable {
    String id;
    String name;
    String icon;
}