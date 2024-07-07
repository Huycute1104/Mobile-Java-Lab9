package com.example.roomdatabase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder> {
    private Context context;
    private List<Person> mPersonList;

    public PersonAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_item,
                viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.firstName.setText(mPersonList.get(position).getFirstName());
        holder.lastName.setText(mPersonList.get(position).getLastName());
    }

    @Override
    public int getItemCount() {
        if(mPersonList == null){
            return 0;
        }
        return mPersonList.size();
    }

    public void setTasks(List<Person> personList){
        mPersonList = personList;
        notifyDataSetChanged();
    }

    public List<Person> getTasks(){
        return mPersonList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView firstName, lastName;
        ImageView editImage;

        MyViewHolder(@NonNull final View itemView){
            super(itemView);

            firstName = itemView.findViewById(R.id.tvFirstName);
            lastName = itemView.findViewById(R.id.tvLastName);

            editImage = itemView.findViewById(R.id.ivEdit);
            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int elementId = mPersonList.get(getAdapterPosition()).getUid();
                    Intent i = new Intent(context, EditPersonActivity.class);
                    i.putExtra(Constants.UPDATE_Person_Id, elementId);
                    context.startActivity(i);
                }
            });
        }
    }
}
