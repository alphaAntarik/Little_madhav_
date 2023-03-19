package com.etechnie.littlemadhav;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etechnie.littlemadhav.adapters.Brand_adapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Deals_adapter extends RecyclerView.Adapter<Deals_adapter.holder_deals>{
    public List<datamodel_deals_rv> list_brands;
    public Deals_adapter.ItemClickListener_deals clickListener_deals;

    public Deals_adapter(List<datamodel_deals_rv> list_brands, Deals_adapter.ItemClickListener_deals clickListener_delas) {
        this.list_brands = list_brands;
        this.clickListener_deals = clickListener_deals;
    }

    @NonNull
    @Override
    public Deals_adapter.holder_deals onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_row_best_offers_in_top_brands,parent,false);
        return new Deals_adapter.holder_deals(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Deals_adapter.holder_deals holder, @SuppressLint("RecyclerView") int position) {
        Picasso.with(getApplicationContext()).load("https://img.freepik.com/free-vector/fashion-sale-template-concept_52683-33763.jpg?w=826&t=st=1663384127~exp=1663384727~hmac=b3ec4eccde9208fb2ae2194b0392cb1dfc3222d9d34854eb52e48d359ca2159b").into(holder.dealImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {clickListener_deals.onItemClicked_deals(list_brands.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return list_brands.size();
    }
    class holder_deals extends RecyclerView.ViewHolder{
        ImageView dealImage;
        ImageButton arrow_;

        public holder_deals(@NonNull View itemView) {
            super(itemView);
            dealImage = (ImageView) itemView.findViewById(R.id.dealImage);
            arrow_ = (ImageButton) itemView.findViewById(R.id.arrow_button);
        }
    }
    public interface  ItemClickListener_deals{
        public void onItemClicked_deals(datamodel_deals_rv datamodel_deals_rv);

    }
}
