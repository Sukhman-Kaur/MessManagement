package com.example.sukhman.messapp.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sukhman.messapp.First;
import com.example.sukhman.messapp.PriceListActivity;
import com.example.sukhman.messapp.R;
import com.example.sukhman.messapp.Util;
import com.example.sukhman.messapp.model.Bill;
import com.example.sukhman.messapp.model.Student;
import com.example.sukhman.messapp.provider.MyContentProvider;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewStudentActivity extends AppCompatActivity {

    @BindView(R.id.Name)
    TextView txtName;
    @BindView(R.id.Phone)
    TextView txtPhone;
    @BindView(R.id.Year)
    TextView txtYear;
    @BindView(R.id.Id)
    TextView txtId;
    @BindView(R.id.acc_id)
            TextView txtAc_Id;
    @BindView(R.id.button4)
    Button btnPdf;
    @BindView(R.id.bill)
            Button btnBill;

    Context context;
    ContentResolver resolver;
    Bill bill;
    Student student;
    ArrayList<String> bills;
    ArrayAdapter<String> adapter;
    TableLayout tableLayout;
    Intent rcv;
    Chunk mOrderDetailsTitleChunk;
    LineSeparator lineSeparator;
    File file1;
    TextView tv;
    String t;
    boolean pdfMode=false;
    PdfPTable table;
    TextView tExtra;
    TextView tAccount;
    TextView tBill;
    Button tBtn;
    int totalExtra;
    SharedPreferences preferences;
    Toolbar toolbar;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        ButterKnife.bind(this);
            toolbar=findViewById(R.id.toolbar_top);
            txt = toolbar.findViewById(R.id.toolbar_title);
            // txt.setText(toolbar.getTitle());

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        rcv = getIntent();
        context = this;
        bill=new Bill();
        bills=new ArrayList<>();
        tableLayout=findViewById(R.id.tab);
        student=new Student();
        txtName.setText("Name: " + rcv.getStringExtra("keyName"));
        txtPhone.setText("Phone: " + rcv.getStringExtra("keyPhone"));
        txtId.setText("Student Id: " + rcv.getIntExtra("keyId", 0));
        txtYear.setText("Year: " + rcv.getIntExtra("keyYear", 0));
        txtAc_Id.setText("Account Id: " + rcv.getIntExtra("keyAcc_Id", 0));
        preferences = getSharedPreferences("data",MODE_PRIVATE);

        btnBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateBill();
            }
        });
        rowHeader();
       queryBill();

    }
    public void queryBill(){
        resolver = getContentResolver();
       String selection =  Util.COL_SID +" = "+String.valueOf(rcv.getIntExtra("keyId", 0));
       //String[] selectionArgs = new String[]{String.valueOf(txtId.getText())};
        Log.i("test",String.valueOf(rcv.getIntExtra("keyId", 0)));
        String[] projection1={Util.COL_DATE,Util.COL_STATUS,Util.COL_EXTRA,Util.COL_PRICE};
        Cursor cursor1 = resolver.query(Util.USER_URI1,projection1,selection,null,null);
            if (cursor1 != null) {
                if(!pdfMode) {

                    while (cursor1.moveToNext()) {
                        // bill.bid=cursor1.getInt(cursor1.getColumnIndex(Util.COL_BILL_ID));
                        // student.id=cursor1.getInt(cursor1.getColumnIndex(Util.COL_SID));
                        bill.status = cursor1.getInt(cursor1.getColumnIndex(Util.COL_STATUS));
                        bill.description = cursor1.getString(cursor1.getColumnIndex(Util.COL_EXTRA));
                        bill.price = cursor1.getInt(cursor1.getColumnIndex(Util.COL_PRICE));
                        bill.date = cursor1.getString(cursor1.getColumnIndex(Util.COL_DATE));
                        // bills.add(bill.toString());
                        Log.i("test", "Bill details ," + bill.status + "," + bill.description + "," + bill.price + "," + bill.date + " " + String.valueOf(rcv.getIntExtra("keyId", 0)));

                        TableRow row = new TableRow(context);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
                        String[] colText = {bill.date, String.valueOf(bill.status), bill.description, String.valueOf(bill.price)};
                        for (String text : colText) {
                            TextView tv = new TextView(this);
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(16);
                            tv.setPadding(5, 5, 5, 5);
                            tv.setText(text);
                            row.addView(tv);
                        }
                        tableLayout.addView(row);
                    }
                }
                   else{
                       //cursor1.moveToFirst();
                       //int count = cursor1.getCount();

                       table = new PdfPTable(new float[]{2, 1, 2, 1});
                       table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                       table.addCell("DATE");
                       table.addCell("STATUS");
                       table.addCell("EXTRA");
                       table.addCell("PRICE");
                       table.setHeaderRows(1);
                       PdfPCell[] cells = table.getRow(0).getCells();
                       for (int j = 0; j < cells.length; j++) {
                           cells[j].setBackgroundColor(BaseColor.GRAY);
                       }
                    while(cursor1.moveToNext()){
                           table.addCell(cursor1.getString(cursor1.getColumnIndex(Util.COL_DATE)));
                           table.addCell(String.valueOf(cursor1.getInt(cursor1.getColumnIndex(Util.COL_STATUS))));
                           table.addCell(cursor1.getString(cursor1.getColumnIndex(Util.COL_EXTRA)));
                           table.addCell(String.valueOf(cursor1.getInt(cursor1.getColumnIndex(Util.COL_PRICE))));
                           } }
                   }
        }


    void rowHeader(){
        TableRow rowHeader = new TableRow(context);
    rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
    rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT));
    String[] headerText={"DATE","STATUS","EXTRA","PRICE"};
    for(String c:headerText) {
        tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(18);
        tv.setPadding(5, 5, 5, 5);
        tv.setText(c);
        rowHeader.addView(tv);
    }
    tableLayout.addView(rowHeader);

}
  public void generatePdf(View vRef){
    //Document document = new Document();
      com.itextpdf.text.Document document = new com.itextpdf.text.Document();

      String file = Environment.getExternalStorageDirectory().getPath() + "/"+txtAc_Id.getText().toString()+".pdf";
      file1=new File(file);
      if (!file1.exists()) {
          try {
              file1.createNewFile();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }

      try {
        PdfWriter.getInstance(document,new FileOutputStream(file1.getAbsoluteFile()));
    } catch (DocumentException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
      document.open();
      document.setPageSize(PageSize.A4);
      document.addCreationDate();
    //  document.addAuthor("Sukhman");
     // document.addCreator("Sukhmandeep Kaur");
      BaseFont urName = null;
      try {
          urName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
      } catch (DocumentException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }
      Font mOrderDetailsTitleFont = new Font(urName,20.0f, Font.BOLD, BaseColor.BLACK);
      mOrderDetailsTitleChunk = new Chunk("Bill Details",mOrderDetailsTitleFont);


      Paragraph p1 = new Paragraph("Hello PDF");
      Paragraph p2 = new Paragraph(mOrderDetailsTitleChunk);
      Paragraph p3=new Paragraph(tv.getText().toString());
      p1.setAlignment(Element.ALIGN_CENTER);
      p2.setAlignment(Element.ALIGN_LEFT);
      pdfMode=true;
      queryBill();
      try {
         document.add(p2);
          document.add(new Paragraph(""));
          LineSeparator lineSeparator = new LineSeparator();
          lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
          document.add(new Chunk(lineSeparator));
          document.add(new Paragraph(""));
          document.add(p1);
          document.add(p3);
          document.add(table);


      } catch (DocumentException e) {
        e.printStackTrace();
    }
    document.close();
    Toast.makeText(this,"pdf created",Toast.LENGTH_SHORT).show();
    viewPdf();

}
private void viewPdf(){
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    startActivity(intent);
}
     @SuppressLint("NewApi")
     void generateBill(){
        int t=0;
for(int i=0;i<12;i++) {

    String s= String.format("%02d", (i+1));
    String selection = Util.COL_SID + " = " + String.valueOf(rcv.getIntExtra("keyId", 0));
    String table2 = "SELECT * FROM " + Util.TAB_NAME1 + " WHERE strftime('%m', " + Util.COL_DATE + " ) = '" +s+"'";

    Cursor cur = (MyContentProvider.sqLiteDatabase).rawQuery("SELECT SUM(" + Util.COL_PRICE + ") FROM " +Util.TAB_NAME1+ " WHERE " + selection, null);
    if (cur.moveToFirst())
        totalExtra = cur.getInt(0);
    Log.i("test", "generateBill: "+totalExtra);
//    t+=totalExtra[i];
}
         final Dialog dialog=new Dialog(this);
         View view  = getLayoutInflater().inflate(R.layout.bill_details, null);
         dialog.setContentView(view);
         tExtra=view.findViewById(R.id.tExtra);
         tExtra.setText("\u20b9 "+String.valueOf(totalExtra));
         tAccount=view.findViewById(R.id.tAccount);
         tAccount.setText(String.valueOf(getRowsCount()));
         tBill=view.findViewById(R.id.tBill);
         tBill.setText(String.valueOf(totalBill()));
         tBtn=view.findViewById(R.id.sendBill);
         SmsManager smsManager = SmsManager.getDefault();
         String message =rcv.getStringExtra("keyName")+", "+rcv.getIntExtra("keyAcc_Id", 0)+ "\n Your Monthly Mess Bill is \u20b9"+String.valueOf(totalBill());
         String phone = "+91 "+rcv.getStringExtra("keyPhone");
         smsManager.sendTextMessage(phone,null,message,null,null);
         Toast.makeText(ViewStudentActivity.this,"Message sent",Toast.LENGTH_LONG).show();

         dialog.show();
         Window window = dialog.getWindow();
         window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 800);
         Toast.makeText(this,"Bill generated",Toast.LENGTH_SHORT).show();
         
     }
    public int getRowsCount() {
        String table1 = "( SELECT * FROM "+Util.TAB_NAME1+" WHERE "+ Util.COL_SID +" = "+String.valueOf(rcv.getIntExtra("keyId", 0)) +" AND "+ Util.COL_STATUS +" = "+String.valueOf(0)+" )";
        String table="( SELECT Distinct "+Util.COL_DATE+" , "+Util.COL_STATUS+" From "+table1+" )";
        int count = (int) DatabaseUtils.queryNumEntries(MyContentProvider.sqLiteDatabase, table,null,null);

        return count;
    }
    int daysInMonth(){
        YearMonth yearMonthObject = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = sdf.format(new Date());
        String[] ymd = strDate.split("-");
        int year = Integer.parseInt(ymd[2]);
        int month = Integer.parseInt(ymd[1]);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            yearMonthObject = YearMonth.of(year, month);
            return yearMonthObject.lengthOfMonth(); //28
        }
        else{
            return 0;
        }
    }

    int daysBetweenDays(){
        int days=0;
        String selection =  Util.COL_SID +" = "+String.valueOf(rcv.getIntExtra("keyId", 0));
        Cursor cur = (MyContentProvider.sqLiteDatabase).rawQuery("SELECT * FROM "+Util.TAB_NAME1+" WHERE "+selection, null);
        cur.moveToLast();
        String strLast=cur.getString(cur.getColumnIndex(Util.COL_DATE));
        cur.moveToFirst();
        String strStart=cur.getString(cur.getColumnIndex(Util.COL_DATE));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date endDate,startDate;
            endDate = sdf.parse(strLast);
            startDate = sdf.parse(strStart);
            Log.i("test", endDate.toString()+","+startDate.toString());
            long diff = endDate.getTime() - startDate.getTime();
            days= (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return days;
    }
    int totalBill(){
        int bill[]=new int[12];
        //Log.i("test", "total price "+String.valueOf(menuTotal())+","+String.valueOf(daysBetweenDays()));
        int temp=0;
        for(int i=0;i<12;i++){
            bill[i] = (preferences.getInt("keyDiet"+String.valueOf(i+1),0)*preferences.getInt("keyTotalDays"+String.valueOf(i+1),0))-(preferences.getInt("keyRebate"+String.valueOf(i+1),0)*getRowsCount())+totalExtra;
            temp+=bill[i];
        }
        return temp;
    }
}