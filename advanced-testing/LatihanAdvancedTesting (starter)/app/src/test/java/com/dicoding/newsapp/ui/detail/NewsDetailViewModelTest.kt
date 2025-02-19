package com.dicoding.newsapp.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.newsapp.data.NewsRepository
import com.dicoding.newsapp.utils.DataDummy
import com.dicoding.newsapp.utils.MainDispatcherRule
import com.dicoding.newsapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NewsDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var newsRepository: NewsRepository
    private lateinit var newsDetailViewModel: NewsDetailViewModel
    private val dummyDetailNews = DataDummy.generateDummyNewsEntity()[0]

    @Before
    fun setUp() {
        newsDetailViewModel = NewsDetailViewModel(newsRepository)
        newsDetailViewModel.setNewsData(dummyDetailNews)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when bookmarkStatus false Should Call SaveNews`() = runTest {
        val expectedBoolean = MutableLiveData<Boolean>()
        expectedBoolean.value = false
        `when`(newsRepository.isNewsBookmarked(dummyDetailNews.title)).thenReturn(expectedBoolean)
        newsDetailViewModel.bookmarkStatus.getOrAwaitValue()
        newsDetailViewModel.changeBookmark(dummyDetailNews)
        verify(newsRepository).saveNews(dummyDetailNews)
    }

    @Test
    fun `when bookmarkStatus true Should Call RemoveNews`() = runTest {
        val expectedBoolead = MutableLiveData<Boolean>()
        expectedBoolead.value = true
        `when`(newsRepository.isNewsBookmarked(dummyDetailNews.title)).thenReturn(expectedBoolead)
        newsDetailViewModel.bookmarkStatus.getOrAwaitValue()
        newsDetailViewModel.changeBookmark(dummyDetailNews)
        verify(newsRepository).deleteNews(dummyDetailNews.title)
    }
}