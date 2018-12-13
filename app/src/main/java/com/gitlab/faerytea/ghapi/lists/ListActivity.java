package com.gitlab.faerytea.ghapi.lists;

import android.os.Bundle;
import android.util.Log;

import com.gitlab.faerytea.ghapi.ActivityScope;
import com.gitlab.faerytea.ghapi.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import lombok.Getter;
import lombok.experimental.Accessors;

@EActivity(R.layout.activity_main)
@Accessors(fluent = true)
@ActivityScope
public class ListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ListActivity.class.getSimpleName();
    @Inject
    ListPresenter<?> presenter;
    @ViewById
    RecyclerView recycler;
    @ViewById
    @Getter
    SwipeRefreshLayout refresh;
    @NonConfigurationInstance
    ListActivityComponent component;
    @Extra
    String initialQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
    }

    void component(@Extra @ResolverConfiguration int config) {
        if (component == null) component = ComponentResolver.resolve(config);
    }

    @AfterViews
    void init() {
        Log.d(LOG_TAG, "init() called");
        if (component == null) component = ComponentResolver.resolve(0);
        if (initialQuery == null) initialQuery = "okhttp";
        component.injectInto(this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        presenter.attach(this);
        recycler.setAdapter(presenter.adapter());
        refresh.setOnRefreshListener(presenter::refresh);
        presenter.query(initialQuery);
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy() called");
        super.onDestroy();
        presenter.detach();
    }
}
