package rw.akimana.officels.Controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import rw.akimana.officels.Models.IpAddress;
import rw.akimana.officels.R;

public class DownloadTask {

    private static final String TAG = "Download Task";
    private Context context;
//    private Button buttonText;
    private TextView tvTitle;
    private PDFView pdfView;
    private String downloadUrl = "", downloadFileName = "", dataUrl = "";
    HashMap<String, String> hashMap;
    DatabaseHelper helper;
    private String URL_PREFIX = "/officels/images/ass_files/";
    public DownloadTask(Context context, TextView tvTitle, PDFView pdfView, String fileNameFromUrl) {
        this.context = context;
//        this.buttonText = buttonText;
        this.tvTitle = tvTitle;
        this.pdfView = pdfView;

        helper = new DatabaseHelper(context);
        hashMap = helper.findIpDetails();
        if(hashMap == null) dataUrl = "http://192.168.0.122" + URL_PREFIX;
        else{
            String id = hashMap.get(IpAddress.IpAttributes.COL_ID);
            String protocal = hashMap.get(IpAddress.IpAttributes.COL_PROTOCAL);
            String ipAddress = hashMap.get(IpAddress.IpAttributes.COL_IPADDRESS);
            dataUrl = protocal + "://" + ipAddress + URL_PREFIX;
        }
        downloadFileName = downloadUrl.replace(dataUrl, "");//Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName);

        this.downloadUrl = dataUrl + fileNameFromUrl;
        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvTitle.setEnabled(false);
            tvTitle.setText(R.string.downloadStarted);//Set Button Text when download started
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
//                    buttonText.setEnabled(true);
//                    buttonText.setText(R.string.downloadCompleted);//If Download completed then change button text
                    pdfView.fromFile(outputFile)
                            .defaultPage(0)
                            .enableSwipe(true)
                            .onLoad((OnLoadCompleteListener) context)
                            .onPageChange((OnPageChangeListener) context)
                            .load();
                } else {
                    tvTitle.setText(R.string.downloadFailed);//If download failed change button text
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvTitle.setEnabled(true);
                            tvTitle.setText(R.string.downloadAgain);//Change button text again after 3sec
                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                tvTitle.setText(R.string.downloadFailed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvTitle.setEnabled(true);
                        tvTitle.setText(R.string.downloadAgain);
                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }finally {

            }
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(
                            Environment.getExternalStorageDirectory()+ "/"
                                    + Utils.downloadDirectory);
                } else
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }
}
