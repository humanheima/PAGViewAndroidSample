package com.tencent.libpag.sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Recycler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tencent.libpag.sample.libpag_sample.CustomView;
import com.tencent.libpag.sample.libpag_sample.R;
import java.util.List;
import org.w3c.dom.Text;


/**
 * Created by p_dmweidu on 2023/2/7
 * Desc:
 */
public class RvAdapter extends RecyclerView.Adapter<RvAdapter.VH> {


    private List<String> datas;

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_rv_item, viewGroup, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(RvAdapter.VH vh, int position) {
        vh.tvTitle.setText("position "+position);
    }

    @Override
    public int getItemCount() {
        if (datas == null) {
            return 0;
        }
        return datas.size();
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }


    class VH extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private CustomView customView;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            customView = itemView.findViewById(R.id.custom_view);
        }
    }
}