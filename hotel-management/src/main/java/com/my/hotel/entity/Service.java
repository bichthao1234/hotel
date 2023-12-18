package com.my.hotel.entity;

import com.my.hotel.utils.Utilities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@ToString
@Builder
@Entity
@Table(name = "DICH_VU")
public class Service {

	@Id
	@GeneratedValue(generator = "sv-generator")
	@GenericGenerator(name = "sv-generator",
			parameters = @Parameter(name = "prefix", value = "SV"),
			strategy = "com.my.hotel.entity.generator.PrefixIdGenerator")
	@Column(name = "ID_DV")
	private String id;

	@Column(name = "TEN_DV")
	private String name;

	@Column(name = "MO_TA")
	private String description;
	
	@Column(name = "DON_VI_TINH")
	private String unit;

	@OneToMany(mappedBy = "service")
	private List<ServiceDetail> serviceDetails;

	@OneToMany(mappedBy = "service")
	private List<PriceService> priceServices;

	public void addServiceDetails(ServiceDetail serviceDetail) {
		if (this.serviceDetails == null) {
			this.serviceDetails = new ArrayList<>();
		}
		serviceDetail.setService(this);
		this.serviceDetails.add(serviceDetail);
	}

	public boolean canDelete() {
		return Utilities.isEmptyList(this.serviceDetails);
	}

}
