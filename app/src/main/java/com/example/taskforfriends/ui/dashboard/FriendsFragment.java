package com.example.taskforfriends.ui.dashboard;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskforfriends.R;
import com.example.taskforfriends.databinding.FragmentFriendsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;


    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView usersRecyclerView;
    private DatabaseReference databaseReference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FriendsViewModel friendsViewModel =
                new ViewModelProvider(this).get(FriendsViewModel.class);

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        // Получаем ссылку на базу данных Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Находим элементы пользовательского интерфейса
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        usersRecyclerView = view.findViewById(R.id.usersRecyclerView);

        // Устанавливаем обработчик клика на кнопку поиска
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchEditText.getText().toString().trim();
                searchUsers(searchQuery);
            }
        });

        return root;
    }

    private void searchUsers(String searchQuery) {
        Query searchUsersQuery = databaseReference.child("users").orderByChild("username").equalTo(searchQuery);

        searchUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Обработка результатов поиска и отображение пользователей в RecyclerView
                List<String> userList = new ArrayList<>(); // Создаем список для хранения данных о пользователях
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Получение данных о пользователе
                    String userName = userSnapshot.child("name").getValue(String.class);
                    // Добавляем имя пользователя в список
                    userList.add(userName);
                }

                String[] dataArray = userList.toArray(new String[userList.size()]); // преобразование List в массив
                Context context = getContext(); // для фрагмента, или this для активити
                // Создаем новый экземпляр адаптера и устанавливаем его для RecyclerView
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(context, dataArray);
                }

            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибок, если они возникнут
                Log.e(TAG, "Ошибка при получении результатов поиска", databaseError.toException());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}