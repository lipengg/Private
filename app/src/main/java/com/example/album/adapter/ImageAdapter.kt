package com.example.album.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.album.R
import com.example.album.databinding.ItemImageBinding
import com.example.model.bean.Image
import java.lang.Exception


class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private var imageList: ObservableArrayList<Image>
    private var listener: OnClickListener
    private var checkListener: OnCheckedChangeListener?

    constructor(imageList: ObservableArrayList<Image>, listener : OnClickListener, checkListener: OnCheckedChangeListener?) {
        this.imageList = imageList
        this.listener = listener
        this.checkListener = checkListener
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
        imageViewHolder.bind(image, listener, checkListener)
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

    interface OnClickListener {
        fun show(image: Image)
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(image: Image, isChecked: Boolean)
    }

    class ImageViewHolder : RecyclerView.ViewHolder {
        private var mBinding: ItemImageBinding? = null

        constructor(itemView: View) : super(itemView) {
            mBinding = DataBindingUtil.bind(itemView)
        }

        fun bind(@NonNull image: Image, listener: OnClickListener, checkListener: OnCheckedChangeListener?) {
            mBinding!!.image = image
            try {
                var imageView = itemView.findViewById<ImageView>(R.id.iv_thumbnail)
                Glide.with(itemView.context.applicationContext).load(image.path).into(imageView)
                imageView.setOnClickListener{
                    listener.show(image)
                }
                var checkbox = itemView.findViewById<CheckBox>(R.id.cb_image_selector)
                checkbox.isChecked = image.selected
                checkListener?.let{
                    checkbox.setOnCheckedChangeListener { _, isChecked ->
                        it.onCheckedChanged(image, isChecked)
                        image.selected = isChecked
                    }
                }
                if (checkListener == null) {
                    checkbox.visibility = View.GONE
                }
            } catch (e:Exception) {
                Log.e("ImageAdapter","ImageViewHolder bind failed! Error: " + e.message)
            }

        }
    }

}