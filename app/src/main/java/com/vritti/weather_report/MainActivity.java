package com.vritti.weather_report;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {
    String regionCode ="N/A", Wparam="N/A", Year="N/A", Key="N/A", Value="N/A";
    String fileUrl,fileName,dwnlod;
    private static final int MEGABYTE = 1024 * 1024;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 123;

    String[] region = {"UK"/*,"England"*/};
    String[] param = {"Tmax"/*,"Tmin","Tmean","Sunshine","Rainfall","Raindays1mm","AirFrost"*/};

    String[] year = new String[107];
    String[] key = new String[17];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndroidVersion();

    }

    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();

        } else {
            GoForDownLoad();
        }

    }

   /* private void checkPermission() {
        if (((ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE))!= PackageManager.PERMISSION_GRANTED)
            &&((ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE))!= PackageManager.PERMISSION_GRANTED)
                &&(( ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION))!= PackageManager.PERMISSION_GRANTED)
                &&(( ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION))!= PackageManager.PERMISSION_GRANTED)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (getParent(), Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (getParent(), Manifest.permission.WRITE_EXTERNAL_STORAGE)&&
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (getParent(), Manifest.permission.ACCESS_COARSE_LOCATION)&&
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (getParent(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                Snackbar.make(getParent().findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{Manifest.permission
                                                .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_ID_MULTIPLE_PERMISSIONS);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission
                                .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            // write your logic code if permission already granted
            GoForDownLoad();
        }
    }*/

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {

            Toast toast = Toast.makeText(this, "PLEASE ALLOW ALL THE PERMISSIONS", Toast.LENGTH_SHORT);
            View view = toast.getView();
            //To change the Background of Toast
            view.setBackgroundColor(Color.RED);
            // toast.show();

            //Permissions
            int permissionReadPhone = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
            int permissionFinelocation = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionWrtiestorage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionCoarseLocation = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int permissionRecordaudio = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO);
            int permissionGetaccount = ContextCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS);
            int permissionReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            //Permissions end


            List<String> listPermissionsNeeded = new ArrayList<>();
            /*if (permissionReadPhone != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE);
            }*/
            if (permissionFinelocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissionWrtiestorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*if (permissionRecordaudio != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.RECORD_AUDIO);
            }*/
            /*if (permissionGetaccount != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.GET_ACCOUNTS);
            }*/
            if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            /*if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }*/
            if (!listPermissionsNeeded.isEmpty()) {
                toast.show();
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                //return false;
            }
            if (listPermissionsNeeded.isEmpty()) {
                //return true;
                GoForDownLoad();
            }
            //return false;
        } else {
            //return true;
            GoForDownLoad();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean FineLocation = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean CoarseLocation = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalFile = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(FineLocation && CoarseLocation && writeExternalFile && readExternalFile)
                    {
                        // write your logic here
                        GoForDownLoad();
                    } else {
                        Snackbar.make(getParent().findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(
                                                new String[]{Manifest.permission
                                                        .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                                REQUEST_ID_MULTIPLE_PERMISSIONS);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }
    private void GoForDownLoad() {
        for (int i=0; i<region.length;i++){
            for(int j=0 ; j<param.length; j++){
                regionCode = region[i];
                Wparam = param[j];
                fileUrl ="http://www.metoffice.gov.uk/pub/data/weather/uk/climate/datasets/"+
                        Wparam+"/date/"+regionCode+".txt";
                fileName = regionCode+"_"+Wparam+".txt";
                //GetFileDownload(fileUrl, fileName);
                new DownloadFile().execute(fileUrl, fileName);
            }
        }
    }



    /*public String downloadFile(String fileUrl, File directory) {
        String isdownload;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				*//*urlConnection.setRequestMethod("GET");
				urlConnection.setDoOutput(true);*//*
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
            isdownload =  "File Downloaded Successfully";
        } catch (FileNotFoundException e) {
            isdownload =  "No File Found";
            e.printStackTrace();
        } catch (MalformedURLException e) {
            isdownload =  "No File Found";
            e.printStackTrace();
        } catch (IOException e) {
            isdownload =  "No File Found";
            e.printStackTrace();
        } catch (Exception e) {
            isdownload =  "No File Found";
            e.printStackTrace();
        }
        return isdownload;
    }
*/
   /* private void GetFileDownload(String fileUrl, String fileName) {
        //String fileUrl = strings[0];   // -> http://www.androhub.com/demo/demo.pdf
        //String fileName = strings[1];  // -> demo.pdf
        String[] words = fileName.split("\\.");
        String FileName = words[0];
        String suffix = words[1];

        File storageDir = new File(Environment.getExternalStorageDirectory(), "WeatherReport");
        if (!storageDir.exists()){  // Checks that Directory/Folder Doesn't Exists!
            boolean result = storageDir.mkdir();
            Toast.makeText(getApplicationContext(),result+"",Toast.LENGTH_SHORT).show();
        }
        File pdfdown ;//= new File(storageDir+"/"+fileName);
        try {
            pdfdown = File. createTempFile( FileName *//* prefix *//*,"."+suffix, storageDir  *//* directory *//* );
            //pdfdown.createNewFile();
            dwnlod = downloadFile(fileUrl, pdfdown);
        } catch (IOException e) {
            dwnlod = "No File Found";
            e.printStackTrace();
        }
    }*/

    private class DownloadFile extends AsyncTask<String, Void, Void> {
        String dwnlod;

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://www.androhub.com/demo/demo.pdf
            String fileName = strings[1];  // -> demo.pdf
            String[] words = fileName.split("\\.");
            String FileName = words[0];
            String suffix = words[1];

            File storageDir = new File(Environment.getExternalStorageDirectory(), "WeatherReport");
            if (!storageDir.exists()){  // Checks that Directory/Folder Doesn't Exists!
                boolean result = storageDir.mkdir();
            }
            File pdfdown = new File(storageDir+"/"+fileName);
            try {
               // pdfdown = File. createTempFile( FileName  prefix ,"."+suffix, storageDir   directory  );
                pdfdown.createNewFile();
                dwnlod = downloadFile(fileUrl, pdfdown);
            } catch (IOException e) {
                dwnlod = "No File Found";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),dwnlod,Toast.LENGTH_SHORT).show();
        }

        private static final int MEGABYTE = 1024 * 1024;

        public String downloadFile(String fileUrl, File directory) {
            String isdownload;
            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setDoOutput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
                isdownload =  "File Downloaded Successfully";
            } catch (FileNotFoundException e) {
                isdownload =  "No File Found";
                e.printStackTrace();
            } catch (MalformedURLException e) {
                isdownload =  "No File Found";
                e.printStackTrace();
            } catch (IOException e) {
                isdownload =  "No File Found";
                e.printStackTrace();
            } catch (Exception e) {
                isdownload =  "No File Found";
                e.printStackTrace();
            }
            return isdownload;
        }

    }
}
