package com.example.dontknow.techumen;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class addentry extends Fragment {

    private Button next;
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText eventname;
    private EditText college;
    private EditText no_of_participants;
    private EditText year;

    public addentry() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addentry, container, false);

    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        next = (Button) view.findViewById(R.id.idNext);
        name = (EditText) view.findViewById(R.id.idname);
        email = (EditText) view.findViewById(R.id.idemail);
        phone = (EditText)view.findViewById(R.id.idphone);
        eventname = (EditText)view.findViewById(R.id.idevent);
        college = (EditText)view.findViewById(R.id.idcollege);
        no_of_participants = (EditText)view.findViewById(R.id.idnoofparticipants);
        year = (EditText)view.findViewById(R.id.idyear);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(phone.getText()) && !TextUtils.isEmpty(eventname.getText())
                        && !TextUtils.isEmpty(college.getText()) && !TextUtils.isEmpty(no_of_participants.getText()) && !TextUtils.isEmpty(year.getText()))
                {
                    Intent intent = new Intent(view.getContext(),Signature.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("name",name.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("phone",phone.getText().toString());
                    intent.putExtra("eventname",eventname.getText().toString());
                    intent.putExtra("college",college.getText().toString());
                    intent.putExtra("no_of_participants",no_of_participants.getText().toString());
                    intent.putExtra("year",year.getText().toString());
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(v.getContext().getApplicationContext(),"Please Fill out All the Fields!!",Toast.LENGTH_LONG).show();
                }
            }
        });
        no_of_participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create123(savedInstanceState);
            }
        });
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(savedInstanceState);
            }
        });
        eventname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialog(savedInstanceState);
            }
        });

    }
    public void create123(Bundle savedInstanceState)
    {
        final CharSequence[] items = {"1","2","3","4","5"};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select No. Of Participants");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {


                switch(item)
                {
                    case 0:
                        // Your code when first option seletced
                        no_of_participants.setText("1");
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        no_of_participants.setText("2");
                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        no_of_participants.setText("3");
                        break;
                    case 3:
                        // Your code when 4th  option seletced
                        no_of_participants.setText("4");
                        break;
                    case 4:
                        no_of_participants.setText("5");
                        break;
                }

                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }
    public void CreateDialog(Bundle savedInstanceState)
    {
        final CharSequence[] items = {"CodeWizard Novice","CodeWizard Expert","ResPublica","MYSTERIO 3.0","Web<ON>","Counter Strike","NFS"};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Event");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {


                switch(item)
                {
                    case 0:
                        // Your code when first option seletced
                        eventname.setText("CodeWizard_Novice");
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        eventname.setText("CodeWizard_Expert");
                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        eventname.setText("ResPublica");
                        break;
                    case 3:
                        // Your code when 4th  option seletced
                        eventname.setText("MYSTERIO_3_0");
                        break;
                    case 4:
                        eventname.setText("Web_ON");
                        break;
                    case 5:
                        eventname.setText("SPOTCS");
                        break;
                    case 6:
                        eventname.setText("SPOTNFS");
                        break;
                }

                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();


    }
    public void onCreateDialog(Bundle savedInstanceState) {

        final CharSequence[] items = {"F.Y","S.Y","T.Y","B.TECH","Diploma"};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Year");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {


                switch(item)
                {
                    case 0:
                        // Your code when first option seletced
                        year.setText("F.Y");
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        year.setText("S.Y");
                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        year.setText("T.Y");
                        break;
                    case 3:
                        // Your code when 4th  option seletced
                        year.setText("B.TECH");
                        break;
                    case 4:
                        year.setText("Diploma");
                        break;
                }

                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();


    }
}
