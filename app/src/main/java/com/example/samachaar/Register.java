package com.example.samachaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    Button Submit;
    ProgressDialog progressDialog;
    TextView alreadyHaveAccount;
    EditText inputemail,inputpassword,inputcnfpassword;
    String emailPattern ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Submit =(Button)  findViewById(R.id.buttonR);
        inputemail=findViewById(R.id.inputEmail);
        inputpassword=findViewById(R.id.lpassword);
        inputcnfpassword=findViewById(R.id.cnfpassword);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perforauth();
            }
        });
//        Submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Register.this,MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    private void perforauth() {
        String email = inputemail.getText().toString();
        String password = inputpassword.getText().toString();
        String cnfpassword = inputcnfpassword.getText().toString();

        if (!email.matches(emailPattern)){
            inputemail.setError("Enter Correct Email");
        } else if (password.isEmpty() || password.length()<8) {
            inputpassword.setError("Password Should be of 8 Characters");
        }else if(password.equals(inputcnfpassword)){
            inputcnfpassword.setError("Password Not Matched");
        }else {
            progressDialog.setMessage("Please Wait Registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(Register.this,"Registration Success",Toast.LENGTH_LONG);
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(Register.this,LoginScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}