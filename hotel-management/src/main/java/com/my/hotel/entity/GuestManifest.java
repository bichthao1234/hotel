package com.my.hotel.entity;

import com.my.hotel.entity.key.GuestManifestId;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CT_KHACH_O")
@IdClass(GuestManifestId.class)
public class GuestManifest {


    @Id
    @ManyToOne
    @JoinColumn(name = "ID_CT_PT")
    private RentalSlipDetail rentalSlipDetail;

    @Id
    @ManyToOne
    @JoinColumn(name = "CMND")
    private Customer customer;


}
