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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PHU_THU")
public class Surcharge {

	@Id
	@GeneratedValue(generator = "sc-generator")
	@GenericGenerator(name = "sc-generator",
			parameters = @Parameter(name = "prefix", value = "SC"),
			strategy = "com.my.hotel.entity.generator.PrefixIdGenerator")
	@Column(name = "ID_PHU_THU")
	private String id;

	@Column(name = "TEN_PHU_THU")
	private String name;

	@Column(name = "LY_DO")
	private String description;

	@OneToMany(mappedBy = "surcharge")
	private List<SurchargeDetail> surchargeDetails;

	@OneToMany(mappedBy = "surcharge")
	private List<PriceSurcharge> priceSurcharges;

	public void addSurchargeDetails(SurchargeDetail surchargeDetail) {
		if (this.surchargeDetails == null) {
			this.surchargeDetails = new ArrayList<>();
		}
		surchargeDetail.setSurcharge(this);
		this.surchargeDetails.add(surchargeDetail);
	}

	public boolean canDelete() {
		return Utilities.isEmptyList(this.surchargeDetails);
	}

}
