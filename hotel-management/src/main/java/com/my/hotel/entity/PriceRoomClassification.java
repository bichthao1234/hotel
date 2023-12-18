package com.my.hotel.entity;

import com.my.hotel.entity.key.PriceRoomClassificationId;
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
@Table(name = "GIA_HANG_PHONG")
@IdClass(PriceRoomClassificationId.class)
public class PriceRoomClassification {

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_HANG_PHONG")
	private RoomClassification roomClassification;

	@Id
	@Column(name = "NGAYAPDUNG")
	private Date appliedDate;

	@Column(name = "NGAY_THIET_LAP")
	private Date createdDate;

	@Column(name = "GIA")
	private Double price;

	@ManyToOne
	@JoinColumn(name = "ID_NV")
	private Employee createdBy;

}
