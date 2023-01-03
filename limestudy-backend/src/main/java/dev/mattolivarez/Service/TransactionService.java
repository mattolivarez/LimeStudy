package dev.mattolivarez.Service;



import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.Transaction;

import java.util.List;

public interface TransactionService
{
    List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId);

    Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId) throws ResourceNotFoundException;

    Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate) throws BadRequestException;

    void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws BadRequestException;

    void removeTransaction(Integer userId, Integer categoryId, Integer transactionId) throws ResourceNotFoundException;
}
