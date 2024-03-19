package com.example.proj1.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class RecipientId implements Serializable {
    private static final long serialVersionUID = -3143287293913362410L;
    @Column(name = "expense_id", nullable = false)
    private Long expenseId;

    @Column(name = "recipient", nullable = false)
    private Long recipient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RecipientId entity = (RecipientId) o;
        return Objects.equals(this.expenseId, entity.expenseId) &&
                Objects.equals(this.recipient, entity.recipient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expenseId, recipient);
    }

}