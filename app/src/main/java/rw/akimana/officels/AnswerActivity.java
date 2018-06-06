package rw.akimana.officels;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import rw.akimana.officels.Controllers.DatabaseHelper;
import rw.akimana.officels.Controllers.FilePath;
import rw.akimana.officels.Controllers.SessionManager;
import rw.akimana.officels.Models.IpAddress;

import static rw.akimana.officels.Controllers.SessionManager.KEY_USER_ID;

public class AnswerActivity extends AppCompatActivity implements View.OnClickListener, OnLoadCompleteListener, OnPageChangeListener {

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String selectedFilePath, userId, examId, ipAddress, protocal, dataUrl;
    private PDFView pdfAttachment;
    private Button bUpload;
    private TextView tvHeading;

    SessionManager sessionManager;
    private HashMap<String, String> hashMap;

    String URL_PREFIX = "/officels/apis/upload_answers.php";
    DatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        pdfAttachment = findViewById(R.id.pdfvAttachment);
        bUpload = findViewById(R.id.b_upload);
        tvHeading = findViewById(R.id.tvHeading);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        helper = new DatabaseHelper(getApplicationContext());
        hashMap = helper.findIpDetails();
        if(hashMap == null) {
            protocal = "http";
            ipAddress = "192.168.0.122";
        }
        else{
            String id = hashMap.get(IpAddress.IpAttributes.COL_ID);
            protocal = hashMap.get(IpAddress.IpAttributes.COL_PROTOCAL);
            ipAddress = hashMap.get(IpAddress.IpAttributes.COL_IPADDRESS);
        }
        dataUrl = protocal + "://" + ipAddress + URL_PREFIX;

        hashMap = sessionManager.getUserDetails();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            examId = (String) bundle.get("exam_id");
        }
        userId = hashMap.get(KEY_USER_ID);

        tvHeading.setOnClickListener(this);
        bUpload.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v== tvHeading){
            //on attachment icon click
            showFileChooser();
        }
        if(v== bUpload){
            //on upload button Click
            if(selectedFilePath != null){
                tvHeading.setText("Uploading File...");
                // creating new thread to handle Http Operations
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadFile(selectedFilePath);
                    }
                }).start();
            }else{
                Toast.makeText(getApplicationContext(),"Please choose a File First",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose file with your answer.."),PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }
                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(getApplicationContext(),selectedFileUri);
                Log.i(TAG,"Selected File Path:" + selectedFilePath);

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    tvHeading.setText("File details");
                    try {
                        File file = new File(selectedFilePath);
                        pdfAttachment.fromFile(file)
                                .defaultPage(0)
                                .enableSwipe(true)
                                .onLoad(this)
                                .onPageChange(this)
                                .load();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //android upload file to server
    public void uploadFile(final String selectedFilePath){
        int serverResponseCode = 0;
        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);

        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
//            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvHeading.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(dataUrl);

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("file_ans",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user_id\""
                        + lineEnd);
                dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8"
                        + lineEnd);
                dataOutputStream.writeBytes("Content-Length: " + userId.length() + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(userId + lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"exam_id\""
                        + lineEnd);
                dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8"
                        + lineEnd);
                dataOutputStream.writeBytes("Content-Length: " + examId.length() + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(examId + lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file_ans\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    Log.d("__________________-", stringBuilder.toString());
                    try {
                        JSONObject jObj = new JSONObject(stringBuilder.toString());
                        boolean error = jObj.getBoolean("error");
                        final String message;
                        if (error) {
                            message = jObj.getString("error_msg");
                        }else{
                            message = jObj.getString("success_msg");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(getApplicationContext(), MarksActivity.class));
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvHeading.setTextSize(14);
                                tvHeading.setText(message);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        });

                    } catch (final Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvHeading.setText(e.getMessage());
                                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (final Exception e) {
                e.printStackTrace();
                Log.d("----Error_____", e.getMessage());
//                tvFileName.setText("Error: "+e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Error occured "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }
}
