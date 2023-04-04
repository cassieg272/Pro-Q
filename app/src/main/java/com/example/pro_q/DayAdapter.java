package com.example.pro_q;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<DayModel> dayModels;

    public DayAdapter(Context context, ArrayList<DayModel> dayModels, RecyclerViewInterface recyclerViewInterface) {
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

        ArrayList<TaskModel> afternoonTaskArr = dayModels.get(position).afternoonTaskArr;
        Log.d("TAG", "test array: "+afternoonTaskArr);
        ArrayList<TaskModel> morningTaskArr = dayModels.get(position).morningTaskArr;
        Log.d("TAG", "test array: "+morningTaskArr);

        ArrayList<TaskModel> eveningTaskArr = dayModels.get(position).morningTaskArr;
        Log.d("TAG", "test arr: "+eveningTaskArr);

        createBtn(morningTaskArr, holder.morningTask);
        createBtn(afternoonTaskArr, holder.afternoonTask);
        createBtn(eveningTaskArr, holder.eveningTask);
        holder.tasksLabel.setText("Tasks for " + dayModels.get(position).getDay());
    }

    private void createBtn(ArrayList<TaskModel> arrayList, LinearLayout layout) {
        for (TaskModel task : arrayList) {
            if (arrayList == null) {
                return;
            }
            Button taskBtn = new Button(context);
            taskBtn.setText(task.getTaskName());
            taskBtn.setWidth(100);
            taskBtn.setHeight(100);
            taskBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            taskBtn.setBackgroundColor(taskBtn.getContext().getResources().getColor(R.color.grey));
            Log.d("TAG", "onBindViewHolder: " + task.getTaskName());
            layout.addView(taskBtn);
        }
    }

    @Override
    public int getItemCount() {
        //the recycler view just wants to know the number of items you want displayed
        return dayModels.size();
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

