package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiverOrderOverview extends AppCompatActivity {

    private TextView articleTextView;
    private TextView expectedAmountTextView;
    private TextView errorMessage;
    private RequestQueue mQueue;
    private EditText actualAmountValue;
    private EditText locationValue;
    private EditText userValue;

    private Button postRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_order_overview);

        articleTextView = findViewById(R.id.article_input);
        errorMessage = findViewById(R.id.errorMessageLine);
        expectedAmountTextView = findViewById(R.id.expected_amount_value);
        actualAmountValue = findViewById(R.id.actual_amount_input);
        mQueue = Volley.newRequestQueue(this);
        postRequest=findViewById(R.id.post_request);


        postRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String id = extras.getString("id");
                    postRequest(id);
                }

                ReceiverOrderOverview.super.onBackPressed();
//                Intent intent = new Intent(ReceiverOrderOverview.this, OrderDetail.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed(){

    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String article = extras.getString("article");
            String expectedAmount = extras.getString("expectedAmount");
            getOrderDetail(article, expectedAmount);
        }
    }

    public void getOrderDetail(String article, String expectedAmount) {
        articleTextView.append(article);
        expectedAmountTextView.append(expectedAmount);
        actualAmountValue.setText(expectedAmount);
    }

    private void postRequest(String id) {
        // https://github.com/hackstarsj/VolleyTutorialGetAndPost_Android/blob/master/app/src/main/java/com/example/volleytutorialgetandpost/MainActivity.java
        String url="http://172.20.10.2:8080/api/v1/orders/";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //let's parse json data
                try {

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    // request body goes here
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("id", id);
                    jsonBody.put("receivedQuantity", actualAmountValue.getText().toString());
                    jsonBody.put("location", 1);
                    jsonBody.put("user", 1);
                    String requestBody = jsonBody.toString();
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    return null;
                }
            }
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                Map<String,String> params=new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        };
        mQueue.add(stringRequest);
    }


    private List<LocationDto> getLocations() {

        List<LocationDto> locationDtoList = new ArrayList<>();
        String url = "http://172.20.10.2:8080/api/v1/locations/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {
                        JSONArray jsonArray = response.getJSONArray("locations");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);
                            String id = employee.getString("id");
                            String locationName = employee.getString("locationName");
                            locationDtoList.add(new LocationDto(id, locationName));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        });
        mQueue.add(request);
        return locationDtoList;
    }
    private List<UserDto> getUsers() {

        List<UserDto> userList = new ArrayList<>();
        String url = "http://172.20.10.2:8080/api/v1/users/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {
                        JSONArray jsonArray = response.getJSONArray("users");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);
                            String id = employee.getString("id");
                            String userName = employee.getString("userName");
                            userList.add(new UserDto(id, userName));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        });
        mQueue.add(request);
        return userList;
    }
}