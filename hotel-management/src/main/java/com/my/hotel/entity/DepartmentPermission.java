package com.my.hotel.entity;

import com.my.hotel.entity.key.DepartmentPermissionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PHAN_QUYEN")
@IdClass(DepartmentPermissionId.class)
public class DepartmentPermission implements Serializable {

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_NQ")
	private Permission permission;

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_BP")
	private Department department;

}
