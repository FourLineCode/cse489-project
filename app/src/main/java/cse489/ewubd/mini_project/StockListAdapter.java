package cse489.ewubd.mini_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Map<String, Object>> stocks;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public StockListAdapter(Context ctx, ArrayList<Map<String, Object>> stocks) {
        this.context = ctx;
        this.stocks = stocks;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView no;
        TextView name;
        TextView price;
        TextView quantity;
        TextView productId;
        ImageView image;

        ViewHolder(View view) {
            super(view);
            this.no = view.findViewById(R.id.stockNo);
            this.name = view.findViewById(R.id.stockName);
            this.price = view.findViewById(R.id.stockPrice);
            this.quantity = view.findViewById(R.id.stockQuantity);
            this.productId = view.findViewById(R.id.stockProductId);
            this.image = view.findViewById(R.id.stockImage);
        }

        @Override
        public void onClick(View view) {
            return;
        }
    }


    @NonNull
    @Override
    public StockListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.adapter_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Map<String, Object> stock = this.stocks.get(position);

        holder.no.setText(String.valueOf(position + 1));
        holder.name.setText(stock.get("productName").toString());
        holder.price.setText(stock.get("productPrice").toString() + " ৳");
        holder.quantity.setText(stock.get("quantity").toString());
        holder.productId.setText(stock.get("productId").toString());
        Picasso.get().load(stock.get("productImageUrl").toString()).into(holder.image);

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(StockListAdapter.this.context, AddProductActivity.class);
            i.putExtra("productId", stock.get("productId").toString());
            i.putExtra("index", stock.get("itemIndex").toString());
            StockListAdapter.this.context.startActivity(i);
        });

        holder.itemView.setOnLongClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            db.collection("stocks").whereEqualTo("ownerId", currentUser.getUid()).whereEqualTo("productId", stock.get("productId")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    try {
                                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                        db.collection("stocks").document(doc.getId()).delete();
                                        StockListAdapter.this.stocks.remove(position);
                                        StockListAdapter.this.notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return this.stocks.size();
    }
}
