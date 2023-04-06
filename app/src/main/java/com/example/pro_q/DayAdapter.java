package com.example.pro_q;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    DayModel[] dayModels;

    public DayAdapter(Context context, DayModel[] dayModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.dayModels = dayModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this is where you inflate the layout (giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewpager_item_day_dashboard, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //assigning values to the views we created in the search_result layout file
        //based on the position of the recycler view
        DayModel currentDay = dayModels[position];
        String day = currentDay.getDay();
        holder.tasksLabel.setText("Tasks for " + day);

        Log.d("TAG", "test array: "+day+" aft" + currentDay.getAllDayTask()[1]);
        Log.d("TAG", "test array: "+day+" mor" + currentDay.getAllDayTask()[0]);
        Log.d("TAG", "test array: "+day+" eve" +  currentDay.getAllDayTask()[2]);
//        createBtn(currentDay.getAllDayTask()[0], holder.morningTask);
//        createBtn(currentDay.getAllDayTask()[1], holder.afternoonTask);
//        createBtn(currentDay.getAllDayTask()[2], holder.eveningTask);
    }

    private void createBtn(ArrayList<TaskModel> arrayList, LinearLayout layout) {
        for (TaskModel task : arrayList) {
            Button taskBtn = new Button(context);
            taskBtn.setText(task.getTaskName());
            taskBtn.setTextColor(taskBtn.getContext().getResources().getColor(R.color.white));
            taskBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "clicked btn", Toast.LENGTH_LONG).show();
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(5, 5, 5, 10);
            taskBtn.setLayoutParams(params);
            taskBtn.setBackgroundColor(taskBtn.getContext().getResources().getColor(R.color.purple_200));
            taskBtn.setTextColor(taskBtn.getContext().getResources().getColor(R.color.white));
            layout.addView(taskBtn);
        }
    }

    @Override
    public int getItemCount() {
        //the recycler view just wants to know the number of items you want displayed
        return dayModels.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from search_result layout file (kinda like onCreate method)
        TextView tasksLabel;
        LinearLayout morningTask, afternoonTask, eveningTask;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tasksLabel = itemView.findViewById(R.id.tasksLabel);
            morningTask = itemView.findViewById(R.id.morningTaskContainer);
            afternoonTask = itemView.findViewById(R.id.afternoonTaskContainer);
            eveningTask = itemView.findViewById(R.id.eveningTaskContainer);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (recyclerViewInterface!=null){
//                        int position = getAdapterPosition();
//                        if(position!=RecyclerView.NO_POSITION){
//                            recyclerViewInterface.onItemClick(position);
//                        }
//                    }
//                }
//            });
        }
    }
}

