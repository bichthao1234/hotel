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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "KIEU_PHONG")
public class RoomType {

	@Id
	@Column(name = "ID_KP")
	@GeneratedValue(generator = "rt-generator")
	@GenericGenerator(name = "rt-generator",
			parameters = @Parameter(name = "prefix", value = "RT"),
			strategy = "com.my.hotel.entity.generator.PrefixIdGenerator")
	private String id;

	@Column(name = "TEN_KP", unique = true)
	private String name;

	@OneToMany(mappedBy = "roomType")
	List<RoomClassification> roomClassifications;

}
