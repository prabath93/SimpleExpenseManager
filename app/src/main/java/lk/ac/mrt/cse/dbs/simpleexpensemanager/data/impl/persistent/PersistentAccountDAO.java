package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.constants.AccountConst;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by prabath s on 12/4/2015.
 */
public class PersistentAccountDAO implements AccountDAO{

    private Context context;
    public PersistentAccountDAO(Context context)
    {
        this.context = context;
    }

    @Override
    public List<String> getAccountNumbersList() {
        //Get the singleton instance
        SQLiteHelper helper = SQLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {
                AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO
        };
        String orderBy = AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO;
        //Here query is used instead of rawquery to improve the efficiency
        Cursor c = db.query(AccountConst.AccountEntry.TABLE_NAME, columns,null,null,null,null,orderBy,null);

        ArrayList<String> results = new ArrayList<>();
        while (c.moveToNext())
        {
            results.add(c.getString(c.getColumnIndex(AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO)));
        }
        return results;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteHelper helper = SQLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {
                AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO,
                AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME,
                AccountConst.AccountEntry.COLUMN_NAME_BANK_NAME,
                AccountConst.AccountEntry.COLUMN_NAME_BALANCE
        };
        String orderBy = AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO;
        Cursor c = db.query(AccountConst.AccountEntry.TABLE_NAME, columns,null,null,null,null,orderBy,null);

        ArrayList<Account> results = new ArrayList<>();
        while (c.moveToNext())
        {
            Account ac = new Account(c.getString(c.getColumnIndex(AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO)),
                    c.getString(c.getColumnIndex(AccountConst.AccountEntry.COLUMN_NAME_BANK_NAME)),
                    c.getString(c.getColumnIndex(AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME)),
                    c.getFloat(c.getColumnIndex(AccountConst.AccountEntry.COLUMN_NAME_BALANCE)));

            results.add(ac);
        }
        return results;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteHelper helper = SQLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {
                AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO,
                AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME,
                AccountConst.AccountEntry.COLUMN_NAME_BANK_NAME,
                AccountConst.AccountEntry.COLUMN_NAME_BALANCE
        };
        String orderBy = AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO + " DESC";
        Cursor c = db.query(AccountConst.AccountEntry.TABLE_NAME, columns,AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO + "='" + accountNo +"'",null,null,null,orderBy,null);

        if(c.moveToNext()) {
            Account ac = new Account(c.getString(c.getColumnIndex(AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO)),
                    c.getString(c.getColumnIndex(AccountConst.AccountEntry.COLUMN_NAME_BANK_NAME)),
                    c.getString(c.getColumnIndex(AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME)),
                    c.getFloat(c.getColumnIndex(AccountConst.AccountEntry.COLUMN_NAME_BALANCE)));

            return  ac;
        }
        else {
            throw new InvalidAccountException("Account number invalid");
        }
    }

    @Override
    public void addAccount(Account account) {

        SQLiteHelper helper = SQLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO,account.getAccountNo());
        values.put(AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        values.put(AccountConst.AccountEntry.COLUMN_NAME_BANK_NAME,account.getBankName());
        values.put(AccountConst.AccountEntry.COLUMN_NAME_BALANCE,account.getBalance());

        db.insert(AccountConst.AccountEntry.TABLE_NAME,null,values);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        SQLiteHelper helper = SQLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO + "= ?'";
        String[] selectionArgs = { accountNo };
        db.delete(AccountConst.AccountEntry.TABLE_NAME,selection,selectionArgs);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account ac = getAccount(accountNo);

        SQLiteHelper helper = SQLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if(expenseType== ExpenseType.EXPENSE){values.put(AccountConst.AccountEntry.COLUMN_NAME_BALANCE,ac.getBalance() - amount);}
        if(expenseType== ExpenseType.INCOME){values.put(AccountConst.AccountEntry.COLUMN_NAME_BALANCE,ac.getBalance() + amount);}

        String selection = AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO + " = ?";
        String[] selectionArgs = { accountNo };

        db.update(AccountConst.AccountEntry.TABLE_NAME,values, selection,selectionArgs);


    }
}
