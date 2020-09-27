package com.example.album.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.album.R
import com.example.album.databinding.ItemAlbumBinding
import com.example.album.databinding.ItemImageBinding
import com.example.model.bean.Image


class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private var imageList: ObservableArrayList<Image>
    //var listener: OnDeleteBtnClickListener

    //constructor(imageList: ObservableArrayList<Image>, listener : OnDeleteBtnClickListener) {
    constructor(imageList: ObservableArrayList<Image>) {
        this.imageList = imageList
        //this.listener = listener
        imageList.addOnListChangedCallback(dataChangeListener)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ImageViewHolder {
        var root = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_image, viewGroup, false)
        return ImageViewHolder(root)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(imageViewHolder: ImageViewHolder, position: Int) {
        val image: Image = imageList[position]
        imageViewHolder.bind(image)
    }


    private var dataChangeListener = object : ObservableList.OnListChangedCallback<ObservableArrayList<Image>>() {
        override fun onChanged(sender: ObservableArrayList<Image>?) {
        }

        override fun onItemRangeRemoved(sender: ObservableArrayList<Image>?, positionStart: Int, itemCount: Int) {
            this@ImageAdapter.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableArrayList<Image>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            this@ImageAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeInserted(sender: ObservableArrayList<Image>?, positionStart: Int, itemCount: Int) {
            this@ImageAdapter.notifyItemInserted(positionStart)
        }

        override fun onItemRangeChanged(sender: ObservableArrayList<Image>?, positionStart: Int, itemCount: Int) {
            this@ImageAdapter.notifyItemRangeChanged(positionStart, itemCount)
        }

    }

/*    interface OnDeleteBtnClickListener {
        fun delete(deviceId: String)
    }*/

    class ImageViewHolder : RecyclerView.ViewHolder {
        private var mBinding: ItemImageBinding? = null

        constructor(itemView: View) : super(itemView) {
            mBinding = DataBindingUtil.bind(itemView)
        }

      /*  fun setOnDeleteBtnClickListener(listener : OnDeleteBtnClickListener){
            itemView.findViewById<LinearLayout>(R.id.ll_delete_container).setOnClickListener { v ->
                var deviceId = itemView.findViewById<PropertyView>(R.id.pv_device_id).getPropertyValue()
                listener.delete(deviceId)
            }
        }*/

        fun bind(@NonNull image: Image) {
            mBinding!!.image = image
            Glide.with(itemView.context.applicationContext).load(image.path).into(itemView.findViewById(R.id.iv_thumbnail))
        }
    }

}