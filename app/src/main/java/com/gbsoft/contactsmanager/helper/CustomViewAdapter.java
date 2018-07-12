package com.gbsoft.contactsmanager.helper;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gbsoft.contactsmanager.R;
import com.gbsoft.contactsmanager.model.Contact;

import java.util.ArrayList;

/**
 * Created by Ravi Lal Pandey on 17/01/2018.
 */

public class CustomViewAdapter extends RecyclerView.Adapter<CustomViewAdapter.CustomViewHolder> {
    private Context adaptContext;
    private ArrayList<Contact> contacts;
    private android.app.AlertDialog dialog;
    private android.app.AlertDialog.Builder builder;

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(adaptContext).inflate(R.layout.recycler_row, parent, false);
        return new CustomViewHolder(v, adaptContext);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        holder.tvName.setText(contacts.get(position).getPersonName());
        holder.tvPhoneNo.setText(contacts.get(position).getPersonPhone());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public CustomViewAdapter(Context con, ArrayList<Contact> conts) {
        this.adaptContext = con;
        this.contacts = conts;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName, tvPhoneNo;
        private Button btnEdit, btnDelete;
        private Context context;

        private CustomViewHolder(View view, Context ctx) {
            super(view);
            context = ctx;
            tvName = view.findViewById(R.id.txtPersonName);
            tvPhoneNo = view.findViewById(R.id.txtPhoneNum);
            btnEdit = view.findViewById(R.id.btnEdit);
            btnDelete = view.findViewById(R.id.btnDelete);

            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        public void deleteDialog() {
            builder = new android.app.AlertDialog.Builder(context);
            final View v = LayoutInflater.from(context).inflate(R.layout.popup_delete, null);
            Button btnNo = v.findViewById(R.id.btnNo);
            Button btnYes = v.findViewById(R.id.btnYes);
            builder.setView(v);
            dialog = builder.create();
            dialog.show();
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler dbHandler = new DatabaseHandler(context);
                    dbHandler.deleteAContact(contacts.get(getAdapterPosition()).getId());
                    dbHandler.close();
                    contacts.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    Snackbar.make(v, "Item has been deleted", Snackbar.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 2000);
                }
            });
        }

        public void buildEditDialog() {
            builder = new android.app.AlertDialog.Builder(context);
            View popView = LayoutInflater.from(context).inflate(R.layout.popup_add_edit, null);
            Button btnSave = popView.findViewById(R.id.btnSave);
            Button btnCancel = popView.findViewById(R.id.btnCancel);
            TextView tvTitle = popView.findViewById(R.id.txtView);
            final EditText edtName = popView.findViewById(R.id.popupEdtTxtName);
            final EditText edtNum = popView.findViewById(R.id.popupEdtTxtPhone);
            tvTitle.setText(context.getResources().getString(R.string.edit_popup_title));
            builder.setView(popView);
            dialog = builder.create();
            dialog.show();
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = contacts.get(getAdapterPosition()).getId();
                    Contact contact = new Contact(edtName.getText().toString(), edtNum.getText().toString());
                    contacts.set(getAdapterPosition(), contact);
                    DatabaseHandler handler = new DatabaseHandler(context);
                    handler.updateContact(id, contact);
                    handler.close();
                    notifyItemChanged(getAdapterPosition(), contact);
                    Snackbar.make(view, "Item has been edited", Snackbar.LENGTH_SHORT).show();
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

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnEdit:
                    buildEditDialog();
                    break;
                case R.id.btnDelete:
                    deleteDialog();
                    break;
            }
        }
    }
}
