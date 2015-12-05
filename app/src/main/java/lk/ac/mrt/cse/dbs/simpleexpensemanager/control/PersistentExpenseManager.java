package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantAccontDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;

/**
 * Created by Chanaka on 05/12/2015.
 */

public class PersistentExpenseManager extends ExpenseManager {

    public PersistentExpenseManager() {
        setup();
    }

    @Override
    public void setup() {
        TransactionDAO perTransactionDAO = new PersistantTransactionDAO();
        setTransactionsDAO(perTransactionDAO);

        AccountDAO perAccountDAO = new PersistantAccontDAO();
        setAccountsDAO(perAccountDAO);

    }
}
