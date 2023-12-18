package com.my.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "HANG_PHONG")
public class RoomClassification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_HANG_PHONG")
	private String id;

	@Column(name = "SO_LUONG_KHACH_O")
	private Integer guestNum;

	@Column(name = "MO_TA")
	private String description;

	@ManyToOne
	@JoinColumn(name = "ID_KP")
	private RoomType roomType;

	@ManyToOne
	@JoinColumn(name = "ID_LP")
	private RoomKind roomKind;

	@OneToMany(mappedBy = "roomClassification")
	private List<ConvenienceDetail> convenienceDetails;

	@OneToMany(mappedBy = "roomClassification")
	private List<Room> rooms;

	@OneToMany(mappedBy = "roomClassification")
	private List<ReservationDetail> reservationDetails;

	@OneToMany(mappedBy = "roomClassification")
	private List<PriceRoomClassification> priceRoomClassifications;

	@OneToMany(mappedBy = "roomClassification")
	private List<PromotionDetail> promotionDetails;

	public void addPriceRoomClassifications(PriceRoomClassification priceRoomClassification) {
		if (this.priceRoomClassifications == null) {
			this.priceRoomClassifications = new ArrayList<>();
		}
		priceRoomClassification.setRoomClassification(this);
		this.priceRoomClassifications.add(priceRoomClassification);
	}

}
