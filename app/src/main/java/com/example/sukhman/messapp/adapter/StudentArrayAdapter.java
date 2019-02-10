package com.example.sukhman.messapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Switch;
import android.widget.TextView;

import com.example.sukhman.messapp.R;
import com.example.sukhman.messapp.model.Bill;
import com.example.sukhman.messapp.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class StudentArrayAdapter extends ArrayAdapter<Student> implements Filterable {
    Context context;
    int resource;
    ArrayList<Student> objects;// for loading main list
    private ArrayList<Student> arraylist=null;  // for loading  filter data


    public StudentArrayAdapter(Context context, int resource, ArrayList<Student> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        Log.i("test",objects.toString());
        this.arraylist = new ArrayList<Student>();
        this.arraylist.addAll(objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//1. Create a View which points to Layout i.e. list_item

        View view = LayoutInflater.from(context).inflate(resource, parent, false);


        TextView txtName = view.findViewById(R.id.listName);
        TextView txtAccId = view.findViewById(R.id.listAcc);
       // Switch swAtten=view.findViewById(R.id.attendance);

        Student student = objects.get(position);

        txtName.setText(student.name);
        txtAccId.setText(String.valueOf(student.acc_id));


        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        objects.clear();
        if (charText.length() == 0) {
            objects.addAll(arraylist);
        }
        else{
                for (Student wp : arraylist) {
                    if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        objects.add(wp);
                    }
                }
            }
        notifyDataSetChanged();
    }
//    public List<Bill> get_Bill()
//    {
//
//        List<Bill> billList = new ArrayList<>();
//        //select query
//        String selectQuery = "SELECT * FROM TAB_NAME1";
//
//        SQLiteDatabase sqLiteDatabase = getContext().getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
//
//
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
//        {
//            Bill bill = new Bill();
//            bill.setId(cursor.getInt(0));
//            bill.setStatus(cursor.getInt(2));
//            bill.setDescription(cursor.getString(3));
//            bill.setDate(cursor.getInt(1));
//            bill.setPrice(cursor.getInt(4));
//
//
//            // Adding person to list
//            billList.add(bill);
//        }
//
//        return billList;
//
//    }
}
