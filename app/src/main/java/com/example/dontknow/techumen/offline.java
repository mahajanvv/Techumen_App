package com.example.dontknow.techumen;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class offline extends Fragment {

    private static int flg=0;

    private Button updata;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private ArrayList<TeDatabase> data;
    private ArrayList<TeDatabase>databaseupload;
    private static MydbHandler mydbHandler;
    private static int cnt;
    private static Uri ImageUri;
    private static StorageReference storageReference;
    private static FirebaseAuth firebaseAuth;
    private Bundle bundle;
    static View.OnLongClickListener myOnLongClickListener;
    public static final int Request_Code = 8243;
    private static ProgressDialog progressDialog;
    public offline() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = savedInstanceState;
        myOnLongClickListener  = new MyOnLongClickListener(view.getContext());
        recyclerView = (RecyclerView )view.findViewById(R.id.idofflineentrylist);
        recyclerView.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setCancelable(false);

        mydbHandler = new MydbHandler(view.getContext(),null,null,1);

        data = mydbHandler.getDatabase();

        adapter = new offline_entry_adapter(data);
        recyclerView.setAdapter(adapter);



        storageReference = FirebaseStorage.getInstance().getReference();

    }
    private static class MyOnLongClickListener implements View.OnLongClickListener
    {
        private final Context context;
        private MyOnLongClickListener(Context context)
        {
            this.context = context;
        }

        @Override
        public boolean onLongClick(final View v) {
            ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo  = connec.getActiveNetworkInfo();
            if(networkInfo!=null && networkInfo.isConnected())
            {
                progressDialog.setMessage("Uploading!!!");
                progressDialog.show();
                int selecteditemposition = recyclerView.getChildPosition(v);
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selecteditemposition);
                TextView mail=(TextView)viewHolder.itemView.findViewById(R.id.idoffline_entry_email);
                TextView phone=(TextView)viewHolder.itemView.findViewById(R.id.idoffline_entry_phone);
                TextView remaining=(TextView)viewHolder.itemView.findViewById(R.id.idoffline_entry_remaining);
                final TextView status =  (TextView)viewHolder.itemView.findViewById(R.id.idoffline_entry_status);
                final TeDatabase cwDatabase = mydbHandler.getTedatabase(mail.getText().toString(),phone.getText().toString(),remaining.getText().toString());
                if(cwDatabase == null)
                {
                    progressDialog.dismiss();
                    Toast.makeText(v.getContext().getApplicationContext(),"Entry Already Uploaded",Toast.LENGTH_LONG).show();
                    return true;
                }
                else
                {

                    DatabaseReference databaseReference=null;

                    //Toast.makeText(getApplicationContext(),""+dataSnapshot.getValue(),Toast.LENGTH_LONG).show();
                    cnt++;
                    if(cwDatabase.get_eventname().equals("CodeWizard_Novice"))
                    {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("CodeWizard_Novice");
                    }
                    else if(cwDatabase.get_eventname().equals("CodeWizard_Expert"))
                    {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("CodeWizard_Expert");
                    }
                    else if(cwDatabase.get_eventname().equals("ResPublica"))
                    {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("ResPublica");
                    }
                    else if(cwDatabase.get_eventname().equals("MYSTERIO_3_0"))
                    {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("MYSTERIO_3_0");
                    }
                    else if(cwDatabase.get_eventname().equals("Web_ON"))
                    {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Web_ON");
                    }
                    else if(cwDatabase.get_eventname().equals("SPOTCS") || cwDatabase.get_eventname().equals("SPOTNFS"))
                    {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("SPOT");
                    }
                    final DatabaseReference newEntry = databaseReference.push();
                    final String id =newEntry.getKey();

                    ImageUri = Uri.fromFile(new File(cwDatabase.get_image()));
                    if(ImageUri!=null)
                    {
                        StorageReference filepath = storageReference.child("SignatureImages").child(id);
                        final DatabaseReference finalDatabaseReference = databaseReference;
                        filepath.putFile(ImageUri).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                finalDatabaseReference.child(id).removeValue();
                                progressDialog.dismiss();
                                Toast.makeText(v.getContext(),"Failed To Upload Image To Database",Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        });

                        filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Uri downloadUrl = taskSnapshot.getDownloadUrl();


                                newEntry.child("ID").setValue(id);

                                newEntry.child("Name").setValue(cwDatabase.get_name());
                                newEntry.child("Email").setValue(cwDatabase.get_email());
                                newEntry.child("Phone").setValue(cwDatabase.get_phone());
                                newEntry.child("College").setValue(cwDatabase.get_college());
                                newEntry.child("Year").setValue(cwDatabase.get_year());
                                newEntry.child("Remaining").setValue(""+cwDatabase.get_remaining());
                                newEntry.child("Eventname").setValue(cwDatabase.get_eventname());
                                newEntry.child("Signature").setValue(downloadUrl.toString().trim());
                                newEntry.child("Date").setValue(new Date().toString());
                                newEntry.child("Timestamp").setValue(""+System.currentTimeMillis());
                                newEntry.child("Useremail").setValue(firebaseAuth.getCurrentUser().getEmail());
                                newEntry.child("Mailstatus").setValue("Mail not send");
                                newEntry.child("Messagestatus").setValue("Message not Send");
                                newEntry.child("TransactionID").setValue(cwDatabase.get_transaction());



                                //databaseReferenceCWCount.setValue(""+cnt);
                                mydbHandler.setUploaded(cwDatabase);

                                progressDialog.dismiss();

                                status.setText("UPLOADED");
                                status.setTextColor(Color.GREEN);
                                Toast.makeText(v.getContext().getApplicationContext(),"Uploaded the entry!!!",Toast.LENGTH_LONG).show();

                                String url = "https://mahajanvv.000webhostapp.com/send-mailte.php";
                                Toast.makeText(v.getContext(),"Preparing Mail",Toast.LENGTH_LONG).show();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        newEntry.child("Mailstatus").setValue("Mail send");
                                        Toast.makeText(v.getContext().getApplicationContext(),"mail is sent",Toast.LENGTH_LONG).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(v.getContext().getApplicationContext(),"mail not send because of"+error.toString(),Toast.LENGTH_LONG).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String>params = new HashMap<>();
                                        params.put("email",cwDatabase.get_email());
                                        params.put("name",cwDatabase.get_name());
                                        params.put("eventname",cwDatabase.get_eventname());
                                        return params;
                                    }
                                };
                                Toast.makeText(v.getContext().getApplicationContext(),"Sending Mail",Toast.LENGTH_LONG).show();
                                MySingleton.getInstance(v.getContext().getApplicationContext()).addToRequestQueue(stringRequest);

                                String wayurl="https://mahajanvv.000webhostapp.com/send_msg.php";
                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, wayurl, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        newEntry.child("Messagestatus").setValue("Message send");
                                        Toast.makeText(v.getContext().getApplicationContext(),"message is sent",Toast.LENGTH_LONG).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(v.getContext().getApplicationContext(),"message is not sent because of"+error.toString(),Toast.LENGTH_LONG).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String>params = new HashMap<>();
                                        params.put("to",cwDatabase.get_phone());
                                        params.put("sub","Hello! "+ cwDatabase.get_name()+" you have successfully registered for "+cwDatabase.get_eventname()+" in Techumen 2K17"
                                        );
                                        return params;
                                    }
                                };
                                MySingleton.getInstance(v.getContext().getApplicationContext()).addToRequestQueue(stringRequest1);

                            }
                        });


                    }
                    else {
                        databaseReference.child(id).removeValue();
                        progressDialog.dismiss();
                        Toast.makeText(v.getContext().getApplicationContext(), "ImageUri is null", Toast.LENGTH_LONG).show();
                    }
                }
                //FirebaseUpload(databaseupload.get(0));
                //Toast.makeText(this,"Wait Until menu pops up that will send mail to participant",Toast.LENGTH_LONG).show();
                //Toast.makeText(this,"Uploaded Sucessfully the entry of "+databaseupload.get(0).get_firstname()+" "+databaseupload.get(0).get_midname()+" "+databaseupload.get(0).get_lastname(),Toast.LENGTH_LONG).show();
                //updata.setEnabled(true);
            }
            else
            {
                Toast.makeText(v.getContext().getApplicationContext(),"Please connect to internet",Toast.LENGTH_LONG).show();
            }
            return true;

        }
    }



}
