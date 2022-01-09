package com.softocorp.assignmentapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.softocorp.assignmentapp.databinding.RepositoryRecyclerBinding
import com.softocorp.assignmentapp.model.RepositoryModel

class RepositoryAdapter(var item: List<RepositoryModel>) :
    RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    fun setNewData(d: List<RepositoryModel>) {
        this.item = d
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding =
            RepositoryRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val items = item[position]
        if (items != null) {
            holder.bind(items)
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    class RepositoryViewHolder(private val binding: RepositoryRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(repositoryModel: RepositoryModel) {
            binding.apply {
                txtRepositoryName.text = repositoryModel.name
                txtRepositoryDescription.text = repositoryModel.description
                cardView.setOnClickListener {
                    val intent = Intent(it.context, RepositoryInformationActivity::class.java)
                    intent.putExtra("id", repositoryModel.id)
                    it.context.startActivity(intent)
                }
                buttonSend.setOnClickListener {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "This is the repo url: " + repositoryModel.html_url.toString()
                    )
                    shareIntent.type = "text/*"
                    val sendIntent = Intent.createChooser(shareIntent, null)
                    it.context.startActivity(sendIntent)
                }
            }
        }
    }

}