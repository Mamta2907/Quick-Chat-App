package chaudhari.mamta.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import chaudhari.mamta.chatapp.ModelClass.UsersModel;
import chaudhari.mamta.chatapp.R;
import chaudhari.mamta.chatapp.Adapter.UserAdapter;

public class home extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<UsersModel> usersModelArrayList;
    ImageView img_logOut,img_setting;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        usersModelArrayList = new ArrayList<>();

        DatabaseReference reference = database.getReference().child("user");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            UsersModel usersModel = dataSnapshot.getValue(UsersModel.class);

                            usersModelArrayList.add(usersModel);
                    }
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        img_logOut = findViewById(R.id.img_logOut);
        img_setting = findViewById(R.id.img_setting);

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(home.this,usersModelArrayList);
        mainUserRecyclerView.setAdapter(adapter);


        img_setting.setOnClickListener(view -> startActivity(new Intent(home.this,SettingActivity.class)));

        img_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dialog dialog = new Dialog(home.this,R.style.Dialog);
               // dialog.setTitle("You want to log Out");
                dialog.setContentView(R.layout.dialog_layout);
                Button yesBtn, noBtn;

                yesBtn = dialog.findViewById(R.id.yes_btn);
                noBtn = dialog.findViewById(R.id.no_btn);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                     //   auth.signOut();
                        startActivity(new Intent(home.this, LoginActivity.class));
                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });


        if(auth.getCurrentUser() == null){
            startActivity(new Intent(home.this, RegistrationActivity.class));
        }




    }

}