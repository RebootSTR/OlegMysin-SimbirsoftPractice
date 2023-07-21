package o.mysin.simbirsoftappjava.ui.news.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import o.mysin.simbirsoftappjava.domain.repository.HelpCategoryRepository
import o.mysin.simbirsoftappjava.domain.repository.NewsRepository
import o.mysin.simbirsoftappjava.domain.model.News

class NewsViewModel(
    private val newsRepository: NewsRepository,
    private val helpCategoryRepository: HelpCategoryRepository,
) : ViewModel() {

    private val _newsList: MutableLiveData<List<News>> = MutableLiveData()
    val newsList: LiveData<List<News>>
        get() = _newsList

    init {
        newsRepository.getObservableNews()
            .subscribe { newsList ->
                _newsList.value = newsList
            }
            .dispose()
    }

    fun loadNews() {
        val filterIdList = helpCategoryRepository.getHelpCategories()
            .filter { it.isEnabled }
            .map { it.id }
        viewModelScope.launch {
            _newsList.value = getNewsByFilter(filterIdList)
        }
    }

    fun setIsViewedNews(idNews: Int) {
        _newsList.value?.find { it.id == idNews }?.isViewed = true
        newsRepository.setIsViewedNews(idNews)
    }

    private fun getNewsByFilter(filter: List<Int>): List<News> {
        return newsRepository.getAllNews().filter { news ->
            news.listHelpCategoryId.any() { category ->
                category in filter
            }
        }
    }
}
