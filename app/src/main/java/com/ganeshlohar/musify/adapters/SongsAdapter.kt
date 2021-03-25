package com.androiddevs.mvvmnewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ganeshlohar.musify.R
import com.ganeshlohar.musify.model.Song
import kotlinx.android.synthetic.main.layout_song_card.view.*

class SongsAdapter : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {
    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.trackId == newItem.trackId
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Song, newItem: Song): Any? {
            return super.getChangePayload(oldItem, newItem)
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_song_card, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = differ.currentList[position]

        holder.itemView.apply {
            tvSongName.text = song.trackName
            tvArtistName.text = song.artistName
            val hdArtworkUrl = song.artworkUrl100.replace("100x100bb.jpg","700x700bb.jpg")
            Glide.with(this).load(hdArtworkUrl).placeholder(R.drawable.cover_placeholder).into(imgArtwork)
            setOnItemClickListener {
                onItemClickListener?.let { it(song) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener : ((Song) -> Unit)? = null

    fun setOnItemClickListener(listener:(Song) -> Unit){
        onItemClickListener = listener
    }

}