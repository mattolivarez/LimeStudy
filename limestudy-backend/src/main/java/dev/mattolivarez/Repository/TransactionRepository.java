package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.Transaction;

import java.util.List;

public interface TransactionRepository
{
    List<Transaction> findAll(Integer userId, Integer categoryId);

    Transaction findById(Integer userId, Integer categoryId, Integer transactionId) throws ResourceNotFoundException;

    Integer create(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate) throws BadRequestException;

    void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws BadRequestException;

    void removeById(Integer userId, Integer categoryId, Integer transactionId) throws ResourceNotFoundException;
}
