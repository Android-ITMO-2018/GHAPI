package com.gitlab.faerytea.ghapi;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gitlab.faerytea.ghapi.api.User;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import lombok.val;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    private static final String LOG_TAG = Adapter.class.getSimpleName();
    private List<User> users = Collections.emptyList();

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.user, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Log.d(LOG_TAG, "onBindViewHolder() called with: holder = [" + holder + "], i = [" + i + "]");
        val user = users.get(i);
        if (user == null) holder.bind("", "null");
        else holder.bind(user.getUserpic(), user.getLogin());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setData(@NonNull List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView name;

        Holder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
        }

        void bind(@NonNull String link, @NonNull CharSequence username) {
            Log.d("ADAPTER", "bind: link=" + link);
            Picasso.get().load(link)
                    .tag(MainActivity.class)
                    .placeholder(R.color.colorAccent)
                    .error(R.color.colorPrimaryDark)
                    .into(pic);
            name.setText(username);
        }
    }
}
