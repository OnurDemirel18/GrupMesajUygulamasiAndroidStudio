package com.example.groupmessageapp.creategroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupmessageapp.GroupModel;
import com.example.groupmessageapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder> {
    List<GroupModel> groupModelList;

    public GroupAdapter(List<GroupModel> groupModelList) {
        this.groupModelList = groupModelList;
    }


    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        GroupHolder groupHolder = new GroupHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false));
        return groupHolder;
    }

    @Override
    public void onBindViewHolder(GroupHolder holder, int position) {
        GroupModel groupModel = groupModelList.get(position);
        holder.setData(groupModel);

    }

    @Override
    public int getItemCount() {
        return groupModelList.size();
    }

    public class GroupHolder extends RecyclerView.ViewHolder {
        TextView groupname, groupexplanation;
        ImageView groupimage;
        public GroupHolder(View itemView) {
            super(itemView);
            groupname = itemView.findViewById(R.id.item_DefaultGroupName);
            groupexplanation = itemView.findViewById(R.id.item_DefaultGroupExp);
            groupimage = itemView.findViewById(R.id.item_DefaultGroupImage);
        }

        public void setData(GroupModel groupModel) {
            groupname.setText(groupModel.getGroupname());
            groupexplanation.setText(groupModel.getGroupexplanation());

            if(groupModel.getGroupImage() != null){
                Picasso.get().load(groupModel.getGroupImage()).into(groupimage);
            }
        }
    }
}

