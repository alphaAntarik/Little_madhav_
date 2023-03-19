package com.etechnie.littlemadhav;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etechnie.littlemadhav.customs.TextAnimationRightToLeft;
import com.payumoney.sdkui.ui.adapters.RecyclerViewAdapter;

import java.util.List;

public class catagory_home_adapter_rv extends RecyclerView.Adapter<catagory_home_adapter_rv.holder>{

    public List<dataModel_category_rv> list;
    public ItemClickListener clickListener;
    public catagory_home_adapter_rv(List<dataModel_category_rv> list,ItemClickListener clickListener){
        this.list = list;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.category_head_single_row,viewGroup,false);
        return new holder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull holder holder, @SuppressLint("RecyclerView") int i) {
            holder.tv.setText(list.get(i).getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {clickListener.onItemClicked(list.get(i));

                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class holder extends RecyclerView.ViewHolder{

        TextView tv;

        public holder(@NonNull View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.category_txt);
        }}
        public interface  ItemClickListener{
            public void onItemClicked(dataModel_category_rv dataModel_category_rv);

    }

}
