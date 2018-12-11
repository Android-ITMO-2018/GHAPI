package com.gitlab.faerytea.ghapi;

import android.os.Bundle;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.ViewById;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    @NonConfigurationInstance
    Presenter presenter;
    @ViewById
    RecyclerView recycler;
    @ViewById
    SwipeRefreshLayout refresh;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        Log.d(LOG_TAG, "init() called");
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        if (presenter == null) presenter = new Presenter();
        presenter.attach(this);
        refresh.setOnRefreshListener(presenter::refresh);
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy() called");
        super.onDestroy();
        presenter.detach();
        Picasso.get().cancelTag(MainActivity.class);
    }
}
