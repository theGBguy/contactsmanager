package com.gbsoft.contactsmanager.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gbsoft.contactsmanager.data.Constants;
import com.gbsoft.contactsmanager.model.Contact;

import java.util.ArrayList;

/**
 * Created by Ravi Lal Pandey on 17/01/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null , Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // CREATE TABLE CONTACTS ( _ID INTEGER PRIMARY KEY, CONTACT_TITLES TEXT, CONTACT_NUMS TEXT );
        String CREATE_TABLE_CONTACTS = "CREATE TABLE "+ Constants.TABLE_NAME+" ( "+Constants.ID+
                " INTEGER PRIMARY KEY, "+Constants.CONTACT_NAME+" TEXT, "+Constants.CONTACT_NUM+" TEXT );";
        sqLiteDatabase.execSQL(CREATE_TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ Constants.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void addAContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.CONTACT_NAME, contact.getPersonName());
        values.put(Constants.CONTACT_NUM, contact.getPersonPhone());
        db.insert(Constants.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteAContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.ID+" = ? ", new String[]{String.valueOf(id)});
        Log.d("delete", "one item is being deleted");
        db.close();
    }

    public ArrayList<Contact> getAllContacts(){
        ArrayList<Contact> contactArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.ID, Constants.CONTACT_NAME, Constants.CONTACT_NUM},
                null, null, null, null, Constants.ID+ " DESC");
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex(Constants.CONTACT_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(Constants.CONTACT_NUM));
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID)));
                    contactArrayList.add(new Contact(id, name, number));
                }while(cursor.moveToNext());
            }
        }
        db.close();
        if(cursor != null)  cursor.close();
        return contactArrayList;
    }

    public Contact getAContact(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.ID, Constants.CONTACT_NAME, Constants.CONTACT_NUM},
                Constants.ID+" =? ", new String[]{String.valueOf(id)}, null, null, null);
        String name = cursor.getString(cursor.getColumnIndex(Constants.CONTACT_NAME));
        String number = cursor.getString(cursor.getColumnIndex(Constants.CONTACT_NUM));
        int id1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID)));
        db.close();
        cursor.close();
        return new Contact(id1, name, number);
    }
    public void updateContact(int id, Contact newContact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.CONTACT_NAME, newContact.getPersonName());
        values.put(Constants.CONTACT_NUM, newContact.getPersonPhone());
        db.update(Constants.TABLE_NAME, values, Constants.ID+" =? ", new String[]{String.valueOf(id)});
        db.close();
    }
}
