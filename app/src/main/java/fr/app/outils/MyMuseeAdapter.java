package fr.app.outils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fr.app.InfoMusee;
import fr.app.R;
import fr.app.model.Musee;

public class MyMuseeAdapter extends RecyclerView.Adapter<MyMuseeAdapter.ViewHolder> {

    private List<Musee> listMusee;
    private OnNoteListener mOnNoteListener;

    public MyMuseeAdapter(List<Musee> listMusee, OnNoteListener onNoteListener) {
        this.listMusee = listMusee;
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.musee_item, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.display(listMusee.get(position));
    }

    @Override
    public int getItemCount() {
        return listMusee.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nom;
        private TextView ville;
        OnNoteListener onNoteListener;

        public ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            nom = (TextView)itemView.findViewById(R.id.nomMusee);
            ville = (TextView)itemView.findViewById(R.id.villeMusee);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        void display(Musee musee) {
            nom.setText(musee.getNom());
            ville.setText(musee.getVille());
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
