package com.my.hotel.entity;

import com.my.hotel.entity.key.PriceRoomClassificationId;
import com.my.hotel.entity.key.PriceServiceId;
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
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "GIA_DICH_VU")
@IdClass(PriceServiceId.class)
public class PriceService {

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_DV")
	private Service service;

	@Id
	@Column(name = "NGAY_AP_DUNG")
	private Date appliedDate;

	@Column(name = "GIA")
	private Double price;

	@ManyToOne
	@JoinColumn(name = "ID_NV")
	private Employee createdBy;

}
