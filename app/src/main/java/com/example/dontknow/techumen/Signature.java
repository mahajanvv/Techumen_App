package com.example.dontknow.techumen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Signature extends AppCompatActivity {

    private SignaturePad signaturePad;

    private Uri ImageUri= null;

    private Button clear;
    private Button register;
    private EditText Remaining;
    private EditText transaction;

    public static final int Request_Code = 2243;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 22345;

    private Bundle bundle;

    private StorageReference storageReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private ProgressDialog progressDialog;

    private boolean done=false;
    private int cnt;
    private String OfflineImage;

    private MydbHandler mydbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);


        clear = (Button)findViewById(R.id.idclear);
        register = (Button)findViewById(R.id.idRegister);
        Remaining =(EditText) findViewById(R.id.idRemaining);
        transaction = (EditText)findViewById(R.id.idTransaction);

        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        signaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        bundle = getIntent().getExtras();

        if(bundle.getString("eventname").toString().equals("CodeWizard_Novice"))
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("CodeWizard_Novice");
        }
        else if(bundle.getString("eventname").toString().equals("CodeWizard_Expert"))
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("CodeWizard_Expert");
        }
        else if(bundle.getString("eventname").toString().equals("ResPublica"))
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("ResPublica");
        }
        else if(bundle.getString("eventname").toString().equals("MYSTERIO_3_0"))
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("MYSTERIO_3_0");
        }
        else if(bundle.getString("eventname").toString().equals("Web_ON"))
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Web_ON");
        }
        else if(bundle.getString("eventname").toString().equals("SPOTCS") || bundle.getString("eventname").toString().equals("SPOTNFS"))
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("SPOT");
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(Remaining.getText()))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Remaining Fees If Any Otherwise Enter 0 In That Field",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(TextUtils.isEmpty(transaction.getText()))
                    {
                        transaction.setText("NULL");
                    }
                    progressDialog.setMessage("Saving Signature!!!");
                    progressDialog.show();
                    createSignature();
                    progressDialog.setMessage("Checking Connection");
                    ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo networkInfo  = connec.getActiveNetworkInfo();
                    if(networkInfo!=null && networkInfo.isConnected())
                    {
                        //Toast.makeText(getApplicationContext(),"You Are Connected To Server",Toast.LENGTH_LONG).show();
                        progressDialog.setMessage("Saving Entry To Online Database!!!!");


                        //Toast.makeText(getApplicationContext(),""+dataSnapshot.getValue(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(),""+dataSnapshot.getValue(),Toast.LENGTH_LONG).show();

                        cnt++;
                        final DatabaseReference newEntry = databaseReference.push();
                        final String id =newEntry.getKey();

                        if(ImageUri!=null)
                        {
                            StorageReference filepath = storageReference.child("SignatureImages").child(id);
                            filepath.putFile(ImageUri).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    databaseReference.child(id).removeValue();
                                    Toast.makeText(Signature.this,"Failed To Upload Image To Database",Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            });

                            filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                    newEntry.child("ID").setValue(id);
                                    newEntry.child("Name").setValue(bundle.getString("name"));
                                    newEntry.child("Email").setValue(bundle.getString("email"));
                                    newEntry.child("Phone").setValue(bundle.getString("phone"));
                                    newEntry.child("College").setValue(bundle.getString("college"));
                                    newEntry.child("Year").setValue(bundle.getString("year"));
                                    newEntry.child("Remaining").setValue(""+Remaining.getText().toString());
                                    newEntry.child("Signature").setValue(downloadUrl.toString().trim());
                                    newEntry.child("Eventname").setValue(bundle.getString("eventname"));
                                    newEntry.child("Date").setValue(new Date().toString());
                                    newEntry.child("Timestamp").setValue(""+System.currentTimeMillis());
                                    newEntry.child("Useremail").setValue(firebaseAuth.getCurrentUser().getEmail());
                                    newEntry.child("Mailstatus").setValue("Mail not send");
                                    newEntry.child("Messagestatus").setValue("Message not send");
                                    newEntry.child("TransactionID").setValue(""+transaction.getText());
                                    /**/

                                    progressDialog.setMessage("Saving Entry to Online Database!!!");
                                    mydbHandler.add(new TeDatabase(bundle.getString("name"),bundle.getString("email"),bundle.getString("phone"),bundle.getString("college"),bundle.getString("year"),OfflineImage,Remaining.getText().toString(),transaction.getText().toString(),bundle.getString("eventname"),"UPLOADED"));
                                    progressDialog.dismiss();

                                    String wayurl="https://mahajanvv.000webhostapp.com/send_msg.php";
                                    Toast.makeText(getApplicationContext(),"Preparing Message",Toast.LENGTH_LONG).show();
                                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, wayurl, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            newEntry.child("Messagestatus").setValue("Message send");
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
                                            params.put("to",bundle.getString("phone"));
                                            params.put("sub","Hello! "+ bundle.getString("name")+" you have successfully registered for "+bundle.getString("eventname")+" in Techumen 2K17"
                                            );
                                            return params;
                                        }
                                    };
                                    Toast.makeText(getApplicationContext(),"Sending Message",Toast.LENGTH_LONG).show();
                                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest1);
                                    String url = "https://mahajanvv.000webhostapp.com/send-mailte.php";
                                    Toast.makeText(getApplicationContext(),"Preparing Mail",Toast.LENGTH_LONG).show();
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            newEntry.child("Mailstatus").setValue("Mail send");
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
                                            params.put("email",bundle.getString("email"));
                                            params.put("name",bundle.getString("name"));
                                            params.put("eventname",bundle.getString("eventname"));
                                            return params;
                                        }
                                    };
                                    Toast.makeText(getApplicationContext(),"Sending Mail",Toast.LENGTH_LONG).show();
                                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                                    Toast.makeText(getApplicationContext(),"SuccessFully Saved To Online & Offline Database!!!",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Signature.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });

                        }
                        else {
                            databaseReference.child(id).removeValue();
                            progressDialog.dismiss();
                            Toast.makeText(Signature.this, "ImageUri is null", Toast.LENGTH_LONG).show();
                        }


                    }
                    else
                    {
                        // Toast.makeText(getApplicationContext(),"Not Connected",Toast.LENGTH_LONG).show();
                        progressDialog.setMessage("Saving Entry to Offline Database!!!");
                        mydbHandler.add(new TeDatabase(bundle.getString("name"),bundle.getString("email"),bundle.getString("phone"),bundle.getString("college"),bundle.getString("year"),OfflineImage,Remaining.getText().toString(),transaction.getText().toString(),bundle.getString("eventname"),"Not Uploaded"));
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"SuccessFully Saved To Offline Database!!!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Signature.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });

        if(checkPermissionREAD_EXTERNAL_STORAGE(this))
        {}

        mydbHandler = new MydbHandler(this,null,null,1);
    }
    private void createSignature() {

        Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
        if(addJpgSignatureToGallery(signatureBitmap)){
            Toast.makeText(this, "JPG format saved into Gallery", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Failed to save jpg format to Gallery", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));;
        //Toast.makeText(Signature.this,"1",Toast.LENGTH_LONG).show();
        saveBitmapToJPG(signature, photo);
        //Toast.makeText(Signature.this,"2",Toast.LENGTH_LONG).show();

        scanMediaFile(photo);

        OfflineImage = photo.getAbsolutePath();

        ImageUri = Uri.fromFile(photo);
        result = true;
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        Signature.this.sendBroadcast(mediaScanIntent);
    }

    private File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);

        if(!file.mkdirs()){
            Log.d("TAG", "Directory not created");
        }

        return file;
    }

    private void saveBitmapToJPG(Bitmap signature, File photo) {
        Bitmap newBitmap = Bitmap.createBitmap(signature.getWidth(), signature.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(signature, 0,0, null);
        try {
            OutputStream stream = new FileOutputStream(photo);
            newBitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    //Toast.makeText(Login.this, "GET_ACCOUNTS Denied",
                    // Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
}
