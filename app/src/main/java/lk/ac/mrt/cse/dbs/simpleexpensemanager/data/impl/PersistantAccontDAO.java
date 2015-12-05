package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.List;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ApplicationContext;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Chanaka on 05/12/2015.
 */
public class PersistantAccontDAO implements AccountDAO{

    private final ADBC adbc = new ADBC(ApplicationContext.getContext());

    @Override
    public List<String> getAccountNumbersList() {
        return adbc.getAccountNumbers();
    }

    @Override
    public List<Account> getAccountsList() {
        return adbc.getAllAccounts();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = adbc.getAccount(accountNo);
        if(account!=null){
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        adbc.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        adbc.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = adbc.getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                adbc.updateBalance(accountNo, account.getBalance() - amount);
                break;
            case INCOME:
                adbc.updateBalance(accountNo, account.getBalance() + amount);
                break;
        }
    }
}
