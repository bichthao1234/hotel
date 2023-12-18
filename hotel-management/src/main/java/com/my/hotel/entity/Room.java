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
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PHONG")
public class Room {

	@Id
	@Column(name = "SO_PHONG")
	private String id;

	@Column(name = "TANG")
	private Integer floor;

	@ManyToOne
	@JoinColumn(name = "ID_HANG_PHONG")
	private RoomClassification roomClassification;

	@ManyToOne
	@JoinColumn(name = "ID_TT")
	private RoomStatus roomStatus;

	@OneToMany(mappedBy = "room")
	private List<RentalSlipDetail> rentalSlipDetails;

}
