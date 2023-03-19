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

public class colours_adapter extends RecyclerView.Adapter<colours_adapter.holder_colours>{
        public List<datamodel_colours_rv> list_colours;
        public colours_adapter.ItemClickListener_colours clickListener_colours;

    public colours_adapter(List<datamodel_colours_rv> list_colours, colours_adapter.ItemClickListener_colours clickListener_colours) {
        this.list_colours = list_colours;
        this.clickListener_colours = clickListener_colours;
    }

    @NonNull
    @Override
    public colours_adapter.holder_colours onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.colour_recycler_view_single_row,parent,false);
        return new colours_adapter.holder_colours(view);
    }

    @Override
    public void onBindViewHolder(@NonNull colours_adapter.holder_colours holder, @SuppressLint("RecyclerView") int position) {
        holder.colourtxtt.setText(list_colours.get(position).getColour_name());

        Picasso.with(getApplicationContext()).load("https://img.freepik.com/free-psd/fashion-poster-template_23-2148838187.jpg?w=826&t=st=1663390937~exp=1663391537~hmac=c6972505169d67aca95ddc46f04e9871e9e1a4f13f0138cac94269836e83f5ec").into(holder.colourImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {clickListener_colours.onItemClicked_colours(list_colours.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return list_colours.size();
    }

    class holder_colours extends RecyclerView.ViewHolder{
        ImageView colourImage;
        TextView colourtxtt;
        public holder_colours(@NonNull View itemView) {
            super(itemView);
            colourtxtt = (TextView) itemView.findViewById(R.id.ColourName);
            colourImage = (ImageView) itemView.findViewById(R.id.ColourImage);
        }
    }

    public interface  ItemClickListener_colours{
        public void onItemClicked_colours(datamodel_colours_rv datamodel_colours_rv);

    }
}
