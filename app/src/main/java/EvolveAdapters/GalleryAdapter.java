package EvolveAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.evolve.evolve.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by vellapanti on 17/9/15.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> filename;
    public GalleryAdapter(Context context,ArrayList<String> file_name){
        inflater=LayoutInflater.from(context);
        filename=file_name;
    }

    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view=inflater.inflate(R.layout.custom_galleryview,viewGroup ,false);
        GalleryViewHolder galleryViewHolder=new GalleryViewHolder(view);
        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.GalleryViewHolder viewHolder, int i) {
        Bitmap bitmap= BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + "/Evolve/" + filename.get(i));
        viewHolder.images.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return filename.size();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder{
        ImageView images;
        public GalleryViewHolder(View itemView) {
            super(itemView);
            images=(ImageView) itemView.findViewById(R.id.gallery_image);
        }
    }
}

