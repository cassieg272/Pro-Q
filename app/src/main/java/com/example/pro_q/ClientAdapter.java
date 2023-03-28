package com.example.pro_q;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<ClientModel> clientModelList;
    public ClientAdapter(Context context, ArrayList<ClientModel> clientModelList, RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.clientModelList = clientModelList;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public ClientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this is where you inflate the layout (giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_result, parent, false);
        return new ClientAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientAdapter.MyViewHolder holder, int position) {
    //assigning values to the views we created in the search_result layout file
    //based on the position of the recycler view
        holder.cltName.setText(clientModelList.get(position).getFullName());
        holder.cltAddress.setText(clientModelList.get(position).getAddress());
        holder.cltID.setText(clientModelList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        //the recycler view just wants to know the number of items you want displayed
        return clientModelList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabbing the views from search_result layout file (kinda like onCreate method)
        TextView cltName, cltAddress, cltID;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            cltName = itemView.findViewById(R.id.cltName);
            cltID = itemView.findViewById(R.id.cltID);
            cltAddress = itemView.findViewById(R.id.cltAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
