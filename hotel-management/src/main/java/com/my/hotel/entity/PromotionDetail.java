package com.my.hotel.entity;

import com.my.hotel.entity.key.PromotionDetailId;
import com.my.hotel.entity.key.ServiceDetailId;
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
@Table(name = "CT_KHUYEN_MAI")
@IdClass(PromotionDetailId.class)
public class PromotionDetail {

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_KM")
	private Promotion promotion;

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_HANG_PHONG")
	private RoomClassification roomClassification;

	@Column(name = "PHAN_TRAM_GIAM")
	private Float percent;

}
