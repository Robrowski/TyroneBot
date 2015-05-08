package fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidbluetooth.R;

import control.IControlFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeasuringFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeasuringFragment extends Fragment implements IControlFragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MeasuringFragment.
     */
    public static MeasuringFragment newInstance() {
        MeasuringFragment fragment = new MeasuringFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MeasuringFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measuring, container, false);
    }


    @Override
    public String getPageTitle() {
        return "Measure";
    }
}
