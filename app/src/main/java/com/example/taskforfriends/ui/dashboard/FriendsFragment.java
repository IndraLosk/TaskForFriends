package com.example.taskforfriends.ui.dashboard;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
//import android.text.TextWatcher;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskforfriends.R;
import com.example.taskforfriends.databinding.FragmentFriendsBinding;
import com.example.taskforfriends.ui.home.MyRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;
    private RecyclerView recyclerView;
    private List<String> userList = new ArrayList<>(); //все
    private List<String> myFriendsList = new ArrayList<>(); //друзья
    private List<String> filteredUserList = new ArrayList<>(); // во время поиска

    String[] friendsToBD = {""};
//    private RecyclerViewAdapter adapter;
    private RecyclerViewAdapter userAdapter;

    private EditText searchEditText;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FriendsViewModel friendsViewModel =
                new ViewModelProvider(this).get(FriendsViewModel.class);

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchEditText = view.findViewById(R.id.searchEditText);

        //все пользователи
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    userList.add(name);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FragmentFriend", "Failed to read value.", databaseError.toException());
            }
        });


        //считывание данных из таблицы
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference userReference = databaseReference.child(uid);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String friends = dataSnapshot.child("friends").getValue(String.class);
                myFriendsList = Arrays.asList(friends.split(" "));
                if (!friends.equals("Никого не найдено")) {
                    friendsToBD = myFriendsList.toArray(new String[0]);
                }
                else {
                    friendsToBD[0] = "";
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Обработка ошибок
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(myFriendsList);
        recyclerView.setAdapter(adapter);
        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredUserList.clear();
                String query = searchEditText.getText().toString().toLowerCase(); //считывание введенного текста
                if (query.isEmpty()) { //Если ничего не введено показывать список друзей
                    filteredUserList.clear();
                    MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(myFriendsList);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    for (String names : userList) { //Иначе поиск имен в общем списке
                        if (names.toLowerCase().contains(query)) {
                            filteredUserList.add(names);
                        }
                    }
                    MyRecyclerViewAdapter adapterFilter = new MyRecyclerViewAdapter(filteredUserList);
                    recyclerView.setAdapter(adapterFilter);
                    adapterFilter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) { //добавление друзей в БД
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();
                            if(friendsToBD[0].length() == 0) {
                                friendsToBD[0] = filteredUserList.get(position) +"\n";
                                Toast.makeText(getContext(), "Друг добавлен!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (!friendsToBD[0].contains(filteredUserList.get(position))) {
                                    friendsToBD[0] += filteredUserList.get(position) + "\n";
                                    Toast.makeText(getContext(), "Друг добавлен!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Вы уже дружите!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            databaseReference.child(uid).child("friends").setValue(friendsToBD[0] + " ");
                        }
                    });
                }
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