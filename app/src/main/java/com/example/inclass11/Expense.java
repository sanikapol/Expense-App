//InClass 11
//File Name: Group12_Inclass11
//Sanika Pol
//Snehal Kekane
package com.example.inclass11;

import java.util.Date;
import java.util.HashMap;

public class Expense {
    private String id, name, category,date;
    private Double amount;

    public Expense(String name, String category, Double amount) {
        this.name = name;
        this.category = category;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap toHashMap(){
        HashMap<String, Object> expenseMap = new HashMap();
        expenseMap.put("name",this.name);
        expenseMap.put("category",this.category);
        expenseMap.put("amount",this.amount);
        expenseMap.put("date",this.date);
        return  expenseMap;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
