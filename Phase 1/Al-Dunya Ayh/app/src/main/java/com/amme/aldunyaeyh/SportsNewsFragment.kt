package com.amme.aldunyaeyh

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.amme.aldunyaeyh.databinding.FragmentSportsNewsBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SportsNewsFragment : Fragment() {

    private lateinit var binding: FragmentSportsNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSportsNewsBinding.inflate(inflater, container, false)

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
        c.getSportsNews().enqueue(object : Callback<News> {
            override fun onResponse(
                call: Call<News?>,
                response: Response<News?>
            ) {
                val news = response.body()
                val articles = news?.articles!!
                showNews(articles)
                binding.prog.isVisible = false
                binding.SR.isRefreshing = false
            }

            override fun onFailure(
                call: Call<News?>,
                t: Throwable
            ) {Snackbar
                .make(
                    binding.root,
                    "error has occurred... ${t.message}",
                    Snackbar.LENGTH_LONG
                )
                .show()
            }
        })
    }

    private fun showNews(articles: ArrayList<Articles>) {
        val adapter = NewsAdapter(requireContext(), articles)
        binding.list.adapter = adapter
    }
}