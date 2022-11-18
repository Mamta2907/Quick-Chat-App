package chaudhari.mamta.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import chaudhari.mamta.chatapp.R;

public class LoginActivity extends AppCompatActivity {

    EditText login_email, login_pass;
    TextView tvSignUp;
    Button btnSignIn;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        login_email = findViewById(R.id.login_email);
        login_pass = findViewById(R.id.login_pass);
        tvSignUp = findViewById(R.id.SignUp_tv);
        btnSignIn = findViewById(R.id.btn_signIn);

        //auth = FirebaseAuth.getInstance();

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login_email.getText().toString();
                String pass = login_pass.getText().toString();

                if (email.isEmpty()) {
                    login_email.setError("Email should not blank..");
                } else if (pass.isEmpty()) {
                    login_pass.setError("Pass should not blank");
                } else if (!email.matches(emailPattern)) {
                    login_email.setError("please enter valid email address", getDrawable(R.drawable.ic_person));
                } else if (!(pass.length() >= 6)) {
                    login_pass.setError("password length should be 6", getDrawable(R.drawable.common_google_signin_btn_icon_dark));
                } else {


                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Success!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, home.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Error in login!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Login Failed!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
}