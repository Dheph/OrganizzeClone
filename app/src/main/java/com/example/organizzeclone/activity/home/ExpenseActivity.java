package com.example.organizzeclone.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.organizzeclone.R;
import com.example.organizzeclone.config.FirebaseConfig;
import com.example.organizzeclone.helpers.Date;
import com.example.organizzeclone.models.Movimentation;
import com.example.organizzeclone.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ExpenseActivity extends AppCompatActivity {

    private EditText expenseRegisterValueInput;
    private TextInputEditText
            expenseRegisterDateInput,
            expenseRegisterCategoryInput,
            expenseRegisterDescriptionInput;
    private String date, category, description;
    private Double value, totalExpense;
    private FirebaseAuth firebaseAuth;
    private Movimentation movimentation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        getUserData();

        expenseRegisterValueInput = findViewById(R.id.expenseRegisterValueInput);
        expenseRegisterDateInput = findViewById(R.id.expenseRegisterDateInput);
        expenseRegisterDescriptionInput = findViewById(R.id.expenseRegisterDescriptionInput);
        expenseRegisterCategoryInput = findViewById(R.id.expenseRegisterCategoryInput);
        expenseRegisterDateInput.setText(Date.currentDate());
    }

    public void handleSave(View view) {
        date = expenseRegisterDateInput.getText().toString();
        if(date.isEmpty()) {
            Toast.makeText(this, "Ops, para continuar adicione uma data", Toast.LENGTH_SHORT).show();
            return;
        }
        category = expenseRegisterCategoryInput.getText().toString();
        if(category.isEmpty()) {
            Toast.makeText(this, "Ops, para continuar adicione uma categoria", Toast.LENGTH_SHORT).show();
            return;
        }
        description = expenseRegisterDescriptionInput.getText().toString();
        if(description.isEmpty()) {
            Toast.makeText(this, "Ops, para continuar adicione uma descrição", Toast.LENGTH_SHORT).show();
            return;
        }
        String checkValue = expenseRegisterValueInput.getText().toString();
        if(checkValue.isEmpty()) {
            Toast.makeText(this, "Ops, para continuar adicione um valor", Toast.LENGTH_SHORT).show();
            return;
        }
        value = Double.parseDouble(checkValue);

        movimentation = new Movimentation(date, category, "expense", description, value);

        try {

            movimentation.save(date,totalExpense);
            Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e) {
            Toast.makeText(this, "Ocorreu um problema: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getUserData() {
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user.getUid() == null) return;
        DatabaseReference firebase = FirebaseConfig.getDatabaseReference();
        DatabaseReference userDataRef = firebase.child("users").child(user.getUid());

        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                totalExpense = user.getTotalExpense();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TESTE ERROR:", "ocorreu um erro ao tentar salvar despesa ");
                Log.d("TESTE ERROR:", error.getMessage());
            }
        });
    }
}