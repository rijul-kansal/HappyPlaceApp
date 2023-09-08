package com.example.happyplaceapp.activity.activity.Adapter


import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaceapp.activity.activity.Model.HappyPlaceModel
import com.example.happyplaceapp.databinding.RecycleViewForShowingDataBinding

class HappyPlaceAdapter(
    private val items: ArrayList<HappyPlaceModel>
) :
    RecyclerView.Adapter<HappyPlaceAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecycleViewForShowingDataBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder?.img?.setImageURI(Uri.parse(item.image))
        holder.title?.text=item.title
        holder.des?.text=item.description
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, item )
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
    fun setOnClickListener(onClickListener: HappyPlaceAdapter.OnClickListener) {
        this.onClickListener = onClickListener
    }
    interface OnClickListener {
        fun onClick(position: Int, model: HappyPlaceModel)
    }
    class ViewHolder(binding: RecycleViewForShowingDataBinding) : RecyclerView.ViewHolder(binding.root) {
        val img=binding?.addmage
        val title=binding?.addtitle
        val des=binding?.adddes
    }
}