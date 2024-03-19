package com.example.proj1.service;

import com.example.proj1.exceptions.NullArgumentException;
import com.example.proj1.model.ExpenseDto;
import com.example.proj1.repository.ExpenseRepository;
import com.example.proj1.repository.entity.Expense;
import com.example.proj1.repository.entity.Trip;
import com.example.proj1.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final MemberService memberService;
    private final TripService tripService;
    private final UserService userService;
    private final PaymentService paymentService;

    public Long createExpense(ExpenseDto expenseDto, Long userId){
        if (expenseDto == null || expenseDto.getName() == null || expenseDto.getTripId() == null ||
                expenseDto.getValue() == null) // description and payedBy can be null
            throw new NullArgumentException();

        Trip trip = tripService.getTripEntity(expenseDto.getTripId());

        if (expenseDto.getPayedBy() != null) {
            User user = userService.getUserEntity(expenseDto.getPayedBy());
            //TODO CHECK IF USER IS IN THE CREW
            paymentService.createPaymentEntity(trip,user,expenseDto.getValue());
        }

        return expenseRepository.save(Expense.builder()
                        .name(expenseDto.getName())
                        .trip(trip)
                        .value(expenseDto.getValue())
                        .description(expenseDto.getDescription())
                        .build())
                .getExpenseId();
    }

    public List<ExpenseDto> getAllExpenses(Long tripId, Long userId){
        Trip trip = tripService.getTripEntity(tripId);

        memberService.assertUserIsMember(userId, trip.getCrew().getCrewId());

        return trip.getExpenses().stream().map(expense -> ExpenseDto.builder()
                .expenseId(expense.getExpenseId())
                .tripId(tripId)
                .value(expense.getValue())
                .name(expense.getName())
                .description(expense.getDescription())
                .build()).toList();
    }
}
