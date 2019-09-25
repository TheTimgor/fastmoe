package moe.timgor.fastmoe;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import androidx.annotation.Nullable;

public class Downloadimg extends Service {

    private Downloadimg ctx;
    private String TAG = "Download img";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: create");

        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        String url = "https://timgor.moe/api/random/";

        ctx = this;

        StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.d(TAG, "onResponse");
                new Thread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "run | response: "+response);
                        URL url = null;
                        try {
                            url = new URL(response);
                            HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
                            InputStream is = connection.getInputStream();
                            Bitmap img = BitmapFactory.decodeStream(is);
                            File file = SaveImage(img);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                final Uri contentUri = Uri.fromFile(file);
                                scanIntent.setData(contentUri);
                                sendBroadcast(scanIntent);
                            } else {
                                final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                                sendBroadcast(intent);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

                Toast.makeText(ctx, "Downloaded", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                error.printStackTrace();
                Toast.makeText(ctx, "Something's gone horribly wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        ExampleRequestQueue.add(ExampleStringRequest);
        stopSelf();
    }
    private File SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        Log.d(TAG, "SaveImage | root: "+root);
        File myDir = new File(root + "/saved_images");
        if (!myDir.exists()) {
            Log.d(TAG, "SaveImage | made dir: "+myDir.mkdirs());
        }
        Log.d(TAG, "SaveImage | save dir exists: "+myDir.exists());
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
