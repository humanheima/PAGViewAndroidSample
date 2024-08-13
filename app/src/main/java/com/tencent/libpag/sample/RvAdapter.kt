package com.tencent.libpag.sample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tencent.libpag.sample.RvAdapter.VH
import com.tencent.libpag.sample.libpag_sample.CustomView
import com.tencent.libpag.sample.libpag_sample.R

/**
 * Created by p_dmweidu on 2023/2/7
 * Desc:
 */
class RvAdapter : RecyclerView.Adapter<VH>() {

    private var datas: List<String>? = null

    private var context: Context? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): VH {
        if (context == null) {
            context = viewGroup.context
        }
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_rv_item, viewGroup, false)
        return VH(view)
    }

    override fun onBindViewHolder(vh: VH, position: Int) {
        vh.tvTitle.text = "pos $position"
        vh.customView.updatePagColor(ContextCompat.getColor(context!!, R.color.colorAccent))
    }

    override fun getItemCount(): Int {
        return if (datas == null) {
            0
        } else datas!!.size
    }

    fun setDatas(datas: List<String>?) {
        this.datas = datas
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val customView: CustomView = itemView.findViewById(R.id.custom_view)

    }
}