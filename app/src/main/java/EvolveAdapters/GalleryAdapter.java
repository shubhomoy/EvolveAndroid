package EvolveAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
    float width;
    Context context;

    public GalleryAdapter(Context context,ArrayList<String> file_name){
        this.context = context;
        inflater=LayoutInflater.from(context);
        filename=file_name;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth() / 3;
    }

    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view=inflater.inflate(R.layout.custom_galleryview,viewGroup ,false);
        GalleryViewHolder galleryViewHolder=new GalleryViewHolder(view, width);
        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.GalleryViewHolder viewHolder, final int i) {
        Glide.with(context).load(Environment.getExternalStorageDirectory().toString()+"/Evolve/"+filename.get(i)).into(viewHolder.images);
        viewHolder.images.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Do you want to delete this pic?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file=new File(Environment.getExternalStorageDirectory().toString()+"/Evolve/"+filename.get(i));
                        file.delete();
                        filename.remove(i);
                        GalleryAdapter.this.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return filename.size();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder{
        ImageView images;
        public GalleryViewHolder(View itemView, float width) {
            super(itemView);
            images=(ImageView) itemView.findViewById(R.id.gallery_image);
            images.getLayoutParams().height = (int)width;
            images.getLayoutParams().width = (int)width;
        }
    }
}

