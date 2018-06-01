package com.example.chj.design;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.chj.design.utils.Constant;

/**
 * Created by ff on 2018/5/30.
 */

public class SplashActivity extends AppCompatActivity {

    private LottieAnimationView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        view = (LottieAnimationView) findViewById(R.id.lottie_layer_name);
        if (isFirst()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            showAnimation();
        }
    }

    private boolean isFirst() {
        SharedPreferences config = getSharedPreferences(Constant.KEY_CONFIG, MODE_APPEND);
        boolean isFrist = config.getBoolean(Constant.KEY_ISFIRST, false);
        return isFrist;
    }

    private void showAnimation() {
        view.setAnimation("LottieLogo2.json");
        view.loop(false);
        view.playAnimation();
        view.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                SharedPreferences sp = getSharedPreferences(Constant.KEY_CONFIG, MODE_APPEND);
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean(Constant.KEY_ISFIRST, true);
                edit.commit();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
