package com.example.taskforfriends;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.taskforfriends.databinding.ActivityStartBinding;
import com.example.taskforfriends.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;
import java.util.UUID;

public class Start extends AppCompatActivity {
    private ActivityStartBinding binding;

    Button btnSignIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnSignIn = binding.btnSignIn;
        btnRegister = binding.btnRegister;

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnRegister.setOnClickListener(w -> showRegisterWindow());

        btnSignIn.setOnClickListener(w -> showSignInWindow());
    }

    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View signInWindow = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(signInWindow);

        final EditText email = signInWindow.findViewById(R.id.emailField);
        final EditText password = signInWindow.findViewById(R.id.passField);

        dialog.setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.dismiss());

        dialog.setPositiveButton("Войти", (dialogInterface, i) -> {
            if(TextUtils.isEmpty(email.getText().toString())){
                //Toast.makeText(getApplicationContext(), "!", Toast.LENGTH_SHORT).show();
                Snackbar.make(binding.getRoot(), "Почта введена неверно", Snackbar.LENGTH_SHORT).show();
                showSignInWindow();
                return;
            }

            if(password.getText().toString().length() < 8){
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                Snackbar.make(binding.getRoot(), "Введен неверный пароль", Snackbar.LENGTH_SHORT).show();
                showSignInWindow();
                return;
            }

            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        startActivity(new Intent(Start.this, MainActivity.class));
                        finish();
                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Ошибка входа, проверьте введеную почту или пароль!", Toast.LENGTH_SHORT).show());
        });

        AlertDialog builder = dialog.create();
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // Установка цвета для кнопок
        builder.setOnShowListener(dialogInterface -> {
            Button positiveButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
            if (positiveButton != null) {
                positiveButton.setTextColor(Color.BLACK);
            }

            Button negativeButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_NEGATIVE);
            if (negativeButton != null) {
                negativeButton.setTextColor(Color.BLACK);
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

        dialog.setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.dismiss());

        dialog.setPositiveButton("Готово", (dialogInterface, i) -> {
            if (TextUtils.isEmpty(email.getText().toString())) {
                Snackbar.make(binding.getRoot(), "Введите почту", Snackbar.LENGTH_SHORT).show();
                showRegisterWindow();
                return;
            }
            if (TextUtils.isEmpty(name.getText().toString())) {
                Snackbar.make(binding.getRoot(), "Введите имя", Snackbar.LENGTH_SHORT).show();
                showRegisterWindow();
                return;
            }
            if (password.getText().toString().length() < 8) {
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                Snackbar.make(binding.getRoot(), "Введите пароль длинее 8 символов", Snackbar.LENGTH_SHORT).show();
                showRegisterWindow();
                return;
            }

            //Регистрация пользователя
            auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        User user = new User();

                        user.setUid(UUID.randomUUID().toString());
                        user.setEmail(email.getText().toString());
                        user.setName(name.getText().toString());
                        user.setPassword(password.getText().toString());
                        user.setScore(0);

                        users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user).
                                addOnSuccessListener(unused -> {
                                    startActivity(new Intent(Start.this, MainActivity.class));
                                    finish();
                                }).addOnFailureListener(e -> Snackbar.make(binding.getRoot(), "Ошибка регистрации", Snackbar.LENGTH_SHORT).show());
                    });

        });

        AlertDialog builder = dialog.create();
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // Установка цвета для кнопок
        builder.setOnShowListener(dialogInterface -> {
            Button positiveButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
            if (positiveButton != null) {
                positiveButton.setTextColor(Color.BLACK);
            }

            Button negativeButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_NEGATIVE);
            if (negativeButton != null) {
                negativeButton.setTextColor(Color.BLACK);
            }
        });


        builder.show();
    }
}