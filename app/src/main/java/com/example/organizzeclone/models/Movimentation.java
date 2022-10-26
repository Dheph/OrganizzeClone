package com.example.organizzeclone.models;

import com.example.organizzeclone.config.FirebaseConfig;
import com.example.organizzeclone.helpers.Date;
import com.google.firebase.database.DatabaseReference;

public class Movimentation {
    private String date;
    private String category;
    private String type;
    private String description;



    private String key;
    private Double value;

    public Movimentation() {
    }

    public Movimentation(String date, String category, String type, String description, Double value) {
        this.date = date;
        this.category = category;
        this.description = description;
        this.type = type;
        this.value = value;
    }

    public void save(String saveDate, Double currentValue) {
        User user = new User();
        String userId = user.getUid();
        if (userId == null)
            throw new Error("Tempo de sessão expirado tente deslogar e logar novamente!");
        String converterSaveDate = Date.monthlyAndYear(saveDate);
        DatabaseReference firebase = FirebaseConfig.getDatabaseReference();
        firebase.child("movimentations")
                .child(userId)
                .child(converterSaveDate)
                .push()
                .setValue(this);

        if (this.type == "expense") {
            user.setTotalExpense((this.value + currentValue));
        }
        if (this.type == "revenue") {
            user.setTotalRevenue(this.value + currentValue);
        }

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Double getValue() {
        return value;
    }


}
