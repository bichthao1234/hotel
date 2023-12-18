package com.my.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PHIEU_DAT")
public class Reservation {

	@Id
	@Column(name = "ID_PD")
	private Integer id;

	@Column(name = "NGAY_DAT")
	private Date createdDate;

	@Column(name = "NGAY_BD_THUE")
	private Date startDate;

	@Column(name = "NGAY_DI")
	private Date endDate;

	@Column(name = "TRANG_THAI")
	private String status;

	@Column(name = "SO_TIEN_COC")
	private Float deposit;

	@ManyToOne
	@JoinColumn(name = "CMND")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "ID_NV")
	private Employee createdBy;

	@OneToMany(mappedBy = "reservation")
	private List<ReservationDetail> reservationDetails;

	@OneToMany(mappedBy = "reservation")
	private List<RentalSlip> rentalSlips;

}
