package com.etechnie.littlemadhav;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class topPicksAdapter extends RecyclerView.Adapter<topPicksAdapter.holder_topPicks>{
    public List<datamodel_top_picks_rv> list_brands;
    public topPicksAdapter.ItemClickListener_topPicks clickListener_topPicks;

    public topPicksAdapter(List<datamodel_top_picks_rv> list_brands, topPicksAdapter.ItemClickListener_topPicks clickListener_topPicks) {
        this.list_brands = list_brands;
        this.clickListener_topPicks = clickListener_topPicks;
    }

    @NonNull
    @Override
    public holder_topPicks onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_row_top_picks,parent,false);
        return new topPicksAdapter.holder_topPicks(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder_topPicks holder, @SuppressLint("RecyclerView") int position) {
        Picasso.with(getApplicationContext()).load("https://img.freepik.com/free-psd/fashion-poster-template_23-2148838187.jpg?w=826&t=st=1663390937~exp=1663391537~hmac=c6972505169d67aca95ddc46f04e9871e9e1a4f13f0138cac94269836e83f5ec").into(holder.topPicksImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {clickListener_topPicks.onItemClicked_topPicks(list_brands.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list_brands.size();
    }

    class holder_topPicks extends RecyclerView.ViewHolder{
        ImageView topPicksImage;

        public holder_topPicks(@NonNull View itemView) {
            super(itemView);
            topPicksImage = (ImageView) itemView.findViewById(R.id.topPickImage);
        }
    }
    public interface  ItemClickListener_topPicks{
        public void onItemClicked_topPicks(datamodel_top_picks_rv datamodel_top_picks_rv);

    }
}
