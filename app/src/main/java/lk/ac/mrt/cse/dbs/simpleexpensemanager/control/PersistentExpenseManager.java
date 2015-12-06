package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by prabath s on 12/5/2015.
 */
public class PersistentExpenseManager extends ExpenseManager{

    private Context context = null;
    public PersistentExpenseManager( Context context) {
        this.context = context;
        setup();

    }

    @Override
    public void setup() {
        //Setup DAO objects
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(context);
        setTransactionsDAO(persistentTransactionDAO);


        AccountDAO persistentAccountDAO = new PersistentAccountDAO(context);
        setAccountsDAO(persistentAccountDAO);

        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        List<String> account_no=persistentAccountDAO.getAccountNumbersList();
        //this condition is checked to avoid duplicating dummy accounts
        //if dummy accounts are not added in here there is no need to check this condition
        //this might cause to degrade the performance of application so if dummy accounts are not needed they should not be added in here and the condition should not be checked
        if(!account_no.contains(dummyAcct1.getAccountNo())) {
            getAccountsDAO().addAccount(dummyAcct1);
            getAccountsDAO().addAccount(dummyAcct2);
        }
    }

}
