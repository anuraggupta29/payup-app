package com.payup.payupapp.entity;

import com.payup.payupapp.model.PersonalExpenseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "personal_expenses")
@Getter @Setter
public class PersonalExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Default constructor
    public PersonalExpense() {}

    // ✅ Constructor that accepts DTO and User
    public PersonalExpense(PersonalExpenseDto dto, User user) {
        this.user = user;
        this.description = dto.getExpenseDescription();
        this.amount = BigDecimal.valueOf(dto.getExpenseAmount());
        this.expenseDate = dto.getExpenseDate();
    }

    @Override
    public String toString() {
        return "PersonalExpense{" +
                "id=" + id +
                ", user=" + user +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", expenseDate=" + expenseDate +
                ", createdAt=" + createdAt +
                '}';
    }

}