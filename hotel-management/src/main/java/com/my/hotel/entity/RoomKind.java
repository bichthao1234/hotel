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
@Table(name = "LOAI_PHONG")
public class RoomKind {

	@Id
	@Column(name = "ID_LP")
	@GeneratedValue(generator = "rk-generator")
	@GenericGenerator(name = "rk-generator",
			parameters = @Parameter(name = "prefix", value = "RK"),
			strategy = "com.my.hotel.entity.generator.PrefixIdGenerator")
	private String id;

	@Column(name = "TEN_LP")
	private String name;

	@OneToMany(mappedBy = "roomKind")
	List<RoomClassification> roomClassifications;

}
