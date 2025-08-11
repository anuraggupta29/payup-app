package com.payup.payupapp.controller;


import com.payup.payupapp.entity.*;
import com.payup.payupapp.model.*;
import com.payup.payupapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class ExpenseApiController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;

    @PostMapping("/createUser")
    public ResponseEntity<String> handleCreateUser(@RequestBody UserRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUserName());
        user.setPasswordHash(request.getPassword());

        userRepository.save(user);

        return ResponseEntity.ok(user.toString());
    }

    @PostMapping("/createGroup")
    public ResponseEntity<String> handleCreateGroup(@RequestBody GroupRequest request){
        String groupName=request.getGroupName();
        String createdBy=request.getCreatedBy();
        Long creatorId=userRepository.findByUsername(createdBy).get().getId();

        Group group=new Group();
        group.setCreatedBy(creatorId);
        group.setName(groupName);
        groupRepository.save(group);

        Long groupId=groupRepository.findByName(groupName).getId();

        for(String user:request.getMembers()){
            Long id=userRepository.findByUsername(user).get().getId();
            GroupMember member=new GroupMember();
            member.setUserId(id);
            member.setGroupId(groupId);
            groupMemberRepository.save(member);
        }

        return ResponseEntity.ok(group.toString());
    }

    @Transactional
    @PostMapping("/addExpense")
    public ResponseEntity<String> handleAddExpense(@RequestBody ExpenseRequest request) {

        String description = request.getDescription();

        if(expenseRepository.findByDescription(description)!=null){
            Long expenseId= expenseRepository.findByDescription(description).getId();
            expenseSplitRepository.deleteByExpenseId(expenseId);
            expenseRepository.deleteById(expenseId);
        }

        String createdBy = request.getCreatedBy();
        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(request.getAmount());
        expense.setCreatedBy(createdBy);

        String groupName = request.getGroupName();
        Long groupId = -1L;
        if(!groupName.equals("non_group")){
            groupId = groupRepository.findByName(groupName).getId();
        }
        expense.setGroupId(groupId);
        expenseRepository.save(expense);

        Long expenseId = expenseRepository.findByDescription(request.getDescription()).getId();

        for (UserExpense userExpense : request.getUsers()) {
            Long userId = userRepository.findByUsername(userExpense.getUserName()).get().getId();
            ExpenseSplit expenseSplit = new ExpenseSplit();
            expenseSplit.setExpenseId(expenseId);
            expenseSplit.setUserId(userId);
            expenseSplit.setOwed(userExpense.getOwed());
            expenseSplit.setPaid(userExpense.getPaid());
            expenseSplitRepository.save(expenseSplit);
        }
        return ResponseEntity.ok(expense.toString());
    }


    @GetMapping("/groupExpenses/{groupName}")
    public ResponseEntity<List<GroupExpensesResponse>> handleGetGroupExpenses(@PathVariable String groupName){
        Long groupId=-1L;

        if(!groupName.equals("non_group")){
            groupId=groupRepository.findByName(groupName).getId();
        }

        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        LinkedList<GroupExpensesResponse> responses=new LinkedList<>();

        for(Expense expense:expenses){
            Long expenseId= expense.getId();
            responses.add(new GroupExpensesResponse());
            responses.getLast().setExpense(expense);
            responses.getLast().setUserExpenses(new LinkedList<>());
            List<ExpenseSplit> expenseSplits=expenseSplitRepository.findByExpenseId(expenseId);
            for(ExpenseSplit expenseSplit:expenseSplits) {
                responses.getLast().getUserExpenses().add(new UserExpense());

                String username = userRepository.findById(expenseSplit.getUserId()).get().getUsername();
                responses.getLast().getUserExpenses().getLast().setUserName(username);

                responses.getLast().getUserExpenses().getLast().setPaid(expenseSplit.getPaid());
                responses.getLast().getUserExpenses().getLast().setOwed(expenseSplit.getOwed());
            }
        }

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/getExpenses/{userName}")
    public ResponseEntity<String> handleGetExpensesPerUser(@PathVariable String userName){
        Map<String,Long> relations=new HashMap<>();
        Long userId=userRepository.findByUsername(userName).get().getId();
        List<ExpenseSplit> expenseSplits=expenseSplitRepository.findByUserId(userId);

        for(ExpenseSplit expenseSplit:expenseSplits){
            String paidBy = expenseRepository.findById(expenseSplit.getExpenseId()).get().getCreatedBy();
            if(userName.equals(paidBy)){
                List<ExpenseSplit> splits = expenseSplitRepository.findByExpenseId(expenseSplit.getExpenseId());
                for(ExpenseSplit split:splits){
                    if(split.getUserId()==userId)
                        continue;

                    Long amount = split.getOwed();
                    String name=userRepository.findById(split.getUserId()).get().getUsername();
                    relations.put(name,relations.getOrDefault(name,0L)+amount);
                }
                continue;
            }

            Long amount = expenseSplit.getPaid()-expenseSplit.getOwed();

            relations.put(paidBy,relations.getOrDefault(paidBy,0L)+amount);
        }

        return ResponseEntity.ok(relations.toString());
    }
}
