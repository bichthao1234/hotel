package com.my.hotel.entity;

import com.my.hotel.entity.key.ConvenienceDetailId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CT_TIEN_NGHI")
@IdClass(ConvenienceDetailId.class)
public class ConvenienceDetail {

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_TN")
	private Convenience convenience;

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_HANG_PHONG")
	private RoomClassification roomClassification;

	@Column(name = "SO_LUONG")
	private Integer quantity;

	@Column(name = "MO_TA")
	private String description;

}
