package com.my.hotel.entity;

import com.my.hotel.utils.Utilities;
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
@Table(name = "KHUYEN_MAI")
public class Promotion {

	@Id
	@Column(name = "ID_KM")
	@GeneratedValue(generator = "pr-generator")
	@GenericGenerator(name = "pr-generator",
			parameters = @Parameter(name = "prefix", value = "PRM"),
			strategy = "com.my.hotel.entity.generator.PrefixIdGenerator")
	private String id;

	@Column(name = "MO_TA_KM")
	private String description;

	@Column(name = "NGAY_BAT_DAU")
	private Date startDate;

	@Column(name = "NGAY_KET_THUC")
	private Date endDate;

	@OneToMany(mappedBy = "promotion")
	private List<PromotionDetail> promotionDetails;

	public boolean canDelete() {
		return Utilities.isEmptyList(this.promotionDetails);
	}

}
