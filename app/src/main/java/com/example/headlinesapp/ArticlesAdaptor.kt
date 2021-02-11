package com.example.headlinesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.headlinesapp.models.Article

/**
 * Adaptor for a RecyclerView and a list of Articles
 * @param mArticles list of Articles to display in RecyclerView
 */
class ArticlesAdaptor(private val mArticles: List<Article>) : RecyclerView.Adapter<ArticlesAdaptor.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val articleTitle = itemView.findViewById<TextView>(R.id.article_title)
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

        //Set the title
        holder.articleTitle.text = article.title

        //Set the image with Glide if the image url is present
        if(article.urlToImage != null) {
            holder.articleImage.visibility = ImageView.VISIBLE
            Glide.with(holder.itemView)
                    .load(article.urlToImage)
                    .fitCenter()
                    .into(holder.articleImage)
        } else {  //Otherwise hide the image view and the title will take up it's space.
            holder.articleImage.visibility = ImageView.GONE
        }
    }

    override fun getItemCount(): Int {
        return mArticles.size
    }
}