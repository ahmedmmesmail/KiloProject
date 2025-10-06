package com.amme.aldunyaeyh

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.amme.aldunyaeyh.databinding.ArticleItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class NewsAdapter(val a : Context, val articles: ArrayList<Articles>)
    : RecyclerView.Adapter<NewsAdapter.NVH>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NVH {
        val b = ArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NVH(b)
    }

    override fun onBindViewHolder(holder: NVH, position: Int) {
        holder.binding.articleText.text = articles[position].title
        val url = articles[position].url
        Glide
            .with(holder.binding.articleImg.context)
            .load(articles[position].urlToImage)
            .error(R.drawable.broken_image)
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            .into(holder.binding.articleImg)

        holder.binding.articleContainer.setOnClickListener {
            a.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
        }
        holder.binding.shareFap.setOnClickListener {
            ShareCompat
                .IntentBuilder(a)
                .setType("text/plain")
                .setText(url)
                .startChooser()
        }

    }

    override fun getItemCount() = articles.size
    class NVH(val binding: ArticleItemBinding): RecyclerView.ViewHolder(binding.root) {}
}
