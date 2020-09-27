package com.example.album.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.album.R
import com.example.album.databinding.ItemAlbumBinding
import com.example.album.databinding.ItemImageBinding
import com.example.model.bean.Image
import com.example.model.bean.ImageFolder

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private var folderList: ObservableArrayList<ImageFolder>
    //var listener: OnDeleteBtnClickListener

    //constructor(imageList: ObservableArrayList<Image>, listener : OnDeleteBtnClickListener) {
    constructor(folderList: ObservableArrayList<ImageFolder>) {
        this.folderList = folderList
        //this.listener = listener
        folderList.addOnListChangedCallback(dataChangeListener)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): AlbumViewHolder {
        var root = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_album, viewGroup, false)
        return AlbumViewHolder(root)
    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    override fun onBindViewHolder(imageViewHolder: AlbumViewHolder, position: Int) {
        val folder: ImageFolder = folderList[position]
        imageViewHolder.bind(folder)
    }


    private var dataChangeListener = object : ObservableList.OnListChangedCallback<ObservableArrayList<ImageFolder>>() {
        override fun onChanged(sender: ObservableArrayList<ImageFolder>?) {
        }

        override fun onItemRangeRemoved(sender: ObservableArrayList<ImageFolder>?, positionStart: Int, itemCount: Int) {
            this@AlbumAdapter.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableArrayList<ImageFolder>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            this@AlbumAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeInserted(sender: ObservableArrayList<ImageFolder>?, positionStart: Int, itemCount: Int) {
            this@AlbumAdapter.notifyItemInserted(positionStart)
        }

        override fun onItemRangeChanged(sender: ObservableArrayList<ImageFolder>?, positionStart: Int, itemCount: Int) {
            this@AlbumAdapter.notifyItemRangeChanged(positionStart, itemCount)
        }

    }

/*    interface OnDeleteBtnClickListener {
        fun delete(deviceId: String)
    }*/

    class AlbumViewHolder : RecyclerView.ViewHolder {
        private var mBinding: ItemAlbumBinding? = null

        constructor(itemView: View) : super(itemView) {
            mBinding = DataBindingUtil.bind(itemView)
        }

        /*  fun setOnDeleteBtnClickListener(listener : OnDeleteBtnClickListener){
              itemView.findViewById<LinearLayout>(R.id.ll_delete_container).setOnClickListener { v ->
                  var deviceId = itemView.findViewById<PropertyView>(R.id.pv_device_id).getPropertyValue()
                  listener.delete(deviceId)
              }
          }*/

        fun bind(@NonNull folder: ImageFolder) {
            mBinding!!.folder = folder
            Glide.with(itemView.context.applicationContext).load(folder.firstImagePath).into(itemView.findViewById(
                R.id.iv_cover))
            itemView.findViewById<AppCompatTextView>(R.id.tv_gallery_name).text = folder.dirName
            itemView.findViewById<AppCompatTextView>(R.id.tv_image_size).text = folder.size.toString()
        }
    }
}