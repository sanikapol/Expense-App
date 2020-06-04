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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EditExpense extends AppCompatActivity {
    private static String TAG = "demo";
    private EditText et_Name,et_Amount;
    private Spinner spn;
    private ArrayList<String> categories;
    private Button btn_Save,btn_Cancel;
    String id;
    private static ShowExpense se;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        setTitle("Edit Expense");
        et_Name = findViewById(R.id.et_expense_name);
        et_Amount = findViewById(R.id.et_expAmt);
        spn = findViewById(R.id.spinner);
        btn_Save = findViewById(R.id.btn_save);
        btn_Cancel = findViewById(R.id.btn_cancel);

        categories = new ArrayList<>();
        categories.add("Groceries");
        categories.add("Invoice");
        categories.add("Transportation");
        categories.add("Shopping");
        categories.add("Trips");
        categories.add("Rent");
        categories.add("Utilities");
        categories.add("Other");

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditExpense.this,android.R.layout.simple_spinner_dropdown_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(dataAdapter);


        if (getIntent()!=null && getIntent().getExtras()!=null) {
            id = getIntent().getExtras().getString(MainActivity.EXPENSE_KEY);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference docRef = db.collection("expenses").document(id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            et_Name.setText(document.get("name").toString());
                            int index = categories.indexOf(document.get("category").toString());
                            spn.setSelection(index);
                            et_Amount.setText("$" + document.get("amount").toString());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_Name.getText().toString().trim().length() == 0){
                    Toast.makeText(EditExpense.this,"Please enter expense name!",Toast.LENGTH_SHORT).show();
                }
                else if(et_Amount.getText().toString().trim().length() == 0){
                    Toast.makeText(EditExpense.this,"Please enter expense amount!",Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    double amt = Double.parseDouble(et_Amount.getText().toString().trim().substring(1));
                    double finalAmt = Math.round(amt*100.0)/100.0;
                    Expense expense = new Expense(et_Name.getText().toString().trim(), spn.getSelectedItem().toString(), finalAmt);
                    Date date = new Date();
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                    expense.setDate(outputFormat.format(date));
                    DocumentReference docRef = db.collection("expenses").document(id);
                    HashMap updatedExp = expense.toHashMap();
                    docRef.set(updatedExp);
                    Intent i = new Intent(EditExpense.this, MainActivity.class);        // Specify any activity here e.g. home or splash or login etc
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("EXIT", true);
                    startActivity(i);
                    finish();
                }
            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
