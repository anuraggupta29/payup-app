package com.payup.payupapp.repository;

import com.payup.payupapp.entity.PersonalExpense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalExpenseRepository extends JpaRepository<PersonalExpense, Long> {

}