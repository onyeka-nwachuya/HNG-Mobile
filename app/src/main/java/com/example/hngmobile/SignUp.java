package com.example.hngmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    EditText etFullname, etEmail, etUsername, etPassword;
    boolean backPressed= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etFullname = findViewById(R.id.su_fullname);
        etUsername = findViewById(R.id.su_username);
        etEmail = findViewById(R.id.su_email);
        etPassword = findViewById(R.id.su_password);

        TextView tvLogin = findViewById(R.id.tv_login);
        SpannableString string = new SpannableString(getResources().getString(R.string.old_user));
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(SignUp.this, SignIn.class));
                Reset();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        string.setSpan(span, 25, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLogin.setText(string);
        tvLogin.setMovementMethod(LinkMovementMethod.getInstance());
        tvLogin.setHighlightColor(getResources().getColor(R.color.colorAccent));
    }

    public void RegisterUser(View view) {
        String fullname = etFullname.getText().toString();
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        SharedPreferences preferences = getSharedPreferences("NewUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (fullname.trim().isEmpty()) {
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
        } else if (username.trim().isEmpty()) {
            Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
        } else if (email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address!", Toast.LENGTH_SHORT).show();
        } else if (password.trim().isEmpty() || password.length() < 6) {
            Toast.makeText(this, "Password length cannot be less than 6!", Toast.LENGTH_SHORT).show();
        } else {
            editor.putString("FULL_NAME", fullname);
            editor.putString("USER_NAME", username);
            editor.putString("USER_EMAIL", email);
            editor.putString("PASS_WORD", password);
            editor.commit();

            Intent profileIntent = new Intent(this, Profile.class);
            startActivity(profileIntent);
            Reset();
        }
    }

    public void Reset() {
        etFullname.setText("");
        etEmail.setText("");
        etUsername.setText("");
        etPassword.setText("");
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);
                if (!rect.contains((int) event.getRawX(), (int) event.getRawX())) {
                    view.clearFocus();
                    InputMethodManager manager =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (backPressed) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(this, "Press the BACK button again to exit!", Toast.LENGTH_SHORT).show();
            backPressed = true;
        }
        Runnable exitRunnable = new Runnable() {
            @Override
            public void run() {
                backPressed = false;
            }
        };
        new Handler().postDelayed(exitRunnable, 2000);
    }
}
