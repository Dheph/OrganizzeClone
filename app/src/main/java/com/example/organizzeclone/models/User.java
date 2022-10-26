package com.example.organizzeclone.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.organizzeclone.config.FirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

public class User {
    private FirebaseAuth firebaseAuth;
    private String name, email, password;
    private Double totalExpense = 0.00, totalRevenue = 0.00;


    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void save() {
        if (getUid() == null) throw new Error("Usuário não encontrado!");
        DatabaseReference firebase = FirebaseConfig.getDatabaseReference();
        firebase.child("users")
                .child(getUid())
                .setValue(this);
    }

    @Exclude
    public String getUid() {
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user.getUid() != null) {
            return user.getUid();
        }
        return null;
    }

    @Exclude
    public DatabaseReference getUserDataRef() {
        if (getUid() == null) throw new Error("Usuário não encontrado!");
        DatabaseReference firebase = FirebaseConfig.getDatabaseReference();
        DatabaseReference userDataRef = firebase.child("users").child(getUid());
        return userDataRef;
    }

//    @Exclude
//    public User getUserData() {
//        final User[] userResponse = {new User()};
//        DatabaseReference userDataRef = getUserDataRef();
//        userDataRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user = snapshot.getValue(User.class);
//                userResponse[0] = user;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("TESTE ERROR:", "ocorreu um erro ao tentar salvar despesa ");
//                Log.d("TESTE ERROR:", error.getMessage());
//                return;
//            }
//        });
//        return userResponse[0];
//    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double value) {
        if (value == null) return;
        DatabaseReference userDataRef = getUserDataRef();
        userDataRef.child("totalExpense").setValue(value);
        this.totalExpense = value;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double value) {
        if (value == null) return;
        DatabaseReference userDataRef = getUserDataRef();
        userDataRef.child("totalRevenue").setValue(value);
        this.totalRevenue = value;
    }

//    public Double getTotalBalance() { return totalBalance; }
//
//    public void setTotalBalance(Double value) {
////        if (value == null) return;
////        DatabaseReference userDataRef = getUserDataRef();
////        userDataRef.child("totalBalance").setValue(value);
//        this.totalBalance = value;
//    }

    public void checkName() {
        DatabaseReference userDataRef = getUserDataRef();
        userDataRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                System.out.println("NAME INTO: " + user.name);
                name = user.name;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TESTE ERROR:", "ocorreu um erro ao tentar salvar despesa ");
                Log.d("TESTE ERROR:", error.getMessage());
            }
        });
    }

    public String getName() {
        checkName();
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
