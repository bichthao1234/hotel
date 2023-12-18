package com.my.hotel.entity;

import com.my.hotel.entity.key.ManagementId;
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
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "QUAN_LY")
@IdClass(ManagementId.class)
public class Management implements Serializable {

	@Id
	@ManyToOne
	@JoinColumn(name = "ID_BP")
	private Department department;

	@Id
	@Column(name = "NGAY_BD_QL")
	private Date managementStartDate;

	@ManyToOne
	@JoinColumn(name = "ID_NV")
	private Employee manager;

}
