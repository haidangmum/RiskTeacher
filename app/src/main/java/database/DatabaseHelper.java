package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.riskteacher.teamcoin.riskteacher.RTOperation;
import com.riskteacher.teamcoin.riskteacher.RTUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "RiskTeacher";

    private static final String USER_TABLE_NAME = "RTUser";

    private static final String COLUMN_ID = "id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String BALANCE = "balance";
    private static final String EMAIL = "email";
    private static final String REAL_ACC_PAGE = "realAccPage";
    private static final String REAL_ACC_USER = "realAccUser";
    private static final String REAL_ACC_PASS = "realAccPass";
    private static final String REAL_ACC_VAL = "realAccVal";
    private static final String REAL_ACC_SECRET = "realAccSecret";

    private static final String OP_TABLE_NAME = "OpHistory";

    private static final String OP_ID = "id";
    private static final String OP_USERNAME = "username";
    private static final String OP_TYPE = "opType";
    private static final String OP_BALANCE = "balance";
    private static final String OP_PROFIT = "opProfit";
    private static final String OP_RESULT = "opResult";
    private static final String OP_DATE = "opDate";

    // CreateQuery String

    private final String CREATE_TABLE = "CREATE TABLE " + USER_TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USERNAME + " TEXT,"
            + PASSWORD + " TEXT,"
            + BALANCE + " REAL,"
            + EMAIL + " TEXT,"
            + REAL_ACC_PAGE + " TEXT,"
            + REAL_ACC_USER + " TEXT,"
            + REAL_ACC_PASS + " TEXT,"
            + REAL_ACC_VAL + " TEXT,"
            + REAL_ACC_SECRET + " TEXT"
            + ")";

    private final String CREATE_TABLE2 = "CREATE TABLE " + OP_TABLE_NAME + "("
            + OP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + OP_USERNAME + " TEXT,"
            + OP_TYPE + " TEXT,"
            + OP_BALANCE + " TEXT,"
            + OP_PROFIT + " TEXT,"
            + OP_RESULT + " TEXT,"
            + OP_DATE + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OP_TABLE_NAME);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public long insertUser(RTUser user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(USERNAME, user.getRtuser());
        values.put(PASSWORD, user.getRtpass());
        values.put(EMAIL, user.getRtemail());

        // insert row
        long id = db.insert(USER_TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertOperation(RTOperation operation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(OP_USERNAME, operation.getUsername());
        values.put(OP_TYPE, operation.getOpType());
        values.put(OP_BALANCE, operation.getBalance().toString());
        values.put(OP_PROFIT, operation.getOpProfit().toString());
        values.put(OP_RESULT, operation.getOpResult());
        values.put(OP_DATE, operation.getOpDate());
        // insert row
        long id = db.insert(OP_TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public RTUser getUser(String email) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(USER_TABLE_NAME,
                new String[]{USERNAME, PASSWORD, EMAIL},
                EMAIL + "=?",
                new String[]{email}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return null;

        // prepare note object
        RTUser user = new RTUser(cursor.getString(cursor.getColumnIndex(USERNAME)),
                cursor.getString(cursor.getColumnIndex(PASSWORD)));

        // close the db connection
        cursor.close();

        return user;
    }

    public List<RTUser> getAllUsers() {
        List<RTUser> users = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + USER_TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RTUser user = new RTUser(cursor.getString(cursor.getColumnIndex(USERNAME))
                ,cursor.getString(cursor.getColumnIndex(PASSWORD)));
                user.setRtemail(cursor.getString(cursor.getColumnIndex(EMAIL)));
                user.setRtbalance(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(BALANCE))));

                users.add(user);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return users;
    }

    public List<RTOperation> getUserOperations(String user) {
        List<RTOperation> operations = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + OP_TABLE_NAME + " WHERE " + OP_USERNAME + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {user});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RTOperation operation = new RTOperation(cursor.getString(cursor.getColumnIndex(OP_USERNAME)),
                        cursor.getString(cursor.getColumnIndex(OP_TYPE)),
                        new BigDecimal(cursor.getString(cursor.getColumnIndex(OP_BALANCE))),
                        new BigDecimal(cursor.getString(cursor.getColumnIndex(OP_PROFIT))),
                        cursor.getString(cursor.getColumnIndex(OP_RESULT)),
                        cursor.getString(cursor.getColumnIndex(OP_DATE)));

                operations.add(operation);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return operations;
    }

    public int delUserOperations(String user){
        SQLiteDatabase db = this.getWritableDatabase();
        int ans = db.delete(OP_TABLE_NAME,OP_USERNAME+"=?",new String[] {user});
        return ans;
    }
}
