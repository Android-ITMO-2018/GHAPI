package com.gitlab.faerytea.ghapi.lists.users;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gitlab.faerytea.ghapi.R;
import com.gitlab.faerytea.ghapi.lists.SimpleAdapter;
import com.gitlab.faerytea.ghapi.net.api.User;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import androidx.recyclerview.widget.RecyclerView;
import lombok.Setter;
import lombok.val;

public class Adapter extends SimpleAdapter<Adapter.Holder, User> {
    private static final String LOG_TAG = Adapter.class.getSimpleName();
    @Setter
    private Listener<User> listener;

    @Inject
    Adapter() {
    }

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
        val user = data.get(i);
        if (user == null) holder.bind("", "null");
        else holder.bind(user.getUserpic(), user.getLogin());
    }

    @Override
    public void onDetachedFromRecyclerView(@androidx.annotation.NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Picasso.get().cancelTag(Adapter.class);
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
            if (listener != null)
                itemView.setOnClickListener(v -> listener.apply(v.getContext(), data.get(getAdapterPosition())));
            Picasso.get().load(link)
                    .tag(Adapter.class)
                    .placeholder(R.color.colorAccent)
                    .error(R.color.colorPrimaryDark)
                    .into(pic);
            name.setText(username);
        }
    }
}
