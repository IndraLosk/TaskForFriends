package com.example.taskforfriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskforfriends.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Start extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    ConstraintLayout root;

    private List<String> emailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        root = findViewById(R.id.root_element);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String savedEmail = prefs.getString("Email", "0");
        if(!savedEmail.equals("0")){
            startActivity(new Intent(Start.this, MainActivity.class));
            finish();
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                showRegisterWindow();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                showSignInWindow();
            }
        });
    }

    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View signInWindow = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(signInWindow);

        final EditText email = signInWindow.findViewById(R.id.emailField);
        final EditText password = signInWindow.findViewById(R.id.passField);

        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    //Toast.makeText(getApplicationContext(), "!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(root, "Почта введена неверно", Snackbar.LENGTH_SHORT).show();
                    showSignInWindow();
                    return;
                }

                if(password.getText().toString().length() < 8){
                    //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    Snackbar.make(root, "Введен неверный пароль", Snackbar.LENGTH_SHORT).show();
                    showSignInWindow();
                    return;
                }

                auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("Email", email.getText().toString());
                                editor.apply();
                                startActivity(new Intent(Start.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Ошибка входа, проверьте введеную почту или пароль!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        AlertDialog builder = dialog.create();
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // Установка цвета для кнопок
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    positiveButton.setTextColor(Color.BLACK);
                }

                Button negativeButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_NEGATIVE);
                if (negativeButton != null) {
                    negativeButton.setTextColor(Color.BLACK);
                }
            }
        });

        builder.show();

    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.register_window, null);
        dialog.setView(registerWindow);

        final EditText email = registerWindow.findViewById(R.id.emailField);
        final EditText password = registerWindow.findViewById(R.id.passField);
        final EditText name = registerWindow.findViewById(R.id.nameField);

        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Проверка введеных данных
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите почту", Snackbar.LENGTH_SHORT).show();
                    showRegisterWindow();
                    return;
                }
                for(int j = 0; j < emailList.toArray().length; j++){
                    if(email == emailList.toArray()[j]){
                        Snackbar.make(root, "Почта уже занята", Snackbar.LENGTH_SHORT).show();
                        showRegisterWindow();
                        return;
                    }
                }
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введите имя", Snackbar.LENGTH_SHORT).show();
                    showRegisterWindow();
                    return;
                }
                if (password.getText().toString().length() < 8) {
                    Snackbar.make(root, "Введите пароль длинее 8 символов", Snackbar.LENGTH_SHORT).show();
                    showRegisterWindow();
                    return;
                }

                //Регестрация пользователя
                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() { //Если все данные введены верно
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User(); //Создание экземпляра класса пользователь и заполнение его данными
                                user.setEmail(email.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPassword(password.getText().toString());
                                user.setScore(0);
                                user.setNumOfTask("");
                                user.setFriends("Никого не найдено");
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).
                                        addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = prefs.edit();
                                                editor.putString("Email", email.getText().toString()); //сохранение email
                                                editor.apply();

                                                startActivity(new Intent(Start.this, MainActivity.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(root, "Ошибка регестрации", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });

            }
        });

        AlertDialog builder = dialog.create();
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // Установка цвета для кнопок
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    positiveButton.setTextColor(Color.BLACK);
                }

                Button negativeButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_NEGATIVE);
                if (negativeButton != null) {
                    negativeButton.setTextColor(Color.BLACK);
                }
            }
        });


        builder.show();
    }
}