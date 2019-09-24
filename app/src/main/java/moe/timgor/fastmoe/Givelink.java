package moe.timgor.fastmoe;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.Nullable;

public class Givelink extends Service {
    private final String TAG = "Give link";
    private Givelink ctx;
    private ClipboardManager clipboard;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: start");
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onCreate() {
        clipboard = (ClipboardManager)
                getSystemService(this.getApplicationContext().CLIPBOARD_SERVICE);

        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        String url = "https://timgor.moe/api/random/";

        ctx = this;

        StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                //You can test it by printing response.substring(0,500) to the screen.
                ClipData clip = ClipData.newPlainText("moe pic", response);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(ctx, "Copied!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Toast.makeText(ctx, "Something's gone horribly wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        ExampleRequestQueue.add(ExampleStringRequest);
        stopSelf();
    }
}
