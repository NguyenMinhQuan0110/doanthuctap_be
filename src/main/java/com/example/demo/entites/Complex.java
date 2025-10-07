package com.example.demo.entites;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entites.enums.ComplexStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Complex")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Complex {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complex_id")
    private Integer complexId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "latitude", precision = 10, scale = 6)
    private BigDecimal  latitude;

    @Column(name = "longitude", precision = 10, scale = 6)
    private BigDecimal  longitude;

    @Column(name = "phone", length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ComplexStatus status = ComplexStatus.active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @OneToMany(mappedBy = "complex", cascade = CascadeType.ALL)
    private List<Pitch> pitches = new ArrayList<>();
}
