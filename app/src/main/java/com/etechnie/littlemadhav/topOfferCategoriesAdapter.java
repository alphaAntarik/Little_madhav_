package com.etechnie.littlemadhav;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class topOfferCategoriesAdapter extends RecyclerView.Adapter<topOfferCategoriesAdapter.holder_topOfferCategories>{
    public List<datamodel_top_offer_categories_rv> list_brands;
    public topOfferCategoriesAdapter.ItemClickListener_topOfferCategories clickListener_topOfferCategories;

    public topOfferCategoriesAdapter(List<datamodel_top_offer_categories_rv> list_brands, topOfferCategoriesAdapter.ItemClickListener_topOfferCategories clickListener_topOfferCategories) {
        this.list_brands = list_brands;
        this.clickListener_topOfferCategories = clickListener_topOfferCategories;
    }

    @NonNull
    @Override
    public holder_topOfferCategories onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_row_top_offer_categories,parent,false);
        return new topOfferCategoriesAdapter.holder_topOfferCategories(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder_topOfferCategories holder, @SuppressLint("RecyclerView") int position) {
        holder.categorytxtt.setText(list_brands.get(position).getCategory_name());

        Picasso.with(getApplicationContext()).load("https://img.freepik.com/free-psd/fashion-poster-template_23-2148838187.jpg?w=826&t=st=1663390937~exp=1663391537~hmac=c6972505169d67aca95ddc46f04e9871e9e1a4f13f0138cac94269836e83f5ec").into(holder.categoryImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {clickListener_topOfferCategories.onItemClicked_topOfferCategories(list_brands.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return list_brands.size();
    }

    class holder_topOfferCategories extends RecyclerView.ViewHolder{
        ImageView categoryImage;
        TextView categorytxtt;
        public holder_topOfferCategories(@NonNull View itemView) {
            super(itemView);
            categorytxtt = (TextView) itemView.findViewById(R.id.CategoryName);
            categoryImage = (ImageView) itemView.findViewById(R.id.CategoryImage);
        }
    }
    public interface  ItemClickListener_topOfferCategories{
        public void onItemClicked_topOfferCategories(datamodel_top_offer_categories_rv datamodel_top_offer_categories_rv);

    }
}
