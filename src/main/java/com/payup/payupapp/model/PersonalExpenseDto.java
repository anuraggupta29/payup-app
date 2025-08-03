package com.payup.payupapp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PersonalExpenseDto {
    private String username;
    private String expenseDescription;
    private Double expenseAmount;
    private LocalDate expenseDate;

    @Override
    public String toString() {
        return "{" +
                "username=" + username +
                ", expenseDescription='" + expenseDescription + '\'' +
                ", expenseAmount=" + expenseAmount +
                ", expenseDate=" + expenseDate +
                '}';
    }
}
