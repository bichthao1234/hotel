package com.my.hotel.entity;

import com.my.hotel.entity.key.ServiceDetailId;
import com.my.hotel.entity.key.SurchargeDetailId;
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
@Table(name = "CT_PHU_THU")
@IdClass(SurchargeDetailId.class)
public class SurchargeDetail {

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_PHU_THU")
	private Surcharge surcharge;

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_CT_PT")
	private RentalSlipDetail rentalSlipDetail;

	@Column(name = "SO_LUONG")
	private Integer quantity;

	@Column(name = "DON_GIA")
	private Float price;

	@Column(name = "TT_THANH_TOAN")
	private Integer status;

}
