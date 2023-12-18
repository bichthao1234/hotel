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
@Table(name = "TIEN_NGHI")
public class Convenience {

	@Id
	@GeneratedValue(generator = "cnv-generator")
	@GenericGenerator(name = "cnv-generator",
			parameters = @Parameter(name = "prefix", value = "CNV"),
			strategy = "com.my.hotel.entity.generator.PrefixIdGenerator")
	@Column(name = "ID_TN")
	private String id;

	@Column(name = "TEN_TN")
	private String name;

	@Column(name = "ICON")
	private String icon;

	@OneToMany(mappedBy = "convenience")
	private List<ConvenienceDetail> convenienceDetails;

}
