package com.gitlab.faerytea.ghapi;

import com.gitlab.faerytea.ghapi.lists.ListActivity_;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class EspressoExample {
    @Rule
    public ActivityTestRule<ListActivity_> mNotesActivityTestRule =
            new ActivityTestRule<>(ListActivity_.class, true);

    @Test
    public void testRefresh() throws Exception {
        onView(withId(R.id.refresh)).perform(swipeDown())
                .check((view, noViewFoundException) -> {
                    if (!(view instanceof SwipeRefreshLayout
                            && ((SwipeRefreshLayout) view).isRefreshing()))
                        throw noViewFoundException;
                });
    }
}
