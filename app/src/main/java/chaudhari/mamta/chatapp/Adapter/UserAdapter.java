package chaudhari.mamta.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import chaudhari.mamta.chatapp.Activity.ChatActivity;
import chaudhari.mamta.chatapp.ModelClass.UsersModel;
import chaudhari.mamta.chatapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context home;
    ArrayList<UsersModel> usersModelArrayList;
    public UserAdapter(chaudhari.mamta.chatapp.Activity.home home, ArrayList<UsersModel> usersModelArrayList) {
            this.home = home;
            this.usersModelArrayList = usersModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(home).inflate(R.layout.item_user_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
                UsersModel usersModel = usersModelArrayList.get(position);

                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(usersModel.getUid())){
                    holder.itemView.setVisibility(View.GONE);
                }


                holder.user_name.setText(usersModel.getName());
                holder.user_status.setText("Hey There I'm using this app");

                Glide.with(home).load(usersModel.getImageUri()).into(holder.user_profile);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(home, ChatActivity.class);
                        intent.putExtra("name",usersModel.getName());
                        intent.putExtra("ReceiverImage",usersModel.getImageUri());
                        intent.putExtra("uid",usersModel.getUid());
                        home.startActivity(intent);
                        //Toast.makeText(home, usersModel.getName()+" is clicked..", Toast.LENGTH_SHORT).show();
                    }
                });



    }


    @Override
    public int getItemCount() {
        return usersModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView user_name, user_status;
        CircleImageView user_profile;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.user_status);
            user_profile = itemView.findViewById(R.id.user_image);

        }
    }
}
