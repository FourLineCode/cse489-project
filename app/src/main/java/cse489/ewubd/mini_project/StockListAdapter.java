package cse489.ewubd.mini_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Map<String, Object>> stocks;

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
    public void onBindViewHolder(@NonNull StockListAdapter.ViewHolder holder, int position) {
        Map<String, Object> stock = this.stocks.get(position);

        holder.no.setText(String.valueOf(position + 1));
        holder.name.setText(stock.get("productName").toString());
        holder.price.setText(stock.get("productPrice").toString() + " ৳");
        holder.quantity.setText(stock.get("quantity").toString());
        holder.productId.setText(stock.get("productId").toString());
        Picasso.get().load(stock.get("productImageUrl").toString()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return this.stocks.size();
    }
}
