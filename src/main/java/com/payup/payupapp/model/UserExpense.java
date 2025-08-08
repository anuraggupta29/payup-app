package com.payup.payupapp.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExpense {
    String userName;
    Long paid;
    Long owed;
}
