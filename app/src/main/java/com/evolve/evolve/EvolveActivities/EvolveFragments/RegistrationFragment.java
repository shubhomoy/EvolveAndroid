package com.evolve.evolve.EvolveActivities.EvolveFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.evolve.evolve.EvolveActivities.EvolveObjects.User;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.Config;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.EvolvePreferences;
import com.evolve.evolve.EvolveActivities.MainActivity;
import com.evolve.evolve.R;

import org.json.JSONException;
import org.json.JSONObject;

import com.evolve.evolve.EvolveActivities.EvolveUtilities.VolleySingleton;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener{


    Button getstarted;
    EditText email;
    EditText mobile_num;
    EvolvePreferences prefs;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_registration, container, false);
        email=(EditText)v.findViewById(R.id.email);
        mobile_num=(EditText)v.findViewById(R.id.mobile);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getstarted= (Button) getActivity().findViewById(R.id.submit);
        getstarted.setOnClickListener(this);
        prefs = new EvolvePreferences(getActivity());

    }
//This function verifies the otp.
// If the otp is verified server sends an id and access token to the client.
    void showOtpDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.otp_custom_dialog, null);
        dialog.setView(view);
        final EditText otp=(EditText)view.findViewById(R.id.otp_et);
        view.findViewById(R.id.otp_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url= Config.apiUrl+"/api/users/verify";
                StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("option", response);
                            JSONObject jsonObject=new JSONObject(response);
                            Gson gson =new Gson();
                            User user=gson.fromJson(jsonObject.getString("data"),User.class);
                            prefs.setId(user.id);
                            prefs.setAccessToken(user.access_token);
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("option", error.toString());
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<String, String>();
                        params.put("email",email.getText().toString());
                        params.put("phone",mobile_num.getText().toString());
                        params.put("otp",otp.getText().toString());
                        return params;
                    }
                };
                VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
            }
        });
        dialog.setCancelable(false);
        dialog.create().show();
    }
//Here the e-mail id and phone number is sent to the server and then a response comes
//with a response of e-mail id, phone number and otp which is then initialised to the user class
    @Override
    public void onClick(View v) {
        String url = Config.apiUrl+"/api/users/login";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            Gson gson = new Gson();
                            User user = gson.fromJson(responseObject.getString("data"), User.class);
                            Log.d("option", user.otp);
                            showOtpDialog();
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("option", error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map params = new HashMap();
                params.put("email", email.getText().toString());
                params.put("phone", mobile_num.getText().toString());
                return params;
            }
        };
        VolleySingleton.getInstance().getRequestQueue().add(jsonObjectRequest);

    }

    }

