package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.constants;

import android.provider.BaseColumns;

/**
 * Created by prabath s on 12/5/2015.
 */
public class AccountConst {
    public static abstract class AccountEntry implements BaseColumns {
        public static final String TABLE_NAME = "account";

        public static final String COLUMN_NAME_ACCOUNT_NO = "account_number";
        public static final String COLUMN_NAME_BANK_NAME = "bank_name";
        public static final String COLUMN_NAME_ACCOUNT_HOLDER_NAME = "account_holder_name";
        public static final String COLUMN_NAME_BALANCE = "balance";
    }
}
