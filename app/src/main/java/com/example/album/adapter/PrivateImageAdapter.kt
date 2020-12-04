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
import com.example.album.databinding.ItemPrivateImageBinding
import com.example.model.bean.PrivateImage
import java.io.File
import java.lang.Exception


class PrivateImageAdapter : RecyclerView.Adapter<PrivateImageAdapter.PrivateImageViewHolder> {

    private var imageList: ObservableArrayList<PrivateImage>
    private var listener: OnClickListener
    private var checkListener: OnCheckedChangeListener?

    constructor(imageList: ObservableArrayList<PrivateImage>, listener : OnClickListener, checkListener: OnCheckedChangeListener?) {
        this.imageList = imageList
        this.listener = listener
        this.checkListener = checkListener
        imageList.addOnListChangedCallback(dataChangeListener)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): PrivateImageViewHolder {
        var root = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_private_image, viewGroup, false)
        return PrivateImageViewHolder(root)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(imageViewHolder: PrivateImageViewHolder, position: Int) {
        val image: PrivateImage = imageList[position]
        imageViewHolder.bind(image, listener, checkListener)
    }


    private var dataChangeListener = object : ObservableList.OnListChangedCallback<ObservableArrayList<PrivateImage>>() {
        override fun onChanged(sender: ObservableArrayList<PrivateImage>?) {
        }

        override fun onItemRangeRemoved(sender: ObservableArrayList<PrivateImage>?, positionStart: Int, itemCount: Int) {
            this@PrivateImageAdapter.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableArrayList<PrivateImage>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            this@PrivateImageAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeInserted(sender: ObservableArrayList<PrivateImage>?, positionStart: Int, itemCount: Int) {
            this@PrivateImageAdapter.notifyItemInserted(positionStart)
        }

        override fun onItemRangeChanged(sender: ObservableArrayList<PrivateImage>?, positionStart: Int, itemCount: Int) {
            this@PrivateImageAdapter.notifyItemRangeChanged(positionStart, itemCount)
        }

    }

    interface OnClickListener {
        fun show(image: PrivateImage)
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(image: PrivateImage, isChecked: Boolean)
    }

    class PrivateImageViewHolder : RecyclerView.ViewHolder {
        private var mBinding: ItemPrivateImageBinding? = null

        constructor(itemView: View) : super(itemView) {
            mBinding = DataBindingUtil.bind(itemView)
        }

        fun bind(@NonNull image: PrivateImage, listener: OnClickListener, checkListener: OnCheckedChangeListener?) {
            mBinding!!.image = image
            try {
                var imageView = itemView.findViewById<ImageView>(R.id.iv_private_thumbnail)
                Glide.with(itemView.context.applicationContext).load(image.getFilePath()).into(imageView)
                imageView.setOnClickListener{
                    listener.show(image)
                }
                var checkbox = itemView.findViewById<CheckBox>(R.id.cb_private_image_selector)
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