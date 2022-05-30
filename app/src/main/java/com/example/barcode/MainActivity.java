package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {

    Button stockInfo;
    Button orderOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stockInfo = findViewById(R.id.stockInfo);
        orderOverview = findViewById(R.id.orderOverview);

        stockInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStockInfo();
            }
        });

        orderOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOrderOverview();
            }
        });

    }

    private void openStockInfo() {
        Intent intent= new Intent(this, StockInfo.class);
        startActivity(intent);
    }
    private void openOrderOverview() {
        Intent intent = new Intent(this, OrderOverview.class);
        startActivity(intent);
    }

}