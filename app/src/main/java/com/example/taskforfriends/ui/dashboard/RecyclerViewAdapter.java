package com.example.taskforfriends.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskforfriends.R;
import com.example.taskforfriends.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<String> userList;
    private OnItemClickListener onItemClickListener;


    public RecyclerViewAdapter(Context context, List<String> userList) { //OnUserClickListener listener
        this.context = context;
        this.userList = userList;
    }

    public interface OnItemClickListener {
        void onItemClick(String username);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemuser, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(userList.get(position));

        String username = userList.get(position);
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(username);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;

        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
        }
    }

}


