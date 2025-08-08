package com.payup.payupapp.model;

import com.payup.payupapp.entity.Expense;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class GroupExpensesResponse {
    Expense expense;
    LinkedList<UserExpense> userExpenses;
}
