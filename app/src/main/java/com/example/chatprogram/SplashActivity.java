package com.example.chatprogram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.chatprogram.model.UserModel;
import com.example.chatprogram.utils.AndoridUtil;
import com.example.chatprogram.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(getIntent().getExtras()!=null){
            //from notification

            String userId = getIntent().getExtras().getString("userId");
            try {
                FirebaseUtil.allUserCollectionReference().document(userId).get()
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                UserModel model = task.getResult().toObject(UserModel.class);

                                Intent mainIntent = new Intent(this, MainActivity.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(mainIntent);

                                Intent intent = new Intent(this, ChatActivity.class);
                                AndoridUtil.passUserModelAsIntent(intent, model);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        });
            } catch (Exception e){
                AndoridUtil.showToast(this, "ошибка перехода");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(FirebaseUtil.isLoggedIn()){
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }else{
                            startActivity(new Intent(SplashActivity.this, LoginPhoneNumberActivity.class));
                        }
                        finish();
                    }
                }, 1000);
            }


        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(FirebaseUtil.isLoggedIn()){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }else{
                        startActivity(new Intent(SplashActivity.this, LoginPhoneNumberActivity.class));
                    }
                    finish();
                }
            }, 1000);
        }
    }
}