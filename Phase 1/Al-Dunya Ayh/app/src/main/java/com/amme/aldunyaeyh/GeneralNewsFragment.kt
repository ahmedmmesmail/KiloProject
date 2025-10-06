package com.amme.aldunyaeyh

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.amme.aldunyaeyh.databinding.FragmentGeneralNewsBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeneralNewsFragment : Fragment() {
    private lateinit var binding: FragmentGeneralNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeneralNewsBinding.inflate(inflater, container, false)

        loadNews()
        binding.SR.setOnRefreshListener { loadNews() }

        return (binding.root)

    }

    private fun loadNews() {
        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val c = retrofit.create(NewsCallable::class.java)
        c.getGeneralNews().enqueue(object : Callback<News> {
            override fun onResponse(
                call: Call<News?>,
                response: Response<News?>
            ) {
                binding.SR.isRefreshing = false
                binding.prog.isVisible = false
                val news = response.body()
                val articles = news?.articles!!
                showNews(articles)
            }

            override fun onFailure(
                call: Call<News?>,
                t: Throwable
            ) {
                Snackbar
                    .make(
                        binding.root,
                        "error has occurred... ${t.message}",
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }

        })
    }

    fun showNews(articles: ArrayList<Articles>) {
        val adapter = NewsAdapter(requireContext(), articles)
        binding.list.adapter = adapter

    }
}