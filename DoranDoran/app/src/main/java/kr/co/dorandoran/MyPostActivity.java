package kr.co.dorandoran;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * Created by hayoung on 2016-09-07.
 * 내가 쓴 글 보여주는 activity
 * 다른 거 없이 frament만 달아요
 */
public class MyPostActivity extends AppCompatActivity {
    public Toolbar toolbar;

    CoordinatorLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_post);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MyPostFragment f = MyPostFragment.newInstance(0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.my_post_container, f);
        ft.commit();

    }



}
