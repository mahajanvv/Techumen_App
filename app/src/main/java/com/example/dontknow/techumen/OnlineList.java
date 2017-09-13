package com.example.dontknow.techumen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class OnlineList extends AppCompatActivity {

    private Query databasereference = null;
    private RecyclerView recyclerView;
    private Bundle bundle;
    private ArrayList<exceldata> arrayList;
    private DatabaseReference databaseReferenceupdate;
    public  static  final  String FileName="";
    private ProgressDialog progressDialog;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 23845;
    private String File_Name = "/Techumen/";
    public static String EVENT_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_list);
        bundle = getIntent().getExtras();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        if (bundle.getString("Childe").contains("CodeWizard_Novice")) {
            databasereference = FirebaseDatabase.getInstance().getReference().child("CodeWizard_Novice").orderByChild("Timestamp");
        } else if (bundle.getString("Childe").contains("CodeWizard_Expert")) {
            databasereference = FirebaseDatabase.getInstance().getReference().child("CodeWizard_Expert").orderByChild("Timestamp");
        } else if (bundle.getString("Childe").contains("ResPublica")) {
            databasereference = FirebaseDatabase.getInstance().getReference().child("ResPublica").orderByChild("Timestamp");
        } else if (bundle.getString("Childe").contains("Web_ON")) {
            databasereference = FirebaseDatabase.getInstance().getReference().child("Web_ON").orderByChild("Timestamp");
        } else if (bundle.getString("Childe").contains("MYSTERIO_3_0")) {
            databasereference = FirebaseDatabase.getInstance().getReference().child("MYSTERIO_3_0").orderByChild("Timestamp");
        }
        else if (bundle.getString("Childe").contains("Spot Games")) {
            databasereference = FirebaseDatabase.getInstance().getReference().child("SPOT").orderByChild("Timestamp");
        }
        databasereference.keepSynced(true);

        databaseReferenceupdate = FirebaseDatabase.getInstance().getReference();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView) findViewById(R.id.idOnlineList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<onlineentryaccept, onlineListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<onlineentryaccept, onlineListViewHolder>(
                onlineentryaccept.class, R.layout.online_entry_row, onlineListViewHolder.class, databasereference) {
            @Override
            protected void populateViewHolder(onlineListViewHolder viewHolder, final onlineentryaccept model, int position) {


                viewHolder.setOnlineRowname(model.getName());
                viewHolder.setOnlineRowemail(model.getEmail());
                viewHolder.setOnlineRowphone(model.getPhone());
                viewHolder.setOnlineRowremaining(model.getRemaining());
                viewHolder.setOnlineRowmemberemail(model.getUseremail());
                viewHolder.setOnlineRowdate(model.getDate());
                viewHolder.setOnlineRoweventname(model.getEventname());
                viewHolder.setOnlineRowmailstatus(model.getMailstatus());
                viewHolder.setOnlineRowmessagestatus(model.getMessagestatus());

                EVENT_NAME = model.getEventname();

                exceldata exceldata = new exceldata(model.getName(),model.getEmail(),model.getPhone(),model.getCollege(),model.getYear(),model.getRemaining(),model.getTransactionID(),model.getEventname(),model.getUseremail(),model.getDate());


                viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if(model.getMailstatus().equals("Mail not send"))
                        {

                            String url = "https://mahajanvv.000webhostapp.com/send-mailte.php";
                            Toast.makeText(v.getContext(),"Preparing Mail",Toast.LENGTH_LONG).show();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(model.getEventname().equals("SPOTCS") || model.getEventname().equals("SPOTNFS"))
                                    {
                                        databaseReferenceupdate.child("SPOT").child(model.getID()).child("Mailstatus").setValue("Mail send");
                                    }
                                    else
                                    {
                                        databaseReferenceupdate.child(model.getEventname()).child(model.getID()).child("Mailstatus").setValue("Mail send");
                                    }
                                    Toast.makeText(getApplicationContext(),"mail is sent",Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),"mail not send because of"+error.toString(),Toast.LENGTH_LONG).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String>params = new HashMap<>();
                                    params.put("email",model.getEmail());
                                    params.put("name",model.getName());
                                    params.put("eventname",model.getEventname());
                                    return params;
                                }
                            };
                            Toast.makeText(getApplicationContext(),"Sending Mail",Toast.LENGTH_LONG).show();
                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                        }
                        if(model.getMessagestatus().equals("Message not send"))
                        {

                            String wayurl="https://mahajanvv.000webhostapp.com/send_msg.php";
                            Toast.makeText(getApplicationContext(),"Preparing Message",Toast.LENGTH_LONG).show();
                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, wayurl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(model.getEventname().equals("SPOTCS") || model.getEventname().equals("SPOTNFS"))
                                    {
                                        databaseReferenceupdate.child("SPOT").child(model.getID()).child("Messagestatus").setValue("Message send");
                                    }
                                    else
                                    {
                                        databaseReferenceupdate.child(model.getEventname()).child(model.getID()).child("Messagestatus").setValue("Message send");
                                    }
                                    Toast.makeText(getApplicationContext(),"message is sent",Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),"message is not sent because of"+error.toString(),Toast.LENGTH_LONG).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String>params = new HashMap<>();
                                    params.put("to",model.getPhone());
                                    params.put("sub","Hello! "+ model.getName()+" you have successfully registered for "+model.getEventname()+" in Techumen 2K17. Please preserve this message till event."
                                    );
                                    return params;
                                }
                            };
                            Toast.makeText(getApplicationContext(),"Sending Message",Toast.LENGTH_LONG).show();
                            MySingleton.getInstance(v.getContext().getApplicationContext()).addToRequestQueue(stringRequest1);
                        }
                        if(model.getMessagestatus().equals("Message send") && model.getMailstatus().equals("Mail send"))
                        {
                            Toast.makeText(v.getContext(),"Mail and Message already sent",Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class onlineListViewHolder extends RecyclerView.ViewHolder
    {
        View view =null;

        public onlineListViewHolder(View itemView) {
            super(itemView);

            view = itemView;
        }
        private void setOnlineRowname(String name)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_name);
            textView.setText(name);
        }
        private void setOnlineRowemail(String email)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_email);
            textView.setText(email);
        }
        private void setOnlineRowphone(String phone)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_phone);
            textView.setText(phone);
        }
        private void setOnlineRowremaining(String remaining)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_remaininig);
            textView.setText(remaining);
        }
        private void setOnlineRowmemberemail(String membername)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_memberemail);
            textView.setText(membername);
        }
        private void setOnlineRowdate(String date)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_date);
            textView.setText(date);
        }
        private void setOnlineRowmailstatus(String mail)
        {
            ImageView textView = (ImageView ) view.findViewById(R.id.idonline_entry_mail);
            if(mail.equals("Mail send"))
            {
                textView.setVisibility(View.VISIBLE);
            }
        }
        private void setOnlineRowmessagestatus(String message)
        {
            ImageView textView = (ImageView ) view.findViewById(R.id.idonline_entry_message);
            if(message.equals("Message send"))
            {
                textView.setVisibility(View.VISIBLE);
            }
        }
        private void setOnlineRoweventname(String eventname)
        {
            TextView textView = (TextView ) view.findViewById(R.id.idonline_entry_eventname);
            textView.setText(eventname);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.download_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        if(itemid == R.id.iddownload) {
            progressDialog.setMessage("Praparing Excel Sheet!!!");
            progressDialog.show();
            arrayList = new ArrayList<exceldata>();
            arrayList.add(new exceldata(new String("NAME"), new String("EMAIL"), new String("PHONE"), new String("COLLEGE"), new String("YEAR"), new String("REMAINING"), new String("TRANSACTION_ID"), new String("EVENT_NAME"), new String("USER_EMAIL"), new String("DATE")));
            databasereference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        exceldata exceldata = new exceldata(ds.child("Name").getValue().toString(),
                                ds.child("Email").getValue().toString(),
                                ds.child("Phone").getValue().toString(),
                                ds.child("College").getValue().toString(),
                                ds.child("Year").getValue().toString(),
                                ds.child("Remaining").getValue().toString(),
                                ds.child("TransactionID").getValue().toString(),
                                ds.child("Eventname").getValue().toString(),
                                ds.child("Useremail").getValue().toString(),
                                ds.child("Date").getValue().toString());

                        arrayList.add(exceldata);

                    }
                    if(EVENT_NAME.equals("SPOTNFS") || EVENT_NAME.equals("SPOTCS"))
                    {
                        EVENT_NAME = "SPOT";
                    }
                    String Fnamexls=EVENT_NAME  + ".xlsx";

                    java.io.File sdCard = Environment.getExternalStorageDirectory();

                    File directory = null;
                    try {
                        directory = new File(sdCard.getAbsolutePath() + "/Techumen/");
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"1"+e.toString(),Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getApplicationContext(),""+sdCard.getAbsolutePath(),Toast.LENGTH_LONG).show();
                    directory.mkdirs();

                    File file = null;
                    try {
                        file = new File(directory, Fnamexls);
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"2"+e.toString(),Toast.LENGTH_LONG).show();
                    }

                    WorkbookSettings wbSettings = new WorkbookSettings();
                    wbSettings.setLocale(new Locale("en", "EN"));

                    WritableWorkbook workbook;
                    try {
                        workbook = Workbook.createWorkbook(file, wbSettings);
                        WritableSheet sheet = workbook.createSheet("First Sheet", 0);
                        for(int i=0;i<arrayList.size();i++)
                        {
                            //Toast.makeText(getApplicationContext(),""+arrayList.size(),Toast.LENGTH_LONG).show();
                            Label label1= new Label(0,i,arrayList.get(i).get_name());
                            Label label2= new Label(1,i,arrayList.get(i).get_email());
                            Label label3= new Label(2,i,arrayList.get(i).get_phone());
                            Label label4= new Label(3,i,arrayList.get(i).get_college());
                            Label label5= new Label(4,i,arrayList.get(i).get_year());
                            Label label6= new Label(5,i,arrayList.get(i).get_remaining());
                            Label label7= new Label(6,i,arrayList.get(i).get_transaction());
                            Label label8= new Label(7,i,arrayList.get(i).get_eventname());
                            Label label9= new Label(8,i,arrayList.get(i).get_useremail());
                            Label label10= new Label(9,i,arrayList.get(i).get_date());

                            try {
                                sheet.addCell(label1);
                                sheet.addCell(label2);
                                sheet.addCell(label3);
                                sheet.addCell(label4);
                                sheet.addCell(label5);
                                sheet.addCell(label6);
                                sheet.addCell(label7);
                                sheet.addCell(label8);
                                sheet.addCell(label9);
                                sheet.addCell(label10);
                            } catch (RowsExceededException e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"3"+e.toString(),Toast.LENGTH_LONG).show();
                            } catch (WriteException e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"4"+e.toString(),Toast.LENGTH_LONG).show();
                            }

                        }
                        workbook.write();

                        try {
                            workbook.close();
                        } catch (WriteException e) {

                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"5"+e.toString(),Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"6"+e.toString(),Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Process Completeed",Toast.LENGTH_LONG).show();
                }
                    //Toast.makeText(getApplicationContext(),""+arrayList.size(),Toast.LENGTH_LONG).show();


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }



        return super.onOptionsItemSelected(item);
}
}
