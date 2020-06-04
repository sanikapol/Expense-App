//InClass 11
//File Name: Group12_Inclass11
//Sanika Pol
//Snehal Kekane
package com.example.inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddExpense extends AppCompatActivity {

    private static String TAG = "demo";
    private EditText et_name,et_amount;
    private Button btn_save,btn_cancel;
    private Spinner spn;
    private ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        setTitle("Add Expense");

        et_name = findViewById(R.id.et_expense_name);
        et_amount = findViewById(R.id.et_amount);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        spn = findViewById(R.id.spinner);
        categories = new ArrayList<>();
        categories.add("Groceries");
        categories.add("Invoice");
        categories.add("Transportation");
        categories.add("Shopping");
        categories.add("Trips");
        categories.add("Rent");
        categories.add("Utilities");
        categories.add("Other");

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddExpense.this,android.R.layout.simple_spinner_dropdown_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(dataAdapter);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_name.getText().toString().trim().length() == 0){
                    Toast.makeText(AddExpense.this,"Please enter expense name!",Toast.LENGTH_SHORT).show();
                }
                else if(et_amount.getText().toString().trim().length() == 0){
                    Toast.makeText(AddExpense.this,"Please enter expense amount!",Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String name = et_name.getText().toString().trim();
                    String category = spn.getSelectedItem().toString();
                    Double amt = Double.parseDouble(et_amount.getText().toString().trim());
                    double finalAmt = Math.round(amt*100.0)/100.0;
                    Expense expense = new Expense(name, category, finalAmt);
                    Date date = new Date();
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                    expense.setDate(outputFormat.format(date));

                    HashMap mapExpense = expense.toHashMap();
                    db.collection("expenses")
                            .add(mapExpense)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });

                    finish();
                }
            }

        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
