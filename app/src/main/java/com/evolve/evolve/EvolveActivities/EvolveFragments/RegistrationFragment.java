package com.evolve.evolve.EvolveActivities.EvolveFragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
public class RegistrationFragment extends Fragment implements View.OnClickListener {


    Button getstarted;
    EditText email;
    EditText mobile_num;
    EvolvePreferences prefs;
    Toolbar toolbar;
    AlertDialog.Builder dialog;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_registration, container, false);
        email = (EditText) v.findViewById(R.id.email);
        mobile_num = (EditText) v.findViewById(R.id.mobile);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Register");
        dialog = new AlertDialog.Builder(getActivity());
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getstarted = (Button) getActivity().findViewById(R.id.submit);
        getstarted.setOnClickListener(this);
        prefs = new EvolvePreferences(getActivity());

    }

    //This function verifies the otp.
// If the otp is verified server sends an id and access token to the client.
    void showOtpDialog() {
        dialog.setTitle("Enter OTP");
        dialog.setMessage(null);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.otp_custom_dialog, null);
        dialog.setView(view);
        final EditText otp = (EditText) view.findViewById(R.id.otp_et);
        dialog.setCancelable(true);
        dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String url = Config.coreUrl + "/auth/verify";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Gson gson = new Gson();
                            User user = gson.fromJson(jsonObject.getString("user"), User.class);
                            prefs.setId(user.id);
                            prefs.setAccessToken(jsonObject.getString("token"));
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.setView(null);
                        dialog.setTitle("Failed");
                        dialog.setMessage("Not a valid OTP");
                        dialog.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialog.create().show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", email.getText().toString());
                        params.put("phone", mobile_num.getText().toString());
                        params.put("otp", otp.getText().toString());
                        return params;
                    }
                };
                VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
            }
        });
        dialog.create().show();
    }

    //Here the e-mail id and phone number is sent to the server and then a response comes
//with a response of e-mail id, phone number and otp which is then initialised to the user class
    @Override
    public void onClick(View v) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registering");
        progressDialog.show();

        String url = Config.coreUrl + "/auth/login";
        Log.d("option", url);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("option", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            Gson gson = new Gson();
                            User user = gson.fromJson(responseObject.getString("data"), User.class);
                            Log.d("option", user.otp);
                            showOtpDialog();
                        } catch (JSONException e) {
                            Log.d("option", e.toString());

                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Connection Timeout", Toast.LENGTH_LONG).show();
                    }
                }) {
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

