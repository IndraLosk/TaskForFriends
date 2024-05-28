package com.example.taskforfriends.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskforfriends.R;
import com.example.taskforfriends.databinding.FragmentHomeBinding;
import com.example.taskforfriends.models.User;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    String[] numOfTaskToBD = {""};
    int[] scoreToDB = {0};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // В вашем Activity или Fragment
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        TextView textView = view.findViewById(R.id.textView6);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<String> data = new ArrayList<>(Arrays.asList(
                "Выучить новый рецепт (5 баллов)", "Выучить пару фокусов (5 баллов)", "Написать стих (5 баллов)", "Научить собирать кубик рубика (5 баллов)", "Прочитать книгу (5 баллов)", "Связать шарф (5 баллов)",
                "Научиться стоять на голове (7 баллов)", "Попробовать себя в роли волонтера (10 баллов)",
                "Научить писать двумя руками (15 баллов)", "Сходить в кемпинг (15 баллов)", "Сходить на концерт (15 баллов)",
                "Научиться играть на любом музыкальном инструменте (20 баллов)", "Покрасить волосы в яркий цвет(20 баллов)",
                "Получить водительские права (100 баллов)", "Посетить другую страну (100 баллов)", "Прыжок с парашютом (300 баллов)", "Сделать татуировку (500 баллов)", "Сделать подарок маме своими руками (бесценно)", ""));

// Создаем адаптер и передаем ему список элементов
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(data);
        recyclerView.setAdapter(adapter);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
//get uid
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        DatabaseReference userReference = databaseReference.child(uid);

        //считывание данных из таблицы
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Получаем значение numOf из данных пользователя
                String numOf = dataSnapshot.child("numOfTask").getValue(String.class);
                int score = dataSnapshot.child("score").getValue(int.class);
                if (numOf != null)
                    numOfTaskToBD[0] = numOf;

                if (score != 0)
                    scoreToDB[0] = score;

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Обработка ошибок
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


        adapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override //Слушатель события адаптера
            public void onItemClick(int position) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();
               int number = 0; //добавить очки
               if(position <= 5)
                   number = 5;
               else if(position == 6)
                   number = 7;
               else if(position == 7)
                   number = 10;
               else if(position <= 10)
                   number = 15;
               else if(position <= 12)
                   number = 20;
               else if(position <= 14)
                   number = 100;
               else if(position == 15)
                   number = 300;
               else if(position == 16)
                   number = 500;
                if(numOfTaskToBD.length == 0) {//добавить сделанное задание
                    numOfTaskToBD[0] = data.get(position);
                    scoreToDB[0] = number;    }
                else {
                    if (!numOfTaskToBD[0].contains(data.get(position))) {
                        numOfTaskToBD[0] += data.get(position);
                        scoreToDB[0] += number;
                    } else {
                        if(position == 17)
                            Toast.makeText(getContext(), "♥️", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(), "Вы уже выполнили " + data.get(position), Toast.LENGTH_SHORT).show();
                    }
                }
                databaseReference.child(uid).child("numOfTask").setValue(numOfTaskToBD[0]);
                databaseReference.child(uid).child("score").setValue(scoreToDB[0]);
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