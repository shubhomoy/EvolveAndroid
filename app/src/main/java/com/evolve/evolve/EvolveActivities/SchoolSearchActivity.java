package com.evolve.evolve.EvolveActivities;

import android.content.Context;
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

public class SchoolSearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<School> list;
    Context context;
    SchoolSearchAdapter adapter;

    void instantiate() {
        context = this;
        recyclerView = (RecyclerView)findViewById(R.id.search_result_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new SchoolSearchAdapter(context, list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_search);
        instantiate();

        String url = Config.apiUrl+"/schools";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
                Toast.makeText(context, "Connection timeout", Toast.LENGTH_LONG).show();
            }
        });
        VolleySingleton.getInstance().getRequestQueue().add(jsonObjectRequest);
    }
}
