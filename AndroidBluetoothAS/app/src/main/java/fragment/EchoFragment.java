package fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import logging.Log;

import com.example.androidbluetooth.R;

import control.IControlFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EchoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EchoFragment extends Fragment implements IControlFragment, View.OnClickListener {
    private static final String TAG = "EchoFragment";
    private TextView mEchoReceived;
    private EditText mEchoInput;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EchoFragment.
     */
    public static EchoFragment newInstance() {
        EchoFragment fragment = new EchoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public EchoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_echo, container, false);

        v.findViewById(R.id.echo_button).setOnClickListener(this);
        mEchoInput = (EditText) v.findViewById(R.id.echo_input);
        mEchoReceived = (TextView) v.findViewById(R.id.echo_received);

        return v;
    }


    @Override
    public String getPageTitle() {
        return "Echo";
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.echo_button){
            Log.i(TAG, "Echoing: '" + mEchoInput.getText() + "'");

            // TODO Send the message to the Arduino
            return;
        }

        Log.w(TAG, "Not sure what triggered this on click...");
    }
}
