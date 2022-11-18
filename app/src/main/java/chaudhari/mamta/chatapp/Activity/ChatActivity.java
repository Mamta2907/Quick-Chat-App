package chaudhari.mamta.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

import chaudhari.mamta.chatapp.Adapter.MessagesAdapter;
import chaudhari.mamta.chatapp.ModelClass.Messages;
import chaudhari.mamta.chatapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String ReceiverImage,ReceiverUID,ReceiverName,SenderUID;
    CircleImageView profileImage;
    TextView receiverName;
    FirebaseDatabase database;
    FirebaseAuth auth;
    public static String sImage;
    public static String rImage;

    CardView sendBtn;
    EditText edtMsg;

    String senderRoom, receiverRoom;

    RecyclerView messageAdapter;
    ArrayList<Messages> messagesArrayList;

    MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

       ReceiverImage = getIntent().getStringExtra("ReceiverImage");
       ReceiverUID = getIntent().getStringExtra("uid");
       ReceiverName = getIntent().getStringExtra("name");

       messagesArrayList = new ArrayList<>();

       profileImage = findViewById(R.id.receiver_profile);
       receiverName = findViewById(R.id.receiver_name);

       messageAdapter =findViewById(R.id.messageAdapter);
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
       linearLayoutManager.setStackFromEnd(true);
       messageAdapter.setLayoutManager(linearLayoutManager);
       // setstack iska matlb layout reverse me chalega kyuki latest msg niche rheta h

        adapter = new MessagesAdapter(ChatActivity.this,messagesArrayList);
        messageAdapter.setAdapter(adapter);


       sendBtn = findViewById(R.id.send_btn);
       edtMsg = findViewById(R.id.edt_msg);


        Glide.with(ChatActivity.this).load(ReceiverImage).into(profileImage);
        receiverName.setText(ReceiverName);
        //Toast.makeText(this, ""+ReceiverName, Toast.LENGTH_SHORT).show();

        SenderUID = auth.getUid();

        senderRoom = SenderUID+ReceiverUID;
        receiverRoom = ReceiverUID+SenderUID;


        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");


        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                //notify the adapter
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                sImage = snapshot.child("imageUri").getValue().toString();
                rImage = ReceiverImage;
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = edtMsg.getText().toString();
                    
                if(msg.isEmpty()){
                    Toast.makeText(ChatActivity.this, "Please enter some Message", Toast.LENGTH_SHORT).show();
                    return;
                }
                edtMsg.setText("");
                Date date = new Date();

                Messages messages = new Messages(msg,SenderUID,date.getTime());

               database = FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .push()
                                .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                            }
                        });
                    }
                });

            }
        });


    }
}