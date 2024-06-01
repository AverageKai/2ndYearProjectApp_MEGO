package com.example.mego;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<MyNoteHolder>{
    private Context context;
    private List<NotesDataClass> dataList;
    public NoteAdapter(Context context, List<NotesDataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyNoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_recycler_item, parent, false);
        return new MyNoteHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyNoteHolder holder, int position) {
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        holder.recDesc.setText(dataList.get(position).getDataDesc());
        holder.date.setText(dataList.get(position).getDateID());
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoteDetails.class);
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDataDesc());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());
                intent.putExtra("Day", dataList.get(holder.getAdapterPosition()).getDay());
                intent.putExtra("Month", dataList.get(holder.getAdapterPosition()).getMonth());
                intent.putExtra("Year", dataList.get(holder.getAdapterPosition()).getYear());
                intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("dateID", dataList.get(holder.getAdapterPosition()).getDateID());
                context.startActivity(intent);
            }
        });
    }
    public int getItemCount() {
        return dataList.size();
    }
    public void searchDataList(ArrayList<NotesDataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}
class MyNoteHolder extends RecyclerView.ViewHolder{
    TextView recTitle, recDesc,date;
    CardView recCard;
    public MyNoteHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.date);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        recTitle = itemView.findViewById(R.id.recTitle);
    }
}
