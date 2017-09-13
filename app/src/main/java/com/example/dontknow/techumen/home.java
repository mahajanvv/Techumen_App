package com.example.dontknow.techumen;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {

    private TextView gocn;
    private TextView goce;
    private TextView admin;
    private TextView mys;
    private TextView work;
    private TextView spot;

    private DatabaseReference databaseReference;
    public home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gocn= (TextView)view.findViewById(R.id.idgoc_novice);
        goce= (TextView)view.findViewById(R.id.idgoc_expert);
        admin = (TextView)view.findViewById(R.id.idRepublica);
        mys = (TextView)view.findViewById(R.id.idmysterio);
        work = (TextView)view.findViewById(R.id.idworkshop);
        spot = (TextView)view.findViewById(R.id.idspot);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gocn.setText("CodeWizard_Novice  : "+dataSnapshot.child("CodeWizard_Novice").getChildrenCount()+"/120");
                goce.setText("CodeWizard_Expert  : "+dataSnapshot.child("CodeWizard_Expert").getChildrenCount()+"/80");
                admin.setText("ResPublica  : "+dataSnapshot.child("ResPublica").getChildrenCount()+"/100");
                mys.setText("MYSTERIO_3_0  : "+dataSnapshot.child("MYSTERIO_3_0").getChildrenCount()+"/100");
                work.setText("Web_ON  : "+dataSnapshot.child("Web_ON").getChildrenCount()+"/100");
                spot.setText("Spot Games : "+dataSnapshot.child("SPOT").getChildrenCount()+"/100");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
