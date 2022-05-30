package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StockInfo extends AppCompatActivity implements View.OnClickListener{

    Button scanBtn;

    private TextView mTextViewResult;
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);

        scanBtn = findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);
        mTextViewResult = findViewById(R.id.text_view_result);

        mQueue = Volley.newRequestQueue(this);

    }



    private void getStockInfo(String barcode) {
        //https://www.youtube.com/watch?v=y2xtLqP8dSQ

        String url = "http://172.20.10.2:8080/api/v1/stockinfo/"+barcode;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {
                        JSONArray jsonArray = response.getJSONArray("article");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            mTextViewResult.setText("");
                            JSONObject employee = jsonArray.getJSONObject(i);

                            String name = employee.getString("name");
                            String articleCode = employee.getString("articleCode");
                            String location = employee.getString("location");
                            String amount = employee.getString("amount");

                            mTextViewResult.append("Name: " + name + "\n");
                            mTextViewResult.append("Barcode: " + articleCode + "\n");
                            mTextViewResult.append("Location: " + location + "\n");
                            mTextViewResult.append("Stock amount: " + amount + "\n");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            mTextViewResult.setText("Error " + error.getMessage());
            error.printStackTrace();
        });

        mQueue.add(request);
    }

    @Override
    public void onClick(View view) {
        scanCode();
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                getStockInfo(result.getContents().toString());
                closeOptionsMenu();

            } else {
                Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}