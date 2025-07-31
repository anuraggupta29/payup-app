package com.payup.payupapp.controller;// FormController.java
import com.payup.payupapp.model.PersonalExpense;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@Controller
public class PersonalExpensePageController {

    @GetMapping("/pexp")
    public String serveAddExpensePage() {
        return "forward:/add-personal-expense.html";  // serves static HTML
    }
}