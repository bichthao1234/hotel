package com.my.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "CT_PHIEU_THUE")
public class RentalSlipDetail {

	@Id
	@Column(name = "ID_CT_PT")
	private Integer id;

	@Column(name = "GIO_DEN")
	private Time arrivalTime;

	@Column(name = "NGAY_DEN")
	private Date arrivalDate;

	@Column(name = "NGAY_DI")
	private Date departureDate;

	@Column(name = "TT_THANH_TOAN")
	private Integer paymentStatus;

	@Column(name = "DON_GIA")
	private Float price;

	@ManyToOne
	@JoinColumn(name = "ID_PT")
	private RentalSlip rentalSlip;

	@ManyToOne
	@JoinColumn(name = "ID_HD")
	private Invoice invoice;

	@ManyToOne
	@JoinColumn(name = "SO_PHONG")
	private Room room;

	@OneToMany(mappedBy = "rentalSlipDetail")
	private List<ServiceDetail> serviceDetails;

	@OneToMany(mappedBy = "rentalSlipDetail")
	private List<SurchargeDetail> surchargeDetails;

	@OneToMany(mappedBy = "rentalSlipDetail")
	private List<GuestManifest> guestManifests;

	public void addServiceDetails(ServiceDetail serviceDetail) {
		if (this.serviceDetails == null) {
			this.serviceDetails = new ArrayList<>();
		}
		serviceDetail.setRentalSlipDetail(this);
		this.serviceDetails.add(serviceDetail);
	}

	public void addSurchargeDetails(SurchargeDetail surchargeDetail) {
		if (this.surchargeDetails == null) {
			this.surchargeDetails = new ArrayList<>();
		}
		surchargeDetail.setRentalSlipDetail(this);
		this.surchargeDetails.add(surchargeDetail);
	}
}
