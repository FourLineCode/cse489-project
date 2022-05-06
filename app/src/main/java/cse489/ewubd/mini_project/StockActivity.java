package cse489.ewubd.mini_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class StockActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    ArrayList<Map<String, Object>> stocks = new ArrayList();
    double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_layout);

        findViewById(R.id.addNewProduct).setOnClickListener(view -> {
            Intent i = new Intent(StockActivity.this, AddProductActivity.class);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.stocks.clear();
        this.totalAmount = 0;

        db.collection("stocks")
                .whereEqualTo("ownerId", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                stocks.add(data);
                                totalAmount += Integer.parseInt(data.get("quantity").toString()) * Double.parseDouble(data.get("productPrice").toString());
                            }

                            TextView amount = findViewById(R.id.stockamount);
                            TextView items = findViewById(R.id.numberofitems);
                            StockListAdapter adapter = new StockListAdapter(StockActivity.this, stocks);

                            RecyclerView list = findViewById(R.id.stockList);
                            list.setLayoutManager(new LinearLayoutManager(StockActivity.this));
                            list.setAdapter(adapter);

                            items.setText(String.valueOf(stocks.size()));
                            amount.setText(String.valueOf(String.format("%.2f", totalAmount)) + " ৳");
                        } else {
                            Toast.makeText(StockActivity.this, "Error getting stocks", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}