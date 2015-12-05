package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Chanaka on 05/12/2015.
 */
public class ADBC extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static String DATABASE_NAME = "130324B.db";

    // Table names
    private static String TBL_ACCOUNT = "account";
    private static String TBL_TRANSACTION = "transactions";

    // Table columns name
    private static final String COL_ACCOUNT_NO = "account_no";
    private static final String COL_BANK_NAME = "bank_name";
    private static final String COL_HOLDER_NAME = "holder_name";
    private static final String COL_BALANCE = "balance";
    private static final String COL_DATE = "date";
    private static final String COL_EXPENSE_TYPE = "expense_type";
    private static final String COL_AMOUNT = "amount";

    // Table creating queries
    private static final String CREATE_TABLE_ACCOUTN = "CREATE TABLE " + TBL_ACCOUNT + "(" + COL_ACCOUNT_NO + " TEXT NOT NULL, " + COL_BANK_NAME + " TEXT NOT NULL, " + COL_HOLDER_NAME + " TEXT NOT NULL, " + COL_BALANCE + " REAL NOT NULL)";
    private static final String CREATE_TBL_TRANSACTION = "CREATE TABLE " + TBL_TRANSACTION + "(" + COL_DATE + " TEXT NOT NULL, " + COL_ACCOUNT_NO + " TEXT NOT NULL, " + COL_EXPENSE_TYPE + " TEXT NOT NULL, " + COL_AMOUNT + " REAL NOT NULL)";

    public ADBC(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUTN);
        db.execSQL(CREATE_TBL_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ACCOUTN);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TBL_TRANSACTION);
    }

    // ------------------------- TBL_ACCOUNT --------------------------------------------------------
    public void addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ACCOUNT_NO, account.getAccountNo());
        values.put(COL_BANK_NAME, account.getBankName());
        values.put(COL_HOLDER_NAME, account.getAccountHolderName());
        values.put(COL_BALANCE, account.getBalance());

        db.insert(TBL_ACCOUNT, null, values);
        db.close();
    }

    public void removeAccount(String accountNo){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_ACCOUNT, COL_ACCOUNT_NO + "='" + accountNo + "'", null);
        db.close();
    }

    public void updateBalance(String accountNo, double newAmount){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_BALANCE, newAmount);

        db.update(TBL_ACCOUNT, values, COL_ACCOUNT_NO + "='" + accountNo + "'", null);
        db.close();
    }

    public Account getAccount(String accountNo){
        Account account=null;
        String selectQuery="SELECT * FROM " + TBL_ACCOUNT + " WHERE " + COL_ACCOUNT_NO + "='" + accountNo + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return account;
    }

    public List<String> getAccountNumbers(){
        List<String> accountNoList = new ArrayList<String>();
        String selectQuery="SELECT " + COL_ACCOUNT_NO + " FROM " + TBL_ACCOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                accountNoList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return accountNoList;
    }

    public List<Account> getAllAccounts(){
        List<Account> accountList = new ArrayList<Account>();
        String selectQuery="SELECT * FROM " + TBL_ACCOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
                accountList.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return accountList;
    }

    // ------------------------- TBL_TRANSACTION --------------------------------------------------------
    public void addTransaction(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(transaction.getDate());

        ContentValues values = new ContentValues();
        values.put(COL_DATE, date);
        values.put(COL_ACCOUNT_NO, transaction.getAccountNo());
        values.put(COL_EXPENSE_TYPE, transaction.getExpenseType().toString());
        values.put(COL_AMOUNT, transaction.getAmount());

        db.insert(TBL_TRANSACTION, null, values);
        db.close();
    }

    public List<Transaction> getAllTransactionLogs(){
        List<Transaction> transactionList = new ArrayList<Transaction>();
        String selectQuery="SELECT * FROM " + TBL_TRANSACTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                Date date = null;
                String dateString = cursor.getString(0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = sdf.parse(dateString);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    Log.e("Database Handler", "Error converting String to Date. " + e.toString());
                }

                Transaction transaction = new Transaction(date, cursor.getString(1), ExpenseType.valueOf(cursor.getString(2)), cursor.getDouble(3));
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return transactionList;
    }

    public List<Transaction> getLastNLogs(int limit){
        List<Transaction> transactionList = new ArrayList<Transaction>();
        String selectQuery="SELECT * FROM " + TBL_TRANSACTION + " ORDER BY " + COL_DATE + " ASC LIMIT " + limit;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                Date date = null;
                String dateString = cursor.getString(0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = sdf.parse(dateString);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    Log.e("Database Handler", "Error converting String to Date. " + e.toString());
                }

                Transaction transaction = new Transaction(date, cursor.getString(1), ExpenseType.valueOf(cursor.getString(2)), cursor.getDouble(3));
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return transactionList;
    }
}