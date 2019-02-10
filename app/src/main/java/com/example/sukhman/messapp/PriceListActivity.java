package com.example.sukhman.messapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sukhman.messapp.model.Bill;
import com.example.sukhman.messapp.ui.ViewStudentActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PriceListActivity extends AppCompatActivity {

    @BindView(R.id.rebate)EditText rebate;
    @BindView(R.id.diet)EditText diet;
    @BindView(R.id.sDate)EditText sDate;
    @BindView(R.id.nDays)EditText nDays;
    @BindView(R.id.tDiet)EditText tDiet;
    @BindView(R.id.read)Button btnRead;
    @BindView(R.id.save)Button btnSave;
    @BindView(R.id.ok)Button btnOk;
    @BindView(R.id.month)TextView month;
    @BindView(R.id.std)TextView std;

    Bill bill;
    EditText etxtName;
    EditText etxtPassword;
    File file;
    Dialog dialog;
    SharedPreferences preferences;   // Creates and Reads Data in XML File
    SharedPreferences.Editor editor; // Writes Data in XML File
     int pRebate[]=new int[13];
     int pDiet[]=new int[13];
    int tDays[]=new int[13];
    String sd[]=new String[13];
     public static int x;
     int totalDiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);
        getSupportActionBar().setTitle("Price List");
        ButterKnife.bind(this);
        bill=new Bill();
        Calendar calendar=Calendar.getInstance();
         x=calendar.get(Calendar.MONTH);
        String m="";
        switch (x+1){
            case 1: m="January";
            break;
            case 2: m="February";
                break;
            case 3: m="March";
                break;
            case 4: m="April";
                break;
            case 5: m="May";
                break;
            case 6: m="June";
                break;
            case 7: m="July";
                break;
            case 8: m="August";
                break;
            case 9: m="September";
                break;
            case 10: m="October";
                break;
            case 11: m="November";
                break;
            case 12: m="December";
                break;
            default: m=null;

        }
        month.setText(m);
        // Application SandBox
        preferences = getSharedPreferences("data",MODE_PRIVATE);
        editor = preferences.edit();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDate();
                totalDiet= Integer.parseInt(tDiet.getText().toString());
                editor.putInt("keyTotalDiet",totalDiet);

                editor.apply();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!rebate.getText().toString().isEmpty()&&(!diet.getText().toString().isEmpty())) {
                    dialog = new Dialog(PriceListActivity.this);
                    View view2 = getLayoutInflater().inflate(R.layout.authentication, null);
                    dialog.setContentView(view2);
                    etxtName = view2.findViewById(R.id.eUsername);
                    etxtPassword = view2.findViewById(R.id.ePassword);
                    Button btnOk = view2.findViewById(R.id.ok);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            validate();
                        }
                    });
                    Button btnCancel = view2.findViewById(R.id.cancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Enter Details First",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRead.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                pRebate[x+1] = preferences.getInt("keyRebate"+String.valueOf(x+1),0);
                rebate.setText("\u20b9 "+String.valueOf(pRebate[x+1]));
                pDiet[x+1] = preferences.getInt("keyDiet"+String.valueOf(x+1),0);
                diet.setText(String.valueOf(pDiet[x+1]));
                tDays[x+1] = preferences.getInt("keyTotalDays"+String.valueOf(x+1),0);
                nDays.setText(String.valueOf(tDays[x+1]));
                sd[x+1] = preferences.getString("keyDate",String.valueOf(x+1));
                sDate.animate().translationY(600);
                btnOk. animate().translationY(600);
                std.animate().translationY(600);
                totalDiet = preferences.getInt("keyTotalDiet",0);
                tDiet.setText(String.valueOf(totalDiet));
                Toast.makeText(getApplicationContext(),"Data retreived",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startDate() {
         sd[x]=sDate.getText().toString();
        String[] ymd = sd[x].split("/");
        endDate(ymd[0]);
        }

    private void endDate(String d){
        int c=Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        int r= (c+1) - Integer.parseInt(d);
        nDays.setText(String.valueOf(r));
        tDays[x+1]= Integer.parseInt(nDays.getText().toString());
        editor.putInt("keyTotalDays"+String.valueOf(x+1),tDays[x+1]);

        editor.apply();

        int x=r*Integer.parseInt(diet.getText().toString());
        tDiet.setText(String.valueOf(x));
    }

    void validate(){
        if(etxtName.getText().toString().equals("admin") && etxtPassword.getText().toString().equals("123")){
             pRebate[x+1]= Integer.parseInt(rebate.getText().toString());
            editor.putInt("keyRebate"+String.valueOf(x+1),pRebate[x+1]);
            pDiet[x+1]= Integer.parseInt(diet.getText().toString());
            editor.putInt("keyDiet"+String.valueOf(x+1),pDiet[x+1]);
            editor.putString("keyDate",sd[x+1] );
            editor.apply();

            rebate.setText("");
            diet.setText("");
            nDays.setText("");
            sDate.setText("");

            Toast.makeText(this,"Changes Saved!!",Toast.LENGTH_LONG).show();
            dialog.dismiss();

        }
        else{
            Toast.makeText(this,"Invalid Username or password",Toast.LENGTH_LONG).show();
            etxtPassword.setText("");
            etxtName.setText("");
        }
    }

    }

