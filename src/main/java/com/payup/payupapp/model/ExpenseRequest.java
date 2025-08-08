package com.payup.payupapp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExpenseRequest {
    String groupName;
    Long amount;
    String description;
    String createdBy;
    List<UserExpense> users;
}
