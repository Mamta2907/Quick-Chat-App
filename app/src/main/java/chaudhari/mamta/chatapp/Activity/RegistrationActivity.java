package chaudhari.mamta.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import chaudhari.mamta.chatapp.R;
import chaudhari.mamta.chatapp.ModelClass.UsersModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    EditText register_name, register_email, register_pass, register_cPass;
    TextView tv_signIn;
    CircleImageView profile_image;
    Button btn_signUp;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //public static int IMAGE_PICKER = 4;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri uri;
    String imageURI;

  //  ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        profile_image = findViewById(R.id.profile_image);
        register_name = findViewById(R.id.register_name);
        register_email = findViewById(R.id.register_email);
        register_pass = findViewById(R.id.register_pass);
        register_cPass = findViewById(R.id.register_Cpass);
        tv_signIn = findViewById(R.id.SignIn_tv);
        btn_signUp = findViewById(R.id.btn_signUp);

      /*  galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                profile_image.setImageURI(uri);
            }
        });*/


        tv_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });


        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = register_name.getText().toString();
                String email = register_email.getText().toString();
                String pass = register_pass.getText().toString();
                String cPass = register_cPass.getText().toString();



                if(name.isEmpty()){
                    register_name.setError("Name should not be blank...");
                }
                else if(email.isEmpty()){
                    register_email.setError("Email not be blank...");
                }
                else if(pass.isEmpty()){
                    register_pass.setError("Password should not blank");
                }
                else if(cPass.isEmpty()){
                    register_cPass.setError("Filed is required...");
                }
                else if (!email.matches(emailPattern)){
                    register_email.setError("Enter valid email address");
                }
                else if (!pass.equals(cPass)){
                    register_cPass.setError("Password nor match");
                }
                else{
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(RegistrationActivity.this, "User Created Successfully...", Toast.LENGTH_SHORT).show();

                                DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                                if(uri!=null){
                                    storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                //Toast.makeText(RegistrationActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                                // get the url of that image from storage reference
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri Imageuri) {
                                                       imageURI = Imageuri.toString();

                                                       UsersModel user = new UsersModel(auth.getUid(),name,email,imageURI);
                                                       reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull  Task<Void> task) {

                                                               if(task.isSuccessful()){
                                                                   startActivity(new Intent(RegistrationActivity.this, home.class));
                                                               }
                                                               else{
                                                                   Toast.makeText(RegistrationActivity.this, "Error in Creating a new user", Toast.LENGTH_SHORT).show();
                                                               }
                                                           }
                                                       });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                        else{

                                    // get this below uri from firebase storage after store the default image from laptop...
                                    imageURI = "https://firebasestorage.googleapis.com/v0/b/quickchatapp-b11ee.appspot.com/o/no-profile-picture.jpg?alt=media&token=5c9d0bf5-d9fe-4a7d-a4af-f25c1802ad13";

                                    UsersModel user = new UsersModel(auth.getUid(),name,email,imageURI);
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull  Task<Void> task) {

                                            if(task.isSuccessful()){
                                                startActivity(new Intent(RegistrationActivity.this,home.class));
                                            }
                                            else{
                                                Toast.makeText(RegistrationActivity.this, "Error in Creating a new user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } // if image is not null else complete here



                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }); // task close
                }

            }

        }); // btn close


       profile_image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
           }
       });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10){
            if (data!=null){
                uri= data.getData();
                profile_image.setImageURI(uri);
            }
        }
    }
}