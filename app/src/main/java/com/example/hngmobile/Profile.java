package com.example.hngmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Welcome!");

        SharedPreferences preferences = getSharedPreferences("NewUser", MODE_PRIVATE);
        String sharedFullname = preferences.getString("FULL_NAME", "Incorrect Username or Password");
        String sharedUsername = preferences.getString("USER_NAME", "Incorrect Username or Password");
        String sharedEmail = preferences.getString("USER_EMAIL", "Incorrect Username or Password");

        TextView tv_fullname = findViewById(R.id.tv_fullname);
        TextView tv_username = findViewById(R.id.tv_username);
        TextView tv_email = findViewById(R.id.tv_email);

        tv_fullname.setText(sharedFullname);
        tv_email.setText(sharedEmail);
        tv_username.setText(new StringBuilder().append(getResources()
                .getString(R.string.welcome_message)).append(" ").append(sharedUsername).toString());

        TextView tvLogout = findViewById(R.id.tv_logout);
        SpannableString string = new SpannableString(getResources().getString(R.string.logout));
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(Profile.this, SignIn.class));
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        string.setSpan(span, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLogout.setText(string);
        tvLogout.setMovementMethod(LinkMovementMethod.getInstance());
        tvLogout.setHighlightColor(getResources().getColor(R.color.colorAccent));
    }
}
