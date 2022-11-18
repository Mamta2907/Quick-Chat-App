package chaudhari.mamta.chatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import chaudhari.mamta.chatapp.ModelClass.Messages;
import chaudhari.mamta.chatapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static chaudhari.mamta.chatapp.Activity.ChatActivity.rImage;
import static chaudhari.mamta.chatapp.Activity.ChatActivity.sImage;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        if(viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new SenderViewHolder(view);
        }
       else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);

        if(holder.getClass() == SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.txt_message.setText(messages.getMessage());

            Glide.with(context).load(sImage).into(viewHolder.circleImageView);
            //sImage are come from chatActivity class vha pe ye static bnayi thi to yha pe import kar diya..
        }
        else{

            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.txt_message.setText(messages.getMessage());

            Glide.with(context).load(rImage).into(viewHolder.circleImageView);
            //rImage are come from chatActivity class vha pe ye static bnayi thi to yha pe import kar diya..
        }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    // for check ki msg sender se ya receiver se to us hisaab se
    // view ko left ya right side me dalna h.

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
            return ITEM_SEND;
        }
        else {
            return ITEM_RECEIVE;
        }
    }

    /* public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }*/

    class SenderViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView txt_message;
        public SenderViewHolder(@NonNull  View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profile_image);
            txt_message = itemView.findViewById(R.id.txtMessages);

        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView txt_message;
        public ReceiverViewHolder(@NonNull  View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_image);
            txt_message = itemView.findViewById(R.id.txtMessages);
        }
    }
}
