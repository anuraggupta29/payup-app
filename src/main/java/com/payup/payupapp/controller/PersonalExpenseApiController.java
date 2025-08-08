package com.payup.payupapp.controller;// FormController.java
import com.payup.payupapp.entity.*;
import com.payup.payupapp.model.*;
import com.payup.payupapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.*;

@RestController // change to RestController and get data as JSON instead of form
public class PersonalExpenseApiController {

    @Autowired
    private PersonalExpenseRepository personalExpenseRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/send-personal-expense")
    public ResponseEntity<String> handlePersonalExpense(@ModelAttribute PersonalExpenseDto personalExpenseDto) {
        // Process the form data
        System.out.println(personalExpenseDto);

        if(userRepository.findByUsername(personalExpenseDto.getUsername()).isPresent()){
            User user = userRepository.findByUsername(personalExpenseDto.getUsername()).get();

            PersonalExpense personalExpense = new PersonalExpense(personalExpenseDto, user);

            PersonalExpense saved = personalExpenseRepository.save(personalExpense);
            System.out.println("Data Saved");
            return ResponseEntity.ok(saved.toString());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/users/{userName}")
    public ResponseEntity<String> handleGetExpenseByName(@PathVariable String userName){
        if(!userRepository.findByUsername(userName).isPresent())
            return ResponseEntity.badRequest().build();

        Long id=userRepository.findByUsername(userName).get().getId();
        List<PersonalExpense> expenseList=personalExpenseRepository.findByUserId(id);
        return ResponseEntity.ok(expenseList.toString());
    }
}