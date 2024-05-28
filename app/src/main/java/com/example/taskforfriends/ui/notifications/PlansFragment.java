package com.example.taskforfriends.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskforfriends.R;
import com.example.taskforfriends.databinding.FragmentPlansBinding;
import com.example.taskforfriends.databinding.FragmentSettingsBinding;
import com.example.taskforfriends.ui.home.MyRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlansFragment extends Fragment {

    private FragmentPlansBinding binding;
    List<String> tasks = new ArrayList<>();
    String numOfTask = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PlansViewModel plansViewModel =
                new ViewModelProvider(this).get(PlansViewModel.class);

        binding = FragmentPlansBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPlan);
        TextView textView = view.findViewById(R.id.textView6);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference userReference = databaseReference.child(uid);

        //считывание данных из таблицы
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Получаем значение numOf из данных пользователя
                String numOf = dataSnapshot.child("numOfTask").getValue(String.class);
                if (numOf != null) {
                    String[] parts = numOf.split("\\)");
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] += ")"; // Добавляем закрывающую скобку к каждому элементу
                    }
                    tasks = Arrays.asList(parts);
                } else {
                    tasks.add("Вы пока не выполнили ни одного задания!");
                }

                int score = dataSnapshot.child("score").getValue(int.class);
                textView.setText("Баллов: " + score);
                MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(tasks);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Обработка ошибок
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}