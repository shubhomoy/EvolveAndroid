package com.evolve.evolve.EvolveActivities.EvolveAdapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.EvolveDatabase;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.ImageManipulator;
import com.evolve.evolve.EvolveActivities.ImagePreviewActivity;
import com.evolve.evolve.R;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by vellapanti on 17/9/15.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> filename;
    float width;
    Context context;
    ImageManipulator imageManipulator;
    EvolveDatabase database;
    AlertDialog dialog;

    public GalleryAdapter(Context context, ArrayList<String> file_name) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        filename = file_name;
        database = new EvolveDatabase(context);
        imageManipulator = new ImageManipulator();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth() / 3;
    }

    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.custom_galleryview, viewGroup, false);
        GalleryViewHolder galleryViewHolder = new GalleryViewHolder(view, width);
        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.GalleryViewHolder viewHolder, final int i) {
        Glide.with(context).load(Environment.getExternalStorageDirectory().toString() + "/Evolve/" + filename.get(i)).into(viewHolder.images);
        viewHolder.images.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                LayoutInflater inflater = LayoutInflater.from(context);
                View row = inflater.inflate(R.layout.delete_list_view, null);
                builder.setView(row);

                ListView deletelist = (ListView) row.findViewById(R.id.delete_list);
                String[] delete_array = context.getResources().getStringArray(R.array.delete_list_array);

                ArrayAdapter<String> deleteAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, delete_array);
                deletelist.setAdapter(deleteAdapter);
                deletelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i) {
                            case 0:
                                File file = new File(Environment.getExternalStorageDirectory().toString() + "/Evolve/" + filename.get(i));
                                file.delete();
                                filename.remove(i);
                                try {
                                    int exifInfoid = imageManipulator.readExifInfo(Environment.getExternalStorageDirectory().toString() + "/Evolve/" + filename.get(i));
                                    database.open();
                                    database.deletePicInfo(exifInfoid);
                                    database.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    GalleryAdapter.this.notifyDataSetChanged();
                                    dialog.dismiss();
                                }

                                break;

                            case 1:
                                Toast.makeText(context, "Position " + i, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
                dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        viewHolder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImagePreviewActivity.class);
                intent.putExtra("filename", filename.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filename.size();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView images;

        public GalleryViewHolder(View itemView, float width) {
            super(itemView);
            images = (ImageView) itemView.findViewById(R.id.gallery_image);
            images.getLayoutParams().height = (int) width;
            images.getLayoutParams().width = (int) width;
        }
    }
}

