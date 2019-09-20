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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {
    boolean backPressed = false;
    EditText etUsername, etPassword;
    SharedPreferences preferences;
    String sharedFullname, sharedUsername, sharedEmail, sharedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etUsername = findViewById(R.id.si_username);
        etPassword = findViewById(R.id.si_password);

        preferences = getSharedPreferences("NewUser", MODE_PRIVATE);
        sharedUsername = preferences.getString("USER_NAME", "Incorrect Username or Password");
        sharedPassword = preferences.getString("PASS_WORD", "Incorrect Username or Password");

        etUsername.setText(sharedUsername);
        etPassword.setText(sharedPassword);

        TextView tvLogin = findViewById(R.id.tv_register);
        SpannableString string = new SpannableString(getResources().getString(R.string.new_user));
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Reset();
                startActivity(new Intent(SignIn.this, SignUp.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        string.setSpan(span, 10, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLogin.setText(string);
        tvLogin.setMovementMethod(LinkMovementMethod.getInstance());
        tvLogin.setHighlightColor(getResources().getColor(R.color.colorAccent));
    }

    public void Login(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(this, "Please enter username and password!", Toast.LENGTH_SHORT).show();
        } else if (username.trim().isEmpty()) {
            Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
        } else if (password.trim().isEmpty()) {
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
        } else if (!(username.equals(sharedUsername) && sharedPassword.equals(password))) {
            Toast.makeText(this, "Username or Password incorrect!", Toast.LENGTH_SHORT).show();
        } else {
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("FULL_NAME", sharedFullname);
//            editor.putString("USER_NAME", username);
//            editor.putString("USER_EMAIL", sharedEmail);
//            editor.putString("PASS_WORD", password);
//            editor.commit();

            Intent profileIntent = new Intent(this, Profile.class);
            startActivity(profileIntent);
            Reset();
        }
    }

    private void Reset() {
        etUsername.setText("");
        etPassword.setText("");
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
