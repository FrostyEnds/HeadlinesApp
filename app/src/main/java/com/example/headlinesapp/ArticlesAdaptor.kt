package com.example.headlinesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ArticlesAdaptor(private val mArticles: List<Article>) : RecyclerView.Adapter<ArticlesAdaptor.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val articleTitle = itemView.findViewById<TextView>(R.id.article_title)
        //val articleSourceName = itemView.findViewById<TextView>(R.id.article_source_name)
        val articleImage = itemView.findViewById<ImageView>(R.id.article_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val articleView = inflater.inflate(R.layout.thin_card_article, parent, false)
        return ViewHolder(articleView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article: Article = mArticles.get(position)
        holder.articleTitle.text = article.title
        //holder.articleSourceName.text = article.source.name
        Glide.with(holder.itemView)
            .load(article.urlToImage)
            .fitCenter()
            .into(holder.articleImage)
    }

    override fun getItemCount(): Int {
        return mArticles.size
    }
}