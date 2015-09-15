package EvolveFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evolve.evolve.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tutorialfragment extends Fragment {


    public Tutorialfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorialfragment, container, false);
    }


}
