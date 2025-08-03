package com.payup.payupapp.repository;

import com.payup.payupapp.entity.PersonalExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalExpenseRepository extends JpaRepository<PersonalExpense, Long> {
    List<PersonalExpense> findByUserId(Long userId);
}