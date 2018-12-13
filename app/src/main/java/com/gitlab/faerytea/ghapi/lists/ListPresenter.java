package com.gitlab.faerytea.ghapi.lists;

import android.widget.Toast;

import com.gitlab.faerytea.ghapi.AbstractPresenter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import lombok.val;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ListPresenter<T> extends AbstractPresenter<ListActivity> {
    public abstract void refresh();

    public abstract void query(@NonNull String query);

    public abstract SimpleAdapter<?, T> adapter();

    public class SimpleCall implements Callback<List<T>> {
        @Override
        public void onResponse(@NonNull Call<List<T>> call, @NonNull Response<List<T>> response) {
            if (!response.isSuccessful() || response.body() == null) {
                final ResponseBody errorBody = response.errorBody();
                final String str;
                try {
                    str = errorBody == null ? null : errorBody.string();
                } catch (IOException e) {
                    onFailure(call, e);
                    return;
                }
                onFailure(call, new Throwable(str));
                return;
            }
            val data = response.body();
            withActivity(a -> {
                a.refresh().setRefreshing(false);
                adapter().setData(data == null ? Collections.emptyList() : data);
            });
        }

        @Override
        public void onFailure(@NonNull Call<List<T>> call, @NonNull Throwable t) {
            withActivity(a -> {
                a.refresh().setRefreshing(false);
                adapter().setData(Collections.emptyList());
                Toast.makeText(
                        a,
                        t.getLocalizedMessage(),
                        Toast.LENGTH_LONG)
                        .show();
            });
        }
    }
}
