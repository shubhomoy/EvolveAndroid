package com.evolve.evolve.EvolveActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.evolve.evolve.EvolveActivities.EvolveAdapters.SchoolSearchAdapter;
import com.evolve.evolve.EvolveActivities.EvolveObjects.School;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.Config;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.VolleySingleton;
import com.evolve.evolve.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SchoolResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<School> list;
    Context context;
    SchoolSearchAdapter adapter;
    ProgressDialog progressDialog;

    void instantiate() {
        context = this;
        recyclerView = (RecyclerView)findViewById(R.id.search_result_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new SchoolSearchAdapter(context, list);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_search);
        instantiate();
        search();
    }

    void search() {
        progressDialog.setMessage("Searching");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url = Config.apiUrl+"/schools";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    list.removeAll(list);
                    list.clear();
                    JSONArray jsonArray = new JSONArray(response.getString("schools"));
                    Gson gson = new Gson();
                    for(int i=0; i<jsonArray.length(); i++) {
                        School school = gson.fromJson(jsonArray.getJSONObject(i).toString(), School.class);
                        list.add(school);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("option", error.toString());
                progressDialog.dismiss();
                retry();
                Toast.makeText(context, "Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
        VolleySingleton.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    void retry() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Retry");
        builder.setMessage("Your connection seems slow. Retry?");
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                search();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
    }
}
