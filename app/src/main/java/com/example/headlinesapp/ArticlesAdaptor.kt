package com.example.headlinesapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.example.headlinesapp.models.Article

/**
 * Adaptor for a RecyclerView and a list of Articles
 * @param mArticles list of Articles to display in RecyclerView
 */
class ArticlesAdaptor(private val mArticles: List<Article>) : RecyclerView.Adapter<ArticlesAdaptor.ViewHolder>() {

    inner class ViewHolder(listItemView: View, val context: Context) : RecyclerView.ViewHolder(listItemView) {
        val articleTitle = itemView.findViewById<TextView>(R.id.article_title)
        val articleImage = itemView.findViewById<ImageView>(R.id.article_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val articleView = inflater.inflate(R.layout.thin_card_article, parent, false)
        return ViewHolder(articleView, context)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article: Article = mArticles.get(position)

        //Set the title
        holder.articleTitle.text = article.title

        //Spinner to indicate image is loading
        val loadingSpinner = CircularProgressDrawable(holder.context)
        loadingSpinner.strokeWidth = 3F
        loadingSpinner.centerRadius = 50F
        loadingSpinner.start()

        //Set the image with Glide if the image url is present
        if(article.urlToImage != null) {
            holder.articleImage.visibility = ImageView.VISIBLE
            Glide.with(holder.itemView)
                    .load(article.urlToImage)
                    .placeholder(loadingSpinner) //loading spinner
                    //Crop image to fit and center, and round edges to match border
                    .transform(MultiTransformation(CenterCrop(), GranularRoundedCorners(0.0F, 12F, 12F, 0F)))
                    .into(holder.articleImage)
        } else {  //Otherwise hide the image view and the title will take up it's space.
            holder.articleImage.visibility = ImageView.GONE
        }

        //Set on click action
        if(article.url != null){
            holder.itemView.setOnClickListener {
                //Open url in browser
                holder.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article.url)))
            }
        } else {
            holder.itemView.setOnClickListener {
                //Sorry toast if no url
                Toast.makeText(holder.context, holder.context.getString(R.string.no_article_toast), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return mArticles.size
    }
}