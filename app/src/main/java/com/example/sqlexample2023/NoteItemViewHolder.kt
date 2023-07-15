package com.example.sqlexample2023

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlexample2023.databinding.NoteItemLayoutBinding
import java.util.Random

class NoteItemViewHolder(val binding:NoteItemLayoutBinding):RecyclerView.ViewHolder(binding.root) {
    //random color
    private val random = Random()
    private val color = Color.argb(160,random.nextInt(255),random.nextInt(178),random.nextInt(178))
    fun bind(note: Note){
        binding.tvTitleRecycler.text=note.title
        binding.tvContentRecycler.text=note.content
        binding.cardView.setCardBackgroundColor(color)
        binding.btnImgDelete.setBackgroundColor(color)
        binding.btnImgUpdate.setBackgroundColor(color)
    }
}