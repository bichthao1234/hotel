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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "NHAN_VIEN")
public class Employee {

    @Id
    @Column(name = "ID_NV")
    @GeneratedValue(generator = "emp-generator")
    @GenericGenerator(name = "emp-generator",
            parameters = @Parameter(name = "prefix", value = "EMPL"),
            strategy = "com.my.hotel.entity.generator.PrefixIdGenerator")
    private String id;

    @Column(name = "HO")
    private String firstName;

    @Column(name = "TEN")
    private String lastName;

    @Column(name = "PHAI")
    private String gender;

    @Column(name = "NGAY_SINH")
    private Date birthday;

    @Column(name = "DIA_CHI")
    private String address;

    @Column(name = "SDT")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "HINH")
    private String image;

    @ManyToOne
    @JoinColumn(name = "ID_BP")
    private Department department;

    @OneToMany(mappedBy = "manager")
    private List<Management> managements;

    @OneToMany(mappedBy = "createdBy")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "createdBy")
    private List<PriceRoomClassification> priceRoomClassifications;

    public void addPriceRoomClassifications(PriceRoomClassification priceRoomClassification) {
        if (this.priceRoomClassifications == null) {
            this.priceRoomClassifications = new ArrayList<>();
        }
        priceRoomClassification.setCreatedBy(this);
        this.priceRoomClassifications.add(priceRoomClassification);
    }

}
