package com.gitlab.faerytea.ghapi;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Queue;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

public abstract class AbstractPresenter<ActivityTp extends Activity> {
    private static final String LOG_TAG = "AbstractPresenter";
    private final Queue<WithActivity<ActivityTp>> actions = new ArrayDeque<>();
    protected ActivityTp activity;

    @CallSuper
    public void attach(ActivityTp activity) {
        Log.d(LOG_TAG, "attach() called with: a = [" + activity + "]");
        this.activity = activity;
        while (!actions.isEmpty()) actions.remove().perform(activity);
    }

    @CallSuper
    public void detach() {
        Log.d(LOG_TAG, "detach() called");
        activity = null;
    }

    protected final void withActivity(WithActivity<ActivityTp> call) {
        if (activity != null) call.perform(activity);
        else actions.add(call);
    }

    protected interface WithActivity<ActivityTp> {
        void perform(@NonNull ActivityTp activity);
    }
}
