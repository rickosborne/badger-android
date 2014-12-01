package org.rickosborne.badger;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.rickosborne.badger.data.CheckIn;
import org.rickosborne.badger.data.User;

import java.util.ArrayList;
import java.util.Collection;


/**
 * A fragment representing a single Patient detail screen.
 * This fragment is either contained in a {@link PatientListActivity}
 * in two-pane mode (on tablets) or a {@link PatientDetailActivity}
 * on handsets.
 */
public class PatientDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private User patient = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PatientDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            long userId = Long.parseLong(getArguments().getString(ARG_ITEM_ID, "0"), 10);
            if (userId > 0) patient = ((BadgerApp) getActivity().getApplication()).getPatientById(userId);
//            patient = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_detail, container, false);

//        View view = getView();
        if ((patient != null) && (rootView != null)) {
            TextView patientName = (TextView) rootView.findViewById(R.id.activeUserName);
            if (patientName != null) patientName.setText(patient.toString());
            TextView patientEmail = (TextView) rootView.findViewById(R.id.activeUserEmail);
            if (patientEmail != null) {
                patientEmail.setText(patient.getEmail());
                patientEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (patient == null) return;
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.putExtra(Intent.EXTRA_EMAIL, patient.getEmail());
                        startActivity(Intent.createChooser(intent, "Send Email"));
                    }
                });
            }
//            final ListView checkInsList = (ListView) rootView.findViewById(R.id.checkInsList);
//            if (checkInsList != null) ((BadgerApp) getActivity().getApplication()).checkInsForUser(new BadgerApp.AfterGetCheckIns() {
//                @Override
//                public void complete(Collection<CheckIn> checkIns) {
//                    checkInsList.setAdapter(new ArrayAdapter<CheckIn>(
//                        getActivity(),
//                        android.R.layout.simple_list_item_activated_1,
//                        android.R.id.text1,
//                        new ArrayList<CheckIn>(checkIns)
//                    ));
//                }
//            }, patient, getActivity());
        }
        // Show the dummy content as text in a TextView.
//        if (patient != null) {
//            ((TextView) rootView.findViewById(R.id.patient)).setText(patient.content);
//        }

        return rootView;
    }

}
