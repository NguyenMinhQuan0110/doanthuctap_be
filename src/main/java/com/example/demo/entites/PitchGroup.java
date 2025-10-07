package com.example.demo.entites;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.example.demo.entites.enums.PitchStatus;
import com.example.demo.entites.enums.PitchType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PitchGroup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PitchGroup {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private PitchType type;

    @Column(name = "price_per_hour", nullable = false)
    private BigDecimal pricePerHour;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private PitchStatus status = PitchStatus.active;

    @ManyToOne
    @JoinColumn(name = "complex_id")
    private Complex complex;

    @ManyToMany
    @JoinTable(
            name = "PitchGroupDetail",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "pitch_id")
    )
    private Set<Pitch> pitches = new HashSet<>();
}
