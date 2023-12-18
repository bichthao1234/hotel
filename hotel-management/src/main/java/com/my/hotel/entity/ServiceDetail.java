package com.my.hotel.entity;

import com.my.hotel.entity.key.ConvenienceDetailId;
import com.my.hotel.entity.key.ServiceDetailId;
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
@Table(name = "CT_DICH_VU")
@IdClass(ServiceDetailId.class)
public class ServiceDetail {

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_DV")
	private Service service;

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_CT_PT")
	private RentalSlipDetail rentalSlipDetail;

	@Column(name = "SO_LUONG")
	private Integer quantity;

	@Column(name = "GIA")
	private Float price;

	@Column(name = "TT_THANH_TOAN")
	private Integer status;

	@Column(name = "GHI_CHU")
	private String note;

	@Column(name = "NGAY_SU_DUNG")
	private Date usingDate;

}
