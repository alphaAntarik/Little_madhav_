package com.etechnie.littlemadhav.adapters;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etechnie.littlemadhav.R;
import com.etechnie.littlemadhav.catagory_home_adapter_rv;
import com.etechnie.littlemadhav.dataModel_category_rv;
import com.etechnie.littlemadhav.datamodel_brands_rv;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Brand_adapter extends RecyclerView.Adapter<Brand_adapter.holder_brands>{


    public List<datamodel_brands_rv> list_brands;
    public ItemClickListener_brands clickListener_brands;
    public Brand_adapter(List<datamodel_brands_rv> list_brands, ItemClickListener_brands clickListener_brands) {

        this.list_brands = list_brands;
        this.clickListener_brands = clickListener_brands;
    }

    @NonNull
    @Override
    public holder_brands onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.single_row_unique_brands,viewGroup,false);
        return new holder_brands(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder_brands holder_brands, @SuppressLint("RecyclerView") int i) {
        holder_brands.brandName.setText(list_brands.get(i).getBrand_name());
        holder_brands.price.setText(list_brands.get(i).getPrice());
        Picasso.with(getApplicationContext()).load("https://img.freepik.com/premium-photo/athletic-young-man-doing-athletic-body-isolated-nature-background-healthy-lifestyle-concept-vertical-view_246930-1610.jpg?w=740").into(holder_brands.brandImage);
        holder_brands.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {clickListener_brands.onItemClicked_brands(list_brands.get(i));

            }
        });

    }

    @Override
    public int getItemCount() {
        return list_brands.size();
    }

    class holder_brands extends RecyclerView.ViewHolder{
        TextView brandName;
        TextView price;
        ImageView brandImage;


        public holder_brands(@NonNull View itemView) {
            super(itemView);
            brandName = (TextView) itemView.findViewById(R.id.brandName);
            brandImage = (ImageView) itemView.findViewById(R.id.brandImage);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }

    public interface  ItemClickListener_brands{
        public void onItemClicked_brands(datamodel_brands_rv datamodel_brands_rv);

    }
}
