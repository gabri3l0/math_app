package com.example.mathapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    // Makes an api post to predict with knn the numbers
    public void checkResults(View view){

        // get Image
        ImageView image =(ImageView)findViewById(R.id.image);
        // encode image to base64 string
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sesenta);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        // image to string
        final String imageString = Base64.encodeToString(imageBytes, Base64.NO_CLOSE);

        final TextView textView = (TextView) findViewById(R.id.text);
        // instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

//        String url ="http://192.168.1.83:5000/getMathAnswer";
        String url ="https://math-engine-api.herokuapp.com/getMathAnswer";

        // request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // display the first 500 characters of the response string.
                        textView.setText("Response is: "+response);
                        Log.e("DEBUGAPIPOST", response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!"+ error);
                Log.e("DEBUGAPIPOST ERROR", error.toString());
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";//set here instead
            }
            @Override
            public byte[] getBody() {
                try {
                    Map<String, String> params = new HashMap<>();
                    params.put("base64", imageString);
                    JSONObject json = new JSONObject(params);
                    String requestBody = json.toString();
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };

        // add timeout request
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // add the request to the RequestQueue.
        queue.add(stringRequest);

    }

}
