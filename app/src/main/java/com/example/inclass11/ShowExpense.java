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
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class ShowExpense extends AppCompatActivity {
    private static String TAG = "demo";

    private TextView tv_Name,tv_Category,tv_Amount,tv_Date;
    private Button btn_Close,btn_save;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        setTitle("Show Expense");
        tv_Name = findViewById(R.id.tv_name);
        tv_Amount = findViewById(R.id.tv_amountExp);
        tv_Category = findViewById(R.id.tv_category);
        tv_Date = findViewById(R.id.tv_date);
        btn_Close = findViewById(R.id.bt_close);
        btn_save = findViewById(R.id.btn_edit);


        if (getIntent()!=null && getIntent().getExtras()!=null){
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
                            tv_Name.setText(document.get("name").toString());
                            tv_Category.setText(document.get("category").toString());
                            tv_Amount.setText("$" + document.get("amount").toString());
                            tv_Date.setText(document.get("date").toString());

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }

        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EditExp = new Intent(ShowExpense.this,EditExpense.class);
                EditExp.putExtra(MainActivity.EXPENSE_KEY,id);
                startActivity(EditExp);
            }
        });
    }
}
