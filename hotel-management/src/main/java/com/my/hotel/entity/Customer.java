package com.my.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
@Table(name = "KHACH_HANG")
public class Customer {

    @Id
    @Column(name = "CMND")
    private String id;

    @Column(name = "HO")
    private String firstName;

    @Column(name = "TEN")
    private String lastName;

    @Column(name = "DIA_CHI")
    private String address;

    @Column(name = "SDT")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "MA_SO_THUE")
    private String taxNumber;

}
