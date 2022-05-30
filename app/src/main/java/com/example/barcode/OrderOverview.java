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

public class OrderOverview extends AppCompatActivity {

    ListView listView;
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview);

        listView = (ListView) findViewById(R.id.listview);
        mQueue = Volley.newRequestQueue(this);


    }


    @Override
    protected void onStart() {
        super.onStart();

        getOrders();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        getOrders();
    }


    public void getOrders() {
        List<String> orderList = new ArrayList<>();

        String url = "http://172.20.10.2:8080/api/v1/orders";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("orders");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject order = jsonArray.getJSONObject(i);
                            String orderNumber = order.getString("orderNumber");
                            Log.i("OrderOverview", orderNumber);
                            orderList.add(orderNumber);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
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


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, orderList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(OrderOverview.this, orderList.get(i).toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OrderOverview.this, OrderDetail.class);
                intent.putExtra("orderNumber", orderList.get(i));
                startActivity(intent);

            }
        });
    }

}