package com.evolve.evolve.EvolveActivities.EvolveUtilities;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubhomoy on 22/9/15.
 */
public class EvolveRequest extends JsonObjectRequest {

    EvolvePreferences prefs;

    public EvolveRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Context context) {
        super(method, url, jsonRequest, listener, errorListener);
        prefs = new EvolvePreferences(context);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> header = new HashMap<String, String>();
        header.put("id", String.valueOf(prefs.getId()));
        header.put("access_token", prefs.getAccessToken());
        return header;
    }
}
