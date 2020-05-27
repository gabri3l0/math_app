package com.example.mathapp;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Game extends AppCompatActivity {

    private Button erase, save;
    private TextView number1, number2, operation;
    private PaintView paintView;
    private String respuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        paintView = (PaintView) findViewById(R.id.drawing);
        erase = (Button) findViewById(R.id.erase_btn);
        save = (Button) findViewById(R.id.save_btn);
        number1 = (TextView) findViewById(R.id.a);
        number2 = (TextView) findViewById(R.id.b);
        operation = (TextView) findViewById(R.id.operation);
        randomTest();
    }


    public void onClick(View v)  {

        if(v.getId()==R.id.erase_btn){
            paintView.setErase(true);
        }

        if(v.getId()==R.id.save_btn){

            // Progress Bar
            final ProgressDialog nDialog;
            nDialog = new ProgressDialog(Game.this);
            nDialog.setMessage("Espere...");
            nDialog.setTitle("C-3P0 está calculando");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();

            // get Image by PaintView
            paintView.setDrawingCacheEnabled(true);
            Bitmap bitmap = paintView.getDrawingCache();

            // encode image to base64 string
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();

            // image to string
            final String imageString = Base64.encodeToString(imageBytes, Base64.NO_CLOSE);

            // instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);

            // Endpoint API
            String url ="http://192.168.1.83:5000/getMathAnswer";
            //String url ="https://math-engine-api.herokuapp.com/getMathAnswer";


            // Get screen size
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            final int width = size.x;
            final int height = size.y;

            // request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Get answer
                            respuesta = response;

                            //Check Result
                            checkResult();

                            // Exit progress bar
                            nDialog.dismiss();

                            // New test
                            randomTest();

                            Log.e("DEBUGAPIPOST", response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    nDialog.dismiss();
                    randomTest();
                    Log.e("DEBUGAPIPOST ERROR", error.toString());
                }
            }){
                // Set headers
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";//set here instead
                }
                // Make json
                @Override
                public byte[] getBody() {
                    try {
                        Map<String, String> params = new HashMap<>();
                        params.put("base64", imageString);
                        params.put("width", Integer.toString(width));
                        params.put("height", Integer.toString(height));
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

            paintView.destroyDrawingCache();
        }
    }


    public void randomTest(){
        // Select operator
        String [] operations = {" + "," - "," * "," / "};
        operation.setText(operations[new Random().nextInt(operations.length)]);

        // Select numbers
        number1.setText(Integer.toString(new Random().nextInt(10)));
        number2.setText(Integer.toString(new Random().nextInt(10)));
    }

    public void checkResult(){

        int number1Int = Integer.parseInt(number1.getText().toString());
        int number2Int = Integer.parseInt(number2.getText().toString());
        int answer = Integer.parseInt(respuesta);

        boolean flag = false;

        // Operation
        switch (operation.getText().toString()){
            case(" + "):
                if (number1Int+number2Int==answer){
                    flag = true;
                }
            case(" - "):
                if (number1Int-number2Int==answer){
                    flag = true;
                }
            case(" * "):
                if (number1Int*number2Int==answer){
                    flag = true;
                }
            case(" / "):
                if (number1Int/number2Int==answer){
                    flag = true;
                }
        }
        // Answer announcement
        if (flag) {
            Toast.makeText(this, "CORRECTO",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "EQUIVOCADO",
                    Toast.LENGTH_LONG).show();
        }

        // Reset paintView
        paintView.setErase(true);
    }
}
