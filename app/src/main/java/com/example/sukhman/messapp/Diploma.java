package com.example.sukhman.messapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.sukhman.messapp.adapter.StudentArrayAdapter;
import com.example.sukhman.messapp.model.Bill;
import com.example.sukhman.messapp.model.Student;
import com.example.sukhman.messapp.ui.InsertStudentActivity;
import com.example.sukhman.messapp.ui.ViewStudentActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class Diploma extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    String name;
    View view;
    Button btnExtra;
    Switch swAccount;
    StudentArrayAdapter studentArrayAdapter;
    ContentResolver resolver;
    Student student;
    ArrayList<Student> students;
    ArrayList<String> bills;

    ListView listView;
    AlertDialog.Builder builder;
    Bill bill;
    EditText eSearch;
    InsertStudentActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.first_frag,container,false);

        student = new Student();
        bill=new Bill();
        bills=new ArrayList<>();

        listView=view.findViewById(R.id.listView);
        students=new ArrayList<>();


        View header = getLayoutInflater().inflate(R.layout.header, null);

        View footer = getLayoutInflater().inflate(R.layout.footer, null);
        listView.addHeaderView(header);
        eSearch=header.findViewById(R.id.textSearch);

        listView.addFooterView(footer);
        listView.setOnItemClickListener(this);
        resolver = getContext().getContentResolver();
        queryUser();
        builder=new AlertDialog.Builder(getContext());

        eSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = eSearch.getText().toString().toLowerCase(Locale.getDefault());
                studentArrayAdapter.filter(text);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // mActivity = (InsertStudentActivity)getActivity();
        mActivity=new InsertStudentActivity();
        return view;
    }
    public void queryUser() {

        String[] projection = {Util.COL_ID,Util.COL_ACC_ID, Util.COL_NAME, Util.COL_PHONE,Util.COL_YEAR};//null
        String where=Util.COL_YEAR+"="+String.valueOf(6);
        Cursor cursor = resolver.query(Util.USER_URI, projection, where, null, Util.COL_ACC_ID + " ASC");

        if (cursor != null) {

            while (cursor.moveToNext()) {

                students.add(new Student(cursor.getInt(cursor.getColumnIndex(Util.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(Util.COL_NAME)),
                        cursor.getString(cursor.getColumnIndex(Util.COL_PHONE)),
                        cursor.getInt(cursor.getColumnIndex(Util.COL_YEAR)),
                        cursor.getInt(cursor.getColumnIndex(Util.COL_ACC_ID))));
            }


            studentArrayAdapter = new StudentArrayAdapter(getContext(), R.layout.list_students, students);

            listView.setAdapter(studentArrayAdapter);
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);
        }
    }
    void display(){
        String[] item={"View","Update"};
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i){
                    case 0:
                        Intent intent=new Intent(getActivity(), ViewStudentActivity.class);
                        intent.putExtra("keyName",student.name);
                        intent.putExtra("keyPhone",student.phone);
                        intent.putExtra("keyId",student.id);
                        intent.putExtra("keyYear",student.year);
                        intent.putExtra("keyAcc_Id",student.acc_id);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1=new Intent(getContext(),InsertStudentActivity.class);
                        intent1.putExtra("keyStudent",student);
                        intent1.putExtra("keyPackage","com.example.sukhman.messapp");
                        startActivity(intent1);
                        break;
                }
            }
        });
        builder.show();

    }
    void addItems(){
        final Dialog dialog=new Dialog(getContext());
        View view  = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog, null);
        dialog.setContentView(view);
        btnExtra=view.findViewById(R.id.button2);
        Button btnSubmit=view.findViewById(R.id.btnInsert);
        swAccount=view.findViewById(R.id.switch1);

        swAccount.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swAccount.isChecked()){
                    bill.status=1;  //Open
                    swAccount.setText("Open");
                }
                else{
                    bill.status=0;//Closed
                    swAccount.setText("Close");
                }
            }
        });


        btnExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog1();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertBill();
                dialog.dismiss();
                bill.price=0;
                bill.description=null;
            }
        });
        dialog.show();
    }

    EditText eDesc,ePrice;
    Dialog dialog1;
    Button btn;

    void showDialog1(){
        dialog1 = new Dialog(getContext());
        View view1  = getActivity().getLayoutInflater().inflate(R.layout.add_extra, null);
        dialog1.setContentView(view1);
        eDesc=view1.findViewById(R.id.eDesc);
        ePrice=view1.findViewById(R.id.ePrice);
        btn = view1.findViewById(R.id.done);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bill.description=eDesc.getText().toString();
                bill.price= Integer.parseInt(ePrice.getText().toString());
                Log.i("test",bill.description+" "+bill.price);
                if(validate()==true){
                    dialog1.dismiss();
                }
                else{
                    Toast.makeText(getContext(),"Enter Details Properly!!",Toast.LENGTH_SHORT).show();
                    eDesc.setText("");
                    ePrice.setText("");
                }
            }
        });
        dialog1.show();
    }

    boolean  validate(){
        boolean flag=true;
        if(bill.description.isEmpty()){
            flag=false;
        }
        if(String.valueOf(bill.price).isEmpty()){
            flag=false;
        }
        return flag;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Log.i("test",String.valueOf(i));
        student=students.get(i-1);
        addItems();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        student=students.get(i-1);
        display();
        return true;
    }
    void insertBill(){
        ContentValues contentValues=new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = sdf.format(new Date());

        contentValues.put(Util.COL_DATE, strDate);
        contentValues.put(Util.COL_SID,student.id);
        contentValues.put(Util.COL_STATUS,bill.status);
        contentValues.put(Util.COL_EXTRA,bill.description);
        contentValues.put(Util.COL_PRICE,bill.price);

        Uri uri = resolver.insert(Util.USER_URI1,contentValues);
        Toast.makeText(getContext(),"Bill"+uri.getLastPathSegment()+ " Added Successfully!! , "+student.id,Toast.LENGTH_LONG).show();

    }

}

