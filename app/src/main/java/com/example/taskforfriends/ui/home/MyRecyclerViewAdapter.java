package com.example.taskforfriends.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskforfriends.R;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> items;
    private OnItemClickListener mListener;

    public MyRecyclerViewAdapter(List<String> items) {
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
//        if (viewType == R.layout.divider_item) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.divider_item, parent, false);
//            return new ViewHolder(view);
//        } else {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//            return new ViewHolder(view);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(items.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        // Возвращаем тип разделителя, когда это необходимо
//        if (position == getItemCount() - 1) {
//            return R.layout.divider_item;
//        }
//        return R.layout.list_item;
//    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
