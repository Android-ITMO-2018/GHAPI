package com.gitlab.faerytea.ghapi.lists.repos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gitlab.faerytea.ghapi.R;
import com.gitlab.faerytea.ghapi.lists.SimpleAdapter;
import com.gitlab.faerytea.ghapi.net.api.Repo;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lombok.Setter;

class Adapter extends SimpleAdapter<Adapter.Holder, Repo> {
    @Setter
    private Listener<Repo> listener;

    @Inject
    public Adapter() {
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.repo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(data.get(position));
    }

    class Holder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView lang;

        Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            lang = itemView.findViewById(R.id.language);
        }

        void bind(Repo repo) {
            if (listener != null)
                itemView.setOnClickListener(v -> listener.apply(v.getContext(), data.get(getAdapterPosition())));
            name.setText(repo.getName());
            lang.setText(repo.getDescription());
        }
    }
}
