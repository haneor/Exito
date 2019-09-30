package kr.co.exito_com.www.exito.recycleview_item;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.exito_com.www.exito.List_ItemActivity;
import kr.co.exito_com.www.exito.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context context;

    ArrayList<String> his_list_time;
    ArrayList<String> his_list_phone;
    ArrayList<String> his_list_start;
    ArrayList<String> his_list_get;
    ArrayList<String> his_list_space;
    ArrayList<String> his_list_price;
    ArrayList<String> list_index;

    public HistoryAdapter(Context context, ArrayList<String> his_list_time, ArrayList<String> his_list_phone, ArrayList<String> his_list_start,
                          ArrayList<String> his_list_get, ArrayList<String> his_list_space, ArrayList<String> his_list_price, ArrayList<String> list_index) {
        super();
        this.context = context;
        this.his_list_time = his_list_time;
        this.his_list_phone = his_list_phone;
        this.his_list_start = his_list_start;
        this.his_list_get = his_list_get;
        this.his_list_space = his_list_space;
        this.his_list_price = his_list_price;
        this.list_index = list_index;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history_recycler_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.his_text_time.setText("생성시간 : " + his_list_time.get(i));
        viewHolder.his_text_phone.setText("전화번호 : " + his_list_phone.get(i));
        viewHolder.his_text_start.setText("출발지 : " + his_list_start.get(i));
        viewHolder.his_text_get.setText("도착지 : " + his_list_get.get(i));
        viewHolder.his_text_space.setText("거리 : " + his_list_space.get(i)+"Km");
        viewHolder.his_text_price.setText("결제요금 : " + his_list_price.get(i)+"원");
        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    Intent intent = new Intent(context, List_ItemActivity.class);
                    intent.putExtra("item_index", list_index.get(i));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return his_list_start.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView his_text_time;
        public TextView his_text_phone;
        public TextView his_text_start;
        public TextView his_text_get;
        public TextView his_text_space;
        public TextView his_text_price;

        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            his_text_time = itemView.findViewById(R.id.history_item_time);
            his_text_phone = itemView.findViewById(R.id.history_item_phone);
            his_text_start = itemView.findViewById(R.id.history_item_start);
            his_text_get = itemView.findViewById(R.id.history_item_get);
            his_text_space = itemView.findViewById(R.id.history_item_space);
            his_text_price = itemView.findViewById(R.id.history_item_price);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);

        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }

}