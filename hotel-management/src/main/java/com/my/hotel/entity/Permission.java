package com.my.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "NHOM_QUYEN")
public class Permission {

	@Id
	@Column(name = "ID_NQ")
	private String id;

	@Column(name = "TEN_NQ")
	private String name;

	@OneToMany(mappedBy = "permission")
	private List<DepartmentPermission> departmentPermissions;

}
