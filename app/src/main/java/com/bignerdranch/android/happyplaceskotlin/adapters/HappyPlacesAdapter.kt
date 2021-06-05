package com.bignerdranch.android.happyplaceskotlin.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.happyplaceskotlin.R
import com.bignerdranch.android.happyplaceskotlin.databinding.ItemHappyPlaceBinding
import com.bignerdranch.android.happyplaceskotlin.models.HappyPlaceModel
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

open class HappyPlacesAdapter(private val context: Context,
                              private var list: ArrayList<HappyPlaceModel>) :
RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding: ItemHappyPlaceBinding =
          ItemHappyPlaceBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: ItemHappyPlaceBinding) : RecyclerView.ViewHolder(view.root) {
        val placeImage = view.ivPlaceImage
        val tvTitle = view.tvTitle
        val tvDescription = view.tvDescription
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list.get(position)

        if(holder is ViewHolder) {

            Glide.with(context).load(model.image).into(holder.placeImage)

          //  holder.placeImage.setImageURI(Uri.parse(model.image))
            holder.tvTitle.text = model.title
            holder.tvDescription.text = model.description
        }

    }

    interface onClickListener {
        fun onClick(position: Int, model: HappyPlaceModel)
    }
}
