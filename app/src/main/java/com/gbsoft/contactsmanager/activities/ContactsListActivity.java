package com.gbsoft.contactsmanager.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gbsoft.contactsmanager.R;
import com.gbsoft.contactsmanager.helper.CustomViewAdapter;
import com.gbsoft.contactsmanager.helper.DatabaseHandler;
import com.gbsoft.contactsmanager.model.Contact;

import java.util.ArrayList;

public class ContactsListActivity extends AppCompatActivity {
    private ArrayList<Contact> contacts = new ArrayList<>();
    private DatabaseHandler dbHandler;
    private RecyclerView rv;
    private CustomViewAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHandler = new DatabaseHandler(this);

        rv = findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        contacts = dbHandler.getAllContacts();

        myAdapter = new CustomViewAdapter(this, contacts);
        rv.setAdapter(myAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildAddDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buildAddDialog() {
        final DatabaseHandler handler = new DatabaseHandler(this);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View popView = LayoutInflater.from(this).inflate(R.layout.popup_add_edit, null);
        Button btnSave = popView.findViewById(R.id.btnSave);
        Button btnCancel = popView.findViewById(R.id.btnCancel);
        TextView tvTitle = popView.findViewById(R.id.txtView);
        final EditText edtName = popView.findViewById(R.id.popupEdtTxtName);
        final EditText edtNum = popView.findViewById(R.id.popupEdtTxtPhone);
        tvTitle.setText(getResources().getString(R.string.add_popup_title));
        builder.setView(popView);
        final android.app.AlertDialog dialog = builder.create();
        dialog.show();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = new Contact(edtName.getText().toString(), edtNum.getText().toString());
                handler.addAContact(contact);
                handler.close();
                contacts.add(0, contact);
                myAdapter.notifyItemInserted(0);
                Snackbar.make(view, "Item has been added", Snackbar.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 2000);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
