package com.evolve.evolve.EvolveActivities.EvolveAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.Config;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.EvolveDatabase;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.EvolvePreferences;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.ImageManipulator;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.VolleySingleton;
import com.evolve.evolve.EvolveActivities.ImagePreviewActivity;
import com.evolve.evolve.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    EvolvePreferences prefs;

    public GalleryAdapter(Context context, ArrayList<String> file_name) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        filename = file_name;
        database = new EvolveDatabase(context);
        imageManipulator = new ImageManipulator();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth() / 3;
        prefs = new EvolvePreferences(context);
    }

    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.custom_galleryview, viewGroup, false);
        GalleryViewHolder galleryViewHolder = new GalleryViewHolder(view, width);
        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.GalleryViewHolder viewHolder, final int position) {
        Glide.with(context).load(Environment.getExternalStorageDirectory().toString() + "/Evolve/" + filename.get(position)).into(viewHolder.images);
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
                        int exifInfo = 0;
                        String fileName = Environment.getExternalStorageDirectory().toString() + "/Evolve/" + filename.get(position);
                        switch (i) {
                            case 0:
                                try {
                                    exifInfo = imageManipulator.readExifInfo(fileName);
                                    imageManipulator.deleteFromLocal(context, fileName, exifInfo);
                                    filename.remove(position);
                                } catch (Exception e) {
                                }
                                GalleryAdapter.this.notifyDataSetChanged();
                                dialog.dismiss();
                                break;
                            case 1:
                                try {
                                    exifInfo = imageManipulator.readExifInfo(fileName);
                                    deletePermanently(exifInfo, position, fileName);
                                    filename.remove(position);
                                    dialog.dismiss();
                                    GalleryAdapter.this.notifyDataSetChanged();
                                } catch (Exception e) {
                                }
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
                intent.putExtra("filename", filename.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filename.size();
    }

    private void deletePermanently(final int exifInfo, int position, final String filename) {

        String url = Config.apiUrl + "/api/delete/image";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("JsonString", response.toString());
                    imageManipulator.deleteFromLocal(context, filename, exifInfo);
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map params = new HashMap();
                params.put("id", String.valueOf(exifInfo));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("id", String.valueOf(prefs.getId()));
                header.put("access_token", prefs.getAccessToken());
                return header;
            }
        };
        VolleySingleton.getInstance().getRequestQueue().add(stringRequest);

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

