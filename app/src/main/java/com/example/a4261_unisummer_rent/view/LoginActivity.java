package com.example.a4261_unisummer_rent.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a4261_unisummer_rent.R;
import com.example.a4261_unisummer_rent.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private TextInputEditText etEmail, etPassword, etUsername;
    private TextInputLayout tilUsername;
    private MaterialButton btnAction;
    private MaterialTextView tvToggle, tvTitle;

    private boolean signupMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        tilUsername = findViewById(R.id.tilUsername);
        btnAction = findViewById(R.id.btnAction);
        tvToggle = findViewById(R.id.tvToggle);
        tvTitle = findViewById(R.id.tvTitle);

        tvToggle.setOnClickListener(v -> toggleMode());
        btnAction.setOnClickListener(v -> onActionClicked());

        // If already logged in, skip
        FirebaseUser current = auth.getCurrentUser();
        if (current != null) {
            goToMain();
        }
    }

    private void toggleMode() {
        signupMode = !signupMode;
        tilUsername.setVisibility(signupMode ? View.VISIBLE : View.GONE);
        btnAction.setText(signupMode ? "Sign Up" : "Log In");
        tvToggle.setText(signupMode ? "Have an account? Log in" : "No account? Sign up");
        tvTitle.setText(signupMode ? "Create account" : "Welcome");
    }

    private void onActionClicked() {
        String email = safe(etEmail);
        String password = safe(etPassword);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            toast("Email and password required");
            return;
        }

        if (signupMode) {
            String username = safe(etUsername);
            if (TextUtils.isEmpty(username)) {
                toast("Username required");
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> {
                        String uid = result.getUser().getUid();
                        User user = new User(uid, username, password);

                        db.collection("users").document(uid).set(user)
                                .addOnSuccessListener(aVoid -> {
                                    toast("Account created!");
                                    goToMain();
                                })
                                .addOnFailureListener(e -> toast("Failed to save user: " + e.getMessage()));
                    })
                    .addOnFailureListener(e -> toast("Sign up failed: " + e.getMessage()));

        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> {
                        toast("Logged in");
                        goToMain();
                    })
                    .addOnFailureListener(e -> toast("Login failed: " + e.getMessage()));
        }
    }

    private String safe(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void toast(@NonNull String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
