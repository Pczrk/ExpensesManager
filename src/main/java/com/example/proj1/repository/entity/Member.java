package com.example.proj1.repository.entity;

import com.example.proj1.repository.entity.id.MemberId;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(MemberId.class)
@Table(name = "members")
public class Member {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "crew_id", nullable = false)
    private Long crewId;

    @Column(name = "nickname")
    private String nickname;
}
