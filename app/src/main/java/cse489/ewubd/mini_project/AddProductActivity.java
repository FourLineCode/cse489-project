package cse489.ewubd.mini_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    ArrayList<Map<String, Object>> products = new ArrayList();
    ArrayList<String> names = new ArrayList();

    Spinner spinner;
    TextView productId;
    TextView productPrice;
    ImageView productImage;
    EditText quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_products);

        spinner = findViewById(R.id.productNameSpinner);
        productId = findViewById(R.id.productId);
        productPrice = findViewById(R.id.productPrice);
        productImage = findViewById(R.id.productImage);
        quantity = findViewById(R.id.productQuantity);

        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                products.add(data);
                                names.add((String) data.get("name"));
                            }

                            ArrayAdapter<String> aa = new ArrayAdapter(AddProductActivity.this, android.R.layout.simple_spinner_item, names);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(aa);
                            spinner.setOnItemSelectedListener(AddProductActivity.this);
                        } else {
                            System.out.println("Error getting firebase data");
                        }
                    }
                });

        findViewById(R.id.backButton).setOnClickListener(view -> {
            finish();
        });

        findViewById(R.id.saveButton).setOnClickListener(view -> {
            String q = quantity.getText().toString().trim();
            if(q.isEmpty()) {
                Toast.makeText(this, "Quantity cannot be empty", Toast.LENGTH_LONG).show();
                return;
            }


        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        productId.setText(products.get(i).get("id").toString());
        productPrice.setText(products.get(i).get("price").toString());
        Picasso.get().load(products.get(i).get("imageUrl").toString()).into(productImage);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}