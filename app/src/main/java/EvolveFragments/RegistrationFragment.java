package EvolveFragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.evolve.evolve.EvolveActivities.MainActivity;
import com.evolve.evolve.R;
import com.google.gson.Gson;

import EvolveObjects.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener{


    Button submit;
    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        submit= (Button) getActivity().findViewById(R.id.submit);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

            AlertDialog.Builder dialog =new AlertDialog.Builder(getActivity());

            LayoutInflater inflater=getActivity().getLayoutInflater();
            View view=inflater.inflate(R.layout.otp_custom_dialog,null);
            dialog.setView(view);

            view.findViewById(R.id.otp_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    getActivity().finish();
                }
            });

            dialog.setCancelable(false);
            dialog.create().show();
        }

    }

