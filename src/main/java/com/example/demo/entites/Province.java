package com.example.demo.entites;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Province")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Province {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "province_name", nullable = false, unique = true, length = 100)
    private String provinceName;

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    private List<District> districts = new ArrayList<>();
}
