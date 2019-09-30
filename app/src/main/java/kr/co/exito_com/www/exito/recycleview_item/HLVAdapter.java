package kr.co.exito_com.www.exito.recycleview_item;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import java.util.ArrayList;

import kr.co.exito_com.www.exito.List_ItemActivity;
import kr.co.exito_com.www.exito.R;

public class HLVAdapter extends RecyclerView.Adapter<HLVAdapter.ViewHolder> {

    Context context;

    ArrayList<String> list_start;
    ArrayList<String> list_get;
    ArrayList<String> list_space;
    ArrayList<String> list_index;

    public HLVAdapter(Context context, ArrayList<String> list_start, ArrayList<String> list_get, ArrayList<String> list_space, ArrayList<String> list_index) {
        super();
        this.context = context;
        this.list_start = list_start;
        this.list_get = list_get;
        this.list_space = list_space;
        this.list_index = list_index;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.text_start.setText("출발지 : "+list_start.get(i));
        viewHolder.text_get.setText("도착지 : "+list_get.get(i));
        viewHolder.text_space.setText("거리 : "+list_space.get(i)+" Km");
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
        return list_start.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView text_start;
        public TextView text_get;
        public TextView text_space;

        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            text_start = itemView.findViewById(R.id.list_text_start);
            text_get = itemView.findViewById(R.id.list_text_get);
            text_space = itemView.findViewById(R.id.list_text_space);
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
