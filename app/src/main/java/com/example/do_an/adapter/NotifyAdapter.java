package com.example.do_an.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an.R;
import com.example.do_an.model.NotifyModel;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ThongBaoViewHolder> {

    private List<NotifyModel> thongBaoList;

    public NotifyAdapter(List<NotifyModel> thongBaoList) {
        this.thongBaoList = thongBaoList;
    }

    @NonNull
    @Override
    public ThongBaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thongbao, parent, false);
        return new ThongBaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoViewHolder holder, int position) {
        NotifyModel item = thongBaoList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return thongBaoList.size();
    }

    public static class ThongBaoViewHolder extends RecyclerView.ViewHolder {
        private TextView title_thongbao, sotiengiaodich, ngaygiaodich, giogiaodich;

        public ThongBaoViewHolder(@NonNull View itemView) {
            super(itemView);
            title_thongbao = itemView.findViewById(R.id.title_thongbao);
            sotiengiaodich = itemView.findViewById(R.id.sotiengiaodich);
            ngaygiaodich = itemView.findViewById(R.id.ngaygiaodich);
            giogiaodich = itemView.findViewById(R.id.giogiaodich);
        }

        public void bind(NotifyModel item){
            title_thongbao.setText(item.getTitle() + " thành công");
            sotiengiaodich.setText(item.getPrice());
            ngaygiaodich.setText(item.getDate());
            giogiaodich.setText(item.getHour());
        }
    }

}
