package com.example.news

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var headlineListFragment: HeadlineListFragment
    private lateinit var newsContentFragment: NewsContentFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        headlineListFragment = HeadlineListFragment()
        newsContentFragment = NewsContentFragment()

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, headlineListFragment)
                .commit()
        } else { // LANDSCAPE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_headlines, headlineListFragment)
                .replace(R.id.fragment_container_content, newsContentFragment)
                .commit()
        }
    }

    fun showNewsContentFragment(content: String) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            newsContentFragment.updateContent(content)
        } else {
            val newsContentFragment = NewsContentFragment.newInstance(content)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newsContentFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    // Headline List Fragment
    class HeadlineListFragment : Fragment() {

        private lateinit var listView: ListView

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_headline_list, container, false)
            listView = view.findViewById(R.id.headline_list)

            val headlines = arrayOf("Headline 1", "Headline 2", "Headline 3")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, headlines)
            listView.adapter = adapter

            listView.setOnItemClickListener { _, _, position, _ ->
                val content = "Content for ${headlines[position]}"
                (activity as MainActivity).showNewsContentFragment(content)
            }

            return view
        }
    }

    // News Content Fragment
    class NewsContentFragment : Fragment() {

        private var content: String? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
                content = it.getString(ARG_CONTENT)
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_news_content, container, false)
            val textView: TextView = view.findViewById(R.id.news_content)
            textView.text = content
            return view
        }

        fun updateContent(newContent: String) {
            content = newContent
            view?.findViewById<TextView>(R.id.news_content)?.text = content
        }

        companion object {
            private const val ARG_CONTENT = "content"

            fun newInstance(content: String) =
                NewsContentFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_CONTENT, content)
                    }
                }
        }
    }
}


