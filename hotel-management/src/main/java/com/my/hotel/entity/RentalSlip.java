package com.my.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "PHIEU_THUE")
public class RentalSlip {

    @Id
    @Column(name = "ID_PT")
    private Integer id;

    @Column(name = "NGAY_LAP")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "CMND")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "ID_NV")
    private Employee createdBy;

    @ManyToOne
    @JoinColumn(name = "ID_PD")
    private Reservation reservation;

    @OneToMany(mappedBy = "rentalSlip")
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "rentalSlip")
    private List<RentalSlipDetail> rentalSlipDetails;

}
