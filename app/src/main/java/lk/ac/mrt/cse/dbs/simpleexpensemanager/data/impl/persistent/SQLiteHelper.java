package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.constants.AccountConst;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.constants.TransactionConst;

/**
 * Created by prabath s on 12/4/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "130534T.db";

    private static SQLiteHelper instance = null;

    //This is the query for creating the two tables
    private  static final String[] SQL_CREATE_ENTRIES =
            {"CREATE TABLE IF NOT EXISTS " + AccountConst.AccountEntry.TABLE_NAME +
                    " (" + AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_NO + " VARCHAR(20) PRIMARY KEY UNIQUE, " +
                    AccountConst.AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME  + " VARCHAR(50) NOT NULL, " +
                    AccountConst.AccountEntry.COLUMN_NAME_BANK_NAME + " VARCHAR (50) NOT NULL, "+
                    AccountConst.AccountEntry.COLUMN_NAME_BALANCE + " DECIMAL(10,2) NOT NULL )" ,
                    "CREATE TABLE IF NOT EXISTS " + TransactionConst.TransactionEntry.TABLE_NAME +
                            " (" + TransactionConst.TransactionEntry.COLUMN_NAME_ENTRY_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                            TransactionConst.TransactionEntry.COLUMN_NAME_ACCOUNT_NO +" VARCHAR(20) NOT NULL, " +
                            TransactionConst.TransactionEntry.COLUMN_NAME_DATE+ " DATE NOT NULL, "+
                            TransactionConst.TransactionEntry.COLUMN_NAME_AMOUNT+ " DECIMAL(10,2) NOT NULL, "+
                            TransactionConst.TransactionEntry.COLUMN_NAME_EXPENSE_TYPE + " VARCHAR(20) NOT NULL )"
            };
    //this is the query to drop the two tables
    private static final String[] SQL_DELETE_ENTRIES = {"DROP TABLE IF EXISTS " + AccountConst.AccountEntry.TABLE_NAME ,
            "DROP TABLE IF EXISTS " + TransactionConst.TransactionEntry.TABLE_NAME};




    public static String dateToString(Date date, Context context){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        return dateString;

    }

    public static Date dateFromString(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = dateFormat.parse(date);
        return strDate;
    }

    //This method is to make sure that instance of this class is made only once
    public static SQLiteHelper getInstance(Context context)
    {
        if(instance == null)
            instance = new SQLiteHelper(context);
        return instance;
    }

    private SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        for(String s : SQL_CREATE_ENTRIES)
        {
            db.execSQL(s);
            System.out.println(s);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(String s : SQL_DELETE_ENTRIES)
        {
            db.execSQL(s);
        }
        onCreate(db);
    }
}
