package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetail extends AppCompatActivity {
    ListView listView;
    private TextView errorMessage;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        listView = (ListView) findViewById(R.id.detailList);
        errorMessage = findViewById(R.id.errorMessageDetail);
        mQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("orderNumber");
            getOrderDetails(value);
        }
    }



    public void getOrderDetails(String orderNumber) {
        List<OrderDetailDto> orderList = new ArrayList<>();
        List<String> viewList = new ArrayList<>();


        String url = "http://172.20.10.2:8080/api/v1/orders/" + orderNumber;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("orderDetails");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject order = jsonArray.getJSONObject(i);
                            String article = order.getString("article");
                            String id = order.getString("id");
                            String expectedAmount = order.getString("expectedAmount");
                            Log.i("OrderDetail", id);
                            Log.i("OrderDetail", article);
                            Log.i("OrderDetail", expectedAmount);
                            errorMessage.append("");
                            orderList.add(new OrderDetailDto(id, article, expectedAmount));
                            viewList.add(article);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            errorMessage.setText("Error " + error.getMessage());
            error.printStackTrace();
        });

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        mQueue.add(request);



        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, viewList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(OrderDetail.this, orderList.get(i).getArticle(), Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(OrderDetail.this, ReceiverOrderOverview.class);
                intent.putExtra("id",orderList.get(i).getId());
                intent.putExtra("article",orderList.get(i).getArticle());
                intent.putExtra("expectedAmount",orderList.get(i).getExpectedAmount());
                startActivity(intent);
            }
        });
    }
}