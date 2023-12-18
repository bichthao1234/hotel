package com.my.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "HOA_DON")
public class Invoice {

	@Id
	@Column(name = "ID_HD")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "NGAY_LAP")
	private Date createdDate;

	@ManyToOne
	@JoinColumn(name = "ID_NV")
	private Employee createdBy;

	@ManyToOne
	@JoinColumn(name = "ID_PT")
	private RentalSlip rentalSlip;

	@OneToMany(mappedBy = "invoice")
	private List<RentalSlipDetail> rentalSlipDetails;

}
