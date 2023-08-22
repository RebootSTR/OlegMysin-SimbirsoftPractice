package o.mysin.simbirsoftappjava.ui.news.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import o.mysin.simbirsoftappjava.R
import o.mysin.simbirsoftappjava.appComponent
import o.mysin.simbirsoftappjava.domain.model.News
import o.mysin.simbirsoftappjava.databinding.FragmentNewsBinding
import o.mysin.simbirsoftappjava.ui.MainActivityViewModel
import o.mysin.simbirsoftappjava.utils.NewsItemDecoration
import javax.inject.Inject

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding: FragmentNewsBinding by viewBinding()

    @Inject
    lateinit var newsViewModel: NewsViewModel
    private val mainViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var adapter: NewsAdapter

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderView()
        initAdapter()
        initRecycler()
        initActionButtons()
        newsViewModel.newsList.observe(viewLifecycleOwner) { renderData(it) }
        newsViewModel.errorMessage.observe(viewLifecycleOwner) { renderMessage(it.message) }
        updateData()
    }

    private fun renderView() {
        mainViewModel.setHideBottomNavigation(false)
    }

    private fun initAdapter() {
        adapter = NewsAdapter { idItem ->
            newsViewModel.setIsViewedNews(idItem)
            mainViewModel.setHideBottomNavigation(true)
            val action = NewsFragmentDirections.actionFragmentNewsToNewsDetailFragment(idItem)
            findNavController().navigate(action)
        }
    }

    private fun initRecycler() {
        binding.newsItemsRecyclerView.adapter = adapter
        binding.newsItemsRecyclerView.apply {
            addItemDecoration(NewsItemDecoration(binding.root.context))
        }
    }

    private fun initActionButtons() {
        binding.buttonFilters.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_news_to_filtersFragment)
        }
    }

    private fun renderData(newsList: List<News>) {
        showList(newsList.isEmpty())
        if (newsList.isNotEmpty()) {
            adapter.updateNewsList(newsList)
        }
        setBadgeCount(newsList)
    }

    private fun setBadgeCount(newsList: List<News>) {
        val currentCountNewsNotViewed = newsViewModel.getCountNewsNotViewed(newsList)
        mainViewModel.setBadgeCount(currentCountNewsNotViewed)
    }

    private fun renderMessage(message: String) {
        if (message.isNotBlank()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateData() {
        showLoadingData()
        newsViewModel.loadNews()
    }

    private fun showList(listIsEmpty: Boolean) {
        hideLoadingData()
        if (listIsEmpty) {
            binding.newsItemsRecyclerView.visibility = View.GONE
            binding.emptyListText.visibility = View.VISIBLE
        } else {
            binding.newsItemsRecyclerView.visibility = View.VISIBLE
            binding.emptyListText.visibility = View.GONE
        }
    }

    private fun showLoadingData() {
        binding.loadingProgressBar.visibility = View.VISIBLE
        binding.newsItemsRecyclerView.visibility = View.GONE
        binding.emptyListText.visibility = View.GONE
    }

    private fun hideLoadingData() {
        binding.loadingProgressBar.visibility = View.GONE
    }

}
