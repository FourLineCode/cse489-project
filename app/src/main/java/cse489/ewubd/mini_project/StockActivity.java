package cse489.ewubd.mini_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class StockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_layout);

        findViewById(R.id.addNewProduct).setOnClickListener(view -> {
            Intent i = new Intent(StockActivity.this, AddProductActivity.class);
            startActivity(i);
        });
    }
}