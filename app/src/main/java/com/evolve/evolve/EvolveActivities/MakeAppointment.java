package com.evolve.evolve.EvolveActivities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.Config;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.VolleySingleton;
import com.evolve.evolve.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by shubhomoy on 12/12/15.
 */
public class MakeAppointment extends AppCompatActivity {
    EditText nameEt;
    EditText phoneEt;
    EditText emailEt;
    EditText dateEt;
    EditText descriptionEt;
    Toolbar toolbar;
    Calendar calendar;
    int year, month, day;
    int docId, clinicId;

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            dateEt.setText(String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day));

        }
    };

    void instantiate() {
        nameEt = (EditText)findViewById(R.id.name);
        phoneEt = (EditText)findViewById(R.id.phone);
        dateEt = (EditText)findViewById(R.id.date);
        emailEt = (EditText)findViewById(R.id.email_et);
        descriptionEt = (EditText)findViewById(R.id.problem);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Make Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        docId = getIntent().getIntExtra("docId", 0);
        clinicId = getIntent().getIntExtra("clinicId", 0);
    }

    boolean checkInput() {
        if(nameEt.getText().toString().trim().length() > 0 &&
                phoneEt.getText().toString().trim().length() > 0 &&
                emailEt.getText().toString().trim().length() > 0 &&
                dateEt.getText().toString().trim().length() > 0 &&
                descriptionEt.getText().toString().trim().length() > 0)
            return true;
        else
            return false;
    }

    void verifyAppointment(final String otp) {
        String url = Config.apiUrl + "/appointment/verify";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(!object.getString("msg").equals("invalid")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MakeAppointment.this);
                        builder.setTitle("Success");
                        builder.setMessage("Your appointment is fixed!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        builder.create().show();
                    }else {
                        Toast.makeText(MakeAppointment.this, "Invalid OTP", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MakeAppointment.this, "Invalid OTP", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap params = new HashMap();
                params.put("otp", otp);
                params.put("email", emailEt.getText().toString());
                params.put("phone", phoneEt.getText().toString());
                return params;
            }
        };
        VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);
        instantiate();

        dateEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(MakeAppointment.this, pickerListener, year, month, day).show();
                }
            }
        });

        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MakeAppointment.this, pickerListener, year, month, day).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.make_appointment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.done:
                if(checkInput()) {
                    String url = Config.apiUrl+"/appointment";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.d("option", jsonObject.getString("data"));
                                AlertDialog.Builder builder = new AlertDialog.Builder(MakeAppointment.this);
                                builder.setTitle("Enter OTP");
                                View v = LayoutInflater.from(MakeAppointment.this).inflate(R.layout.otp, null);
                                final EditText otpEt = (EditText)v.findViewById(R.id.otpEt);
                                builder.setView(v);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(otpEt.getText().toString().trim().length() > 0) {
                                            verifyAppointment(otpEt.getText().toString());
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.create().show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("option", error.toString());
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("name", nameEt.getText().toString());
                            params.put("email", emailEt.getText().toString());
                            params.put("time", dateEt.getText().toString());
                            params.put("phone", phoneEt.getText().toString());
                            params.put("description", descriptionEt.getText().toString());
                            params.put("doctor_id", String.valueOf(docId));
                            params.put("clinic_id", String.valueOf(clinicId));
                            return params;
                        }
                    };
                    VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
