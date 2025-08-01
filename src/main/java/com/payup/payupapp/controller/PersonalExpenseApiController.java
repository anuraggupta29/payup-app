package com.payup.payupapp.controller;// FormController.java
import com.payup.payupapp.entity.PersonalExpense;
import com.payup.payupapp.entity.User;
import com.payup.payupapp.model.PersonalExpenseDto;
import com.payup.payupapp.repository.PersonalExpenseRepository;
import com.payup.payupapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@Controller // change to RestController and get data as JSON instead of form
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
}