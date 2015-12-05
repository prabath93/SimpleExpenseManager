package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.constants;

import android.provider.BaseColumns;

/**
 * Created by prabath s on 12/4/2015.
 */
public class TransactionConst {
    public static abstract class TransactionEntry implements BaseColumns {
        public static final String TABLE_NAME = "transaction_history";

        public static final String COLUMN_NAME_ENTRY_INDEX = "record_number";
        public static final String COLUMN_NAME_ACCOUNT_NO = "account_number";
        public static final String COLUMN_NAME_EXPENSE_TYPE = "expense_type";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_AMOUNT = "amount";


    }
}
