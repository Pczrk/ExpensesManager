package com.example.proj1.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "crews")
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_id", nullable = false)
    private Long crewId;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leader", nullable = false)
    private User leader;

    @Column(name = "access_key", nullable = false, length = 8)
    private String accessKey;

    @OneToMany(mappedBy = "crew")
    private Set<Member> members = new LinkedHashSet<>();

}