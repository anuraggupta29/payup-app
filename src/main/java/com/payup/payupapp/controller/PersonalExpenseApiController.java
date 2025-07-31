package com.payup.payupapp.controller;// FormController.java
import com.payup.payupapp.model.PersonalExpense;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.sql.SQLOutput;

@Controller // change to RestController and get data as JSON instead of form
public class PersonalExpenseApiController {

    @PostMapping("/send-personal-expense")
    public ResponseEntity<String> handlePersonalExpense(@ModelAttribute PersonalExpense personalExpense) {
        // Process the form data
        System.out.println(personalExpense);
        return ResponseEntity.ok(personalExpense.toString());
    }
}