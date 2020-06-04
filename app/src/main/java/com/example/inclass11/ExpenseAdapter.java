//InClass 11
//File Name: Group12_Inclass11
//Sanika Pol
//Snehal Kekane
package com.example.inclass11;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder>{

    ArrayList<Expense> expenses;
    private static iExpenseOps expenseOps;

    public ExpenseAdapter(ArrayList<Expense> expenses, iExpenseOps expenseOps) {
        this.expenses = expenses;
        this.expenseOps = expenseOps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.expense,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.tv_expName.setText(expense.getName());
        holder.tv_expAmt.setText("$" + expense.getAmount());
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_expName,tv_expAmt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_expName = (TextView) itemView.findViewById(R.id.tv_expName);
            tv_expAmt = (TextView) itemView.findViewById(R.id.tv_expAmt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expenseOps.Display(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    expenseOps.Delete(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public interface iExpenseOps{
        public void Delete(int position);
        public void Display(int position);
    }
}
