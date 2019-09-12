package fr.app.outils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import fr.app.R;

public class MyPhotoAdapter extends RecyclerView.Adapter<MyPhotoAdapter.ViewHolder> {

    List<Bitmap> bmps;

    public MyPhotoAdapter(List<Bitmap> bmps) { this.bmps = bmps; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.musee_photo_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.display(bmps.get(position));
    }

    @Override
    public int getItemCount() { return bmps.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView photo;

        @SuppressLint("WrongViewCast")
        public ViewHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.imageMusee);
        }

        void display(Bitmap bmp) {
            photo.setImageBitmap(bmp);
        }
    }
}
