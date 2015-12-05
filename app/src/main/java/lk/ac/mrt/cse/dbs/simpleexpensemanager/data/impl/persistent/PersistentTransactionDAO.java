package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.constants.TransactionConst;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by prabath s on 12/4/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO {

    private Context context;

    public PersistentTransactionDAO(Context context)
    {
        this.context = context;
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteHelper helper = SQLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TransactionConst.TransactionEntry.COLUMN_NAME_ACCOUNT_NO,accountNo);
        values.put(TransactionConst.TransactionEntry.COLUMN_NAME_DATE,SQLiteHelper.dateToString(date,context));
        values.put(TransactionConst.TransactionEntry.COLUMN_NAME_AMOUNT,amount);
        values.put(TransactionConst.TransactionEntry.COLUMN_NAME_EXPENSE_TYPE,expenseType.toString());

        db.insert(TransactionConst.TransactionEntry.TABLE_NAME,null,values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return getPaginatedTransactionLogs(0);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteHelper helper = SQLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        String strLim;
        if(limit==0){strLim=null;}
        else{strLim=String.valueOf(limit);}

        String[] projection = {
                TransactionConst.TransactionEntry.COLUMN_NAME_ACCOUNT_NO,
                TransactionConst.TransactionEntry.COLUMN_NAME_DATE,
                TransactionConst.TransactionEntry.COLUMN_NAME_EXPENSE_TYPE,
                TransactionConst.TransactionEntry.COLUMN_NAME_AMOUNT
        };
        String sortOrder = TransactionConst.TransactionEntry.COLUMN_NAME_ENTRY_INDEX;
        Cursor c = db.query(TransactionConst.TransactionEntry.TABLE_NAME, projection,null,null,null,null,sortOrder,strLim);

        ArrayList<Transaction> results = new ArrayList<>();
        while (c.moveToNext())
        {
            ExpenseType expenseType = ExpenseType.EXPENSE;
            if(ExpenseType.INCOME.toString().equals( c.getString(c.getColumnIndex(TransactionConst.TransactionEntry.COLUMN_NAME_EXPENSE_TYPE))))
            {
                expenseType =ExpenseType.INCOME;
            }
            try {
                String dateString =c.getString(c.getColumnIndex(TransactionConst.TransactionEntry.COLUMN_NAME_DATE));
                Date date = SQLiteHelper.dateFromString(dateString);
                Transaction tr = new Transaction(date,
                        c.getString(c.getColumnIndex(TransactionConst.TransactionEntry.COLUMN_NAME_ACCOUNT_NO)),
                        expenseType,
                        c.getFloat(c.getColumnIndex(TransactionConst.TransactionEntry.COLUMN_NAME_AMOUNT)));
                results.add(tr);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        return results;
    }
}
