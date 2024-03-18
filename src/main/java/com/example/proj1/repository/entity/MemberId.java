package com.example.proj1.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Embeddable
public class MemberId implements Serializable {
    @Serial
    private static final long serialVersionUID = -1392272937088659107L;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "crew_id", nullable = false)
    private Long crewId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MemberId entity = (MemberId) o;
        return Objects.equals(this.crewId, entity.crewId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crewId, userId);
    }

}