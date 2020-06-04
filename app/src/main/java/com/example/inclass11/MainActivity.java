//InClass 11
//File Name: Group12_Inclass11
//Sanika Pol
//Snehal Kekane
package com.example.inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements ExpenseAdapter.iExpenseOps{

    private static String TAG = "demo";
    private ImageView iv_add;
    private ArrayList<Expense> expenses;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter = null;
    public static String EXPENSE_KEY = "EXPENSE_KEY";
    private TextView tv_msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Expense App");

        iv_add = findViewById(R.id.iv_add);
        expenses = new ArrayList<Expense>();
        recyclerView = findViewById(R.id.Recycler_Expenses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        tv_msg = findViewById(R.id.tv_msg);

        tv_msg.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("expenses")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()== 0) {
                            tv_msg.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                        else{
                            tv_msg.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                                Log.d(TAG, documentSnapshot.getData() + "");
                                String name = documentSnapshot.get("name").toString();
                                String categoty = documentSnapshot.get("category").toString();
                                Double amt = Double.parseDouble(documentSnapshot.get("amount").toString());
                                Expense expense = new Expense(name, categoty, amt);
                                expense.setId(documentSnapshot.getId());
                                expense.setDate(documentSnapshot.get("date").toString());
                                expense.toString();
                                expenses.add(expense);
                            }
                        }

                        mAdapter = new ExpenseAdapter(expenses,MainActivity.this);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "There are no documents present", e);
                    }
                });

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addExpense = new Intent(MainActivity.this,AddExpense.class);
                startActivity(addExpense);
            }
        });


    }

   @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter!=null){
            tv_msg.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            expenses.clear();
            db.collection("expenses")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                                Log.d(TAG, documentSnapshot.getData() + "");
                                String name = documentSnapshot.get("name").toString();
                                String category = documentSnapshot.get("category").toString();
                                Double amt = Double.parseDouble(documentSnapshot.get("amount").toString());
                                Expense expense = new Expense(name,category,amt);
                                expense.setId(documentSnapshot.getId());
                                expense.setDate(documentSnapshot.get("date").toString());
                                expense.toString();
                                expenses.add(expense);
                            }

                            mAdapter = new ExpenseAdapter(expenses,MainActivity.this);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "There are no documents present", e);
                        }
                    });
        }

    }

    @Override
    public void Delete(int position) {
        String id = expenses.get(position).getId();
        expenses.remove(position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("expenses").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        Toast.makeText(MainActivity.this,"Expense deleted!",Toast.LENGTH_SHORT).show();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void Display(int position) {
        Expense expense = expenses.get(position);
        Intent showExp = new Intent(MainActivity.this,ShowExpense.class);
        showExp.putExtra(EXPENSE_KEY,expense.getId());
        startActivity(showExp);
    }
}
