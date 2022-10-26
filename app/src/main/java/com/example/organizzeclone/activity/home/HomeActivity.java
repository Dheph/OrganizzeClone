package com.example.organizzeclone.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizzeclone.R;
import com.example.organizzeclone.activity.MainActivity;
import com.example.organizzeclone.adapter.AdapterMovimentation;
import com.example.organizzeclone.config.FirebaseConfig;
import com.example.organizzeclone.models.Movimentation;
import com.example.organizzeclone.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private com.example.organizzeclone.databinding.ActivityHomeBinding binding;
    private MaterialCalendarView calendarView;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDataRef, movimentationsDataRef;
    private ValueEventListener userValueEventListener, movimentationsValueEventListener;
    private TextView textWelcome, textTotalValue;
    private String name, monthAndYear;
    private Double totalBalance, totalExpense, totalRevenue;
    private AdapterMovimentation adapterMovimentation;
    private Movimentation movimentation;
    private List<Movimentation> movimentations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = com.example.organizzeclone.databinding.ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle("");
        setSupportActionBar(binding.toolbar);

        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView);
        textWelcome = findViewById(R.id.textWelcome);
        textTotalValue = findViewById(R.id.textTotalValue);

        CalendarDay currentDate = calendarView.getCurrentDate();
        String selectedMonth = String.format("%02d", (currentDate.getMonth() + 1));
        monthAndYear = String.valueOf(selectedMonth + "" + currentDate.getYear());
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String selectedMonth = String.format("%02d", (date.getMonth() + 1));
                monthAndYear = String.valueOf(selectedMonth + "" + date.getYear());

                movimentationsDataRef.removeEventListener(movimentationsValueEventListener);
                getUserMovimentations();
            }
        });

        swipe();


        adapterMovimentation = new AdapterMovimentation(movimentations, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserData();
        getUserMovimentations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_signout:
                firebaseAuth = FirebaseConfig.getFirebaseAuth();
                firebaseAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void getUserData() {
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user.getUid() == null) return;
        DatabaseReference firebase = FirebaseConfig.getDatabaseReference();
        userDataRef = firebase.child("users").child(user.getUid());

        userValueEventListener = userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                name = user.getName();
                totalRevenue = user.getTotalRevenue();
                totalExpense = user.getTotalExpense();
                textWelcome.setText("Olá, " + name);
                totalBalance = user.getTotalRevenue() - user.getTotalExpense();
                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultFormat = decimalFormat.format(totalBalance);
                textTotalValue.setText("R$ " + resultFormat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TESTE ERROR:", "ocorreu um erro ao tentar salvar despesa ");
                Log.d("TESTE ERROR:", error.getMessage());
            }
        });
    }

    public void getUserMovimentations() {
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user.getUid() == null) return;
        DatabaseReference firebase = FirebaseConfig.getDatabaseReference();
        movimentationsDataRef = firebase.child("movimentations").child(user.getUid()).child(monthAndYear);

        movimentationsValueEventListener = movimentationsDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movimentations.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Movimentation movimentation = data.getValue(Movimentation.class);
                    movimentation.setKey(data.getKey());
                    movimentations.add(movimentation);
                }
                adapterMovimentation.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TESTE ERROR:", "ocorreu um erro ao tentar salvar despesa ");
                Log.d("TESTE ERROR:", error.getMessage());
            }
        });
    }

    public void swipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excludeMovimentation(viewHolder);
            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void excludeMovimentation(RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Movimentação da conta");
        alertDialog.setMessage("Você tem certeza que deseja realmente exlcuir essa movimentação ?");
        alertDialog.setCancelable(true);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentation = movimentations.get(position);

                firebaseAuth = FirebaseConfig.getFirebaseAuth();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user.getUid() == null) return;
                DatabaseReference firebase = FirebaseConfig.getDatabaseReference();
                movimentationsDataRef = firebase
                        .child("movimentations")
                        .child(user.getUid())
                        .child(monthAndYear);

                movimentationsDataRef.child(movimentation.getKey()).removeValue();
                adapterMovimentation.notifyItemRemoved(position);
                updateBalance();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                adapterMovimentation.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();

    }

    public void updateBalance() {
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user.getUid() == null) return;
        DatabaseReference firebase = FirebaseConfig.getDatabaseReference();
        userDataRef = firebase.child("users").child(user.getUid());

        if(movimentation.getType().equals("revenue")) {
            totalRevenue = totalRevenue- movimentation.getValue();
            userDataRef.child("totalRevenue").setValue(totalRevenue);
        }
        if(movimentation.getType().equals("expense")) {
            totalExpense = totalExpense - movimentation.getValue();
            userDataRef.child("totalExpense").setValue(totalExpense);
        }


    }
    public void goToRevenue(View view) {
        startActivity(new Intent(this, RevenueActivity.class));
    }

    public void goToExpense(View view) {
        startActivity(new Intent(this, ExpenseActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        userDataRef.removeEventListener(userValueEventListener);
        movimentationsDataRef.removeEventListener(movimentationsValueEventListener);
    }
}