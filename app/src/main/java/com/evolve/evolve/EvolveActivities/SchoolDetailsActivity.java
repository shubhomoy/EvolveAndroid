package com.evolve.evolve.EvolveActivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.evolve.evolve.EvolveActivities.EvolveObjects.School;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.Config;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.VolleySingleton;
import com.evolve.evolve.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class SchoolDetailsActivity extends AppCompatActivity {
    private TextView schoolName;
    private TextView schoolAff;
    private TextView schoolContact;
    private TextView schoolType;
    private TextView schoolDescription;
    private TextView schoolemail;
    private TextView schooladdress;
    private TextView schoolPrincipal;
    private ImageView schoolLogo;
    private Button schoolExtra;
    private static int schoolid;
    private static ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);
        instantiate();
        Intent intent = getIntent();
        schoolid = intent.getIntExtra("id", 0);
        setSchoolDetails(schoolid);
    }

    private void setSchoolDetails(final int schoolid) {
        String url = Config.apiUrl + "/school/" + schoolid;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                try {
                    final School school = gson.fromJson(response.getString("school"), School.class);
                    schoolName.setText(school.name);
                    String affiliations = "";
                    for (int i = 0; i < school.affiliations.size(); i++) {
                        affiliations += school.affiliations.get(i).affiliation + " ";

                    }
                    schoolAff.setText(affiliations);
                    String types = "";
                    for (int i = 0; i < school.types.size(); i++) {
                        types += school.types.get(i).type_name + " ";

                    }
                    schoolType.setText(types);
                    schoolContact.setText(school.contact_no);
                    schoolDescription.setText(school.description);
                    schoolemail.setText(school.email);
                    schooladdress.setText(school.address);
                    schoolPrincipal.setText(school.principal_name);
                    progressDialog.cancel();
                } catch (JSONException e) {

                    progressDialog.cancel();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SchoolDetailsActivity.this);
                builder.setTitle("Connection Failed");
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setSchoolDetails(schoolid);
                    }
                });
                Log.d("option_error", error.toString());
            }
        });
        VolleySingleton.getInstance().getRequestQueue().add(request);
    }

    private void instantiate() {
        schoolName = (TextView) findViewById(R.id.school_name);
        schoolAff = (TextView) findViewById(R.id.school_aff);
        schoolContact = (TextView) findViewById(R.id.school_phone);
        schoolType = (TextView) findViewById(R.id.school_type);
        schoolDescription = (TextView) findViewById(R.id.school_desc);
        schoolemail = (TextView) findViewById(R.id.school_email);
        schooladdress = (TextView) findViewById(R.id.school_add);
        schoolPrincipal = (TextView) findViewById(R.id.school_princ);
        schoolLogo = (ImageView) findViewById(R.id.school_img);
        schoolExtra = (Button) findViewById(R.id.show_extra);
    }
}
