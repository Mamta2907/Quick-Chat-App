package chaudhari.mamta.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import chaudhari.mamta.chatapp.ModelClass.UsersModel;
import chaudhari.mamta.chatapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    ImageView save;
    CircleImageView setting_image;
    EditText setting_name,setting_status;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String name,email,img;
    Uri setImageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        save = findViewById(R.id.save);
        setting_image = findViewById(R.id.setting_image);
        setting_name = findViewById(R.id.setting_name);
        setting_status = findViewById(R.id.setting_status);



        DatabaseReference reference = database.getReference().child("user").child(Objects.requireNonNull(auth.getUid()));
        StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

         reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                name = (String) snapshot.child("name").getValue();
                setting_name.setText(name);
                email = (String) snapshot.child("email").getValue();
                setting_status.setText(email);
                img = (String) snapshot.child("imageUri").getValue();
                Glide.with(SettingActivity.this).load(img).into(setting_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = setting_name.getText().toString();
                email = setting_status.getText().toString();

                if (setImageURI!=null){
                    storageReference.putFile(setImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                     String finalImageUri = uri.toString();
                                    UsersModel usersModel = new UsersModel(auth.getUid(),name,email,finalImageUri);

                                    reference.setValue(usersModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(SettingActivity.this, "Data successFully Updated", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(SettingActivity.this,home.class));
                                                }
                                                else{
                                                    Toast.makeText(SettingActivity.this, "Something wrong Data not updated...", Toast.LENGTH_SHORT).show();
                                                }
                                                    
                                        }
                                    });
                                }
                            });
                        }
                    });
                }

                else{
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageUri = uri.toString();
                            UsersModel usersModel = new UsersModel(auth.getUid(),name,email,finalImageUri);

                            reference.setValue(usersModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SettingActivity.this, "Data successFully Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SettingActivity.this,home.class));
                                    }
                                    else{
                                        Toast.makeText(SettingActivity.this, "Something wrong Data not updated...", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    });

                }
            }
        });



        setting_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"),10);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10){
            if (data!=null){
                setImageURI= data.getData();
                setting_image.setImageURI(setImageURI);
            }
        }
    }
}