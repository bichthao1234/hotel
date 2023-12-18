package com.my.hotel.entity;

import com.my.hotel.entity.key.ReservationDetailId;
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
@Table(name = "CT_PHIEU_DAT")
@IdClass(ReservationDetailId.class)
public class ReservationDetail {

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_PD")
	private Reservation reservation;

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_HANG_PHONG")
	private RoomClassification roomClassification;

	@Column(name = "DON_GIA")
	private Double price;

	@Column(name = "SO_LUONG_PHONG_O")
	private Integer numberOfRooms;

	@Column(name = "TRANG_THAI")
	private Integer status;

}
