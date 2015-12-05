package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.ApplicationContext;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Chanaka on 05/12/2015.
 */
public class PersistantTransactionDAO implements TransactionDAO {

    private final ADBC adbc = new ADBC(ApplicationContext.getContext());

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        adbc.addTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return adbc.getAllTransactionLogs();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return adbc.getLastNLogs(limit);
    }
}
