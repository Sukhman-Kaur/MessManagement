package com.example.sukhman.messapp.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sukhman.messapp.First;
import com.example.sukhman.messapp.R;
import com.example.sukhman.messapp.Second;
import com.example.sukhman.messapp.Util;
import com.example.sukhman.messapp.adapter.StudentArrayAdapter;
import com.example.sukhman.messapp.model.Student;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InsertStudentActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.name) EditText etxtName;
    @BindView(R.id.phone) EditText etxtPhone;
    @BindView(R.id.year) TextView etxtYear;

    @BindView(R.id.register) Button btnRegister;
    Student student;
    ContentResolver resolver;
    boolean updateMode;
    StudentArrayAdapter studentArrayAdapter;

    @BindView(R.id.usernameWrapper) TextInputLayout nameWrapper;
    @BindView(R.id.phoneWrapper) TextInputLayout phoneWrapper;

    ArrayList<Student> students;
    @BindView(R.id.view) LinearLayout view;
    @BindView(R.id.eAccId)EditText etxtAcc_Id;

    textEntered txt;

    public interface textEntered {
        void setValue1(String value);
        void setValue2(int value);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_student);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setTitle("Insert Student");
        ButterKnife.bind(this);
        student = new Student();
        students = new ArrayList<>();
        nameWrapper.setHint("Name");
        phoneWrapper.setHint("Phone No.");
        phoneWrapper.setErrorEnabled(false);
        phoneWrapper.setCounterMaxLength(10);
        phoneWrapper.setCounterEnabled(true);
        studentArrayAdapter = new StudentArrayAdapter(getApplicationContext(), R.layout.list_students, students);
        resolver = getContentResolver();
        btnRegister.setOnClickListener(this);
        Intent rcv = getIntent();

        Bundle rcvBun = rcv.getBundleExtra("keyBundle");
        Fragment fragment=null;

        //String year=rcv.getStringExtra("keyPos");
        if (rcv.hasExtra("keyPackage")) {
            if (rcv.getStringExtra("keyPackage").equals("com.example.sukhman.messapp")) {
                updateMode = rcv.hasExtra("keyStudent");
                if (updateMode) {
                    student = (Student) rcv.getSerializableExtra("keyStudent");
                    etxtName.setText(student.name);
                    etxtPhone.setText(student.phone);
                   // etxtAcc_Id.setText(student.acc_id);
                    view.animate().translationY(600);
                    btnRegister.setText("Update " + student.name);
                    getSupportActionBar().setTitle("Update User");
                }
            }
        } else {
           // Bundle rcvBun = rcv.getBundleExtra("keyBundle");
            view.animate().translationY(0);
            int year1 = rcvBun.getInt("keyPos", 0);
            etxtYear.setText(String.valueOf(year1));
           // Toast.makeText(InsertStudentActivity.this, "Year:" + year1, Toast.LENGTH_LONG).show();
        }
        if(etxtYear.getText().toString()=="1"){
            fragment=new First();
        }
        txt = (textEntered)fragment;

        }

    public void insertUser(){
        ContentValues contentValues=new ContentValues();
        contentValues.put(Util.COL_ACC_ID,student.acc_id);
        contentValues.put(Util.COL_NAME,student.name);
        contentValues.put(Util.COL_PHONE,student.phone);
        contentValues.put(Util.COL_YEAR,student.year);


        if(!updateMode){
            Uri uri = resolver.insert(Util.USER_URI,contentValues);
            Toast.makeText(this,student.name+" Registered !! "+uri.getLastPathSegment(),Toast.LENGTH_LONG).show();

            Bundle bundle = new Bundle();
            bundle.putString("keyName", student.name);
            bundle.putInt("keyId", student.acc_id);

            // set Fragmentclass Arguments
            if(etxtYear.getText().toString()=="1") {

                Log.i("test",etxtYear.getText().toString());
                First fragobj = new First();
                            txt = (textEntered)fragobj;

                fragobj.setArguments(bundle);
            }
            if(etxtYear.getText().toString()=="2") {
                Second fragobj = new Second();
                fragobj.setArguments(bundle);
            }
        }
        else{
            String where = Util.COL_ID+" = "+student.id;
            int i = resolver.update(Util.USER_URI,contentValues,where,null);
            if(i>0){
                Toast.makeText(this,student.name+" Updated!!"+i,Toast.LENGTH_LONG).show();
                finish();
                Bundle bundle = new Bundle();
                bundle.putString("keyName", student.name);
                bundle.putInt("keyId", student.acc_id);

                // set Fragmentclass Arguments
                if(etxtYear.getText()=="1") {
                    First fragobj = new First();
                    fragobj.setArguments(bundle);
                }
                if(etxtYear.getText()=="2") {
                    Second fragobj = new Second();
                    fragobj.setArguments(bundle);
                }
//          //...Other two same as above
            }else{
                Toast.makeText(this,student.name+" Not Updated..."+i,Toast.LENGTH_LONG).show();
            }
        }

    }
    void  clearFields(){
        etxtName.setText("");
        etxtPhone.setText("");
        etxtAcc_Id.setText("");
    }

    boolean validateFields(){
        boolean flag = true;

        if(student.name.isEmpty()){
            flag = false;}
        if(String.valueOf(student.acc_id).isEmpty()) {
            flag = false;
        }

        if(student.phone.isEmpty()) {
            flag = false;
        }else{
            if(student.phone.length() != 10){
                flag = false;
                phoneWrapper.setErrorEnabled(true);
                phoneWrapper.setError("*It must contain 10 digits");
            }
        }
        return flag;
    }
    @Override
    public void onClick(View view) {
        student.name=etxtName.getText().toString();
        student.phone=etxtPhone.getText().toString();
        String acc= etxtAcc_Id.getText().toString();
        if (!acc.equals("") && !acc.equals(null)) {
            student.acc_id = Integer.parseInt(acc);
        }
        String year=etxtYear.getText().toString();

        if (!year.equals("") && !year.equals(null)) {
            student.year = Integer.parseInt(year);
        }
        if(validateFields()==true){
            phoneWrapper.setErrorEnabled(false);
            insertUser();
//            txt.setValue2(Integer.parseInt(etxtAcc_Id.getText().toString()));
  //          txt.setValue1(etxtName.getText().toString());
            clearFields();
//            Intent data = new Intent();
//            data.putExtra("keyName",student.name);
//            //   data.putExtra("keyPhone",student.phone);
           // setResult(776,data); // To Send the data back to MainActivity

        }else{
            Toast.makeText(this,"Please enter correct details first!!",Toast.LENGTH_LONG).show();
        }
    }


    public void onAttachFragment(Fragment fragment) {
        try {
            txt = (textEntered)fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString() + " must implement textEntered");
        }
    }

}
