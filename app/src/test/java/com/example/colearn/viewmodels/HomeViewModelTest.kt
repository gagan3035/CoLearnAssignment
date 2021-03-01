package com.example.colearn.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.colearn.Utils.Constants
import com.example.colearn.getOrAwaitValue
import com.example.colearn.network.ImageService
import com.example.colearn.remoteSource.PhotosRemoteSoruce
import com.example.colearn.repository.PhotosRepository
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Gagan on 01/03/21.
 */
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @Mock
    lateinit var photosRepository: PhotosRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var viewModel:HomeViewModel

    @Before
    fun setUp() {
    viewModel = HomeViewModel(photosRepository)
    }



    @Test
    fun setSearchQuery_starWarsEntered_StarWarsReturned(){

        viewModel.setSearchData("starwars")
        val result = viewModel._searchQueryLiveData.getOrAwaitValue()
        assertThat(result,Matchers.`is`("starwars"))
    }

    @Test
    fun setSearchQuery_emptyString_emptyStringReturned(){

        viewModel.setSearchData("")
        val result = viewModel._searchQueryLiveData.getOrAwaitValue()
        assertThat(result,Matchers.`is`(""))
    }




    @Test
    fun applyFilterClicked_OrderByRelevance_RelevanceReturned() {
        // Given / Arrange 

        // When / Act
        viewModel.setFilterData(Constants.SORT_BY_FILTER,Constants.OrderByFilter.ORDER_BY_RELEVANT)
        //Then/ Assert
        assertThat(viewModel.sortBy,Matchers.`is`(Constants.OrderByFilter.ORDER_BY_RELEVANT))

    }

    @Test
    fun applyFilterClicked_OrderByLatest_LatestReturned() {
        // Given / Arrange

        // When / Act
        viewModel.setFilterData(Constants.SORT_BY_FILTER,Constants.OrderByFilter.ORDER_BY_LATEST)
        //Then/ Assert
        assertThat(viewModel.sortBy,Matchers.`is`(Constants.OrderByFilter.ORDER_BY_LATEST))

    }
    @Test
    fun applyFilterClicked_FilterByBlackAndWhite_BlackAndWhiteReturned() {
        // Given / Arrange

        // When / Act
        viewModel.setFilterData(Constants.COLOR_FILTER,Constants.OrderByColor.ORDER_BY_BNW)
        //Then/ Assert
        assertThat(viewModel.sortBy,Matchers.`is`(Constants.OrderByColor.ORDER_BY_BNW))

    }

    @Test
    fun applyFilterClicked_FilterByLandscapeOrientation_landscapeReturned() {
        // Given / Arrange

        // When / Act
        viewModel.setFilterData(Constants.ORIENTATION_FILTER,Constants.OrderByOrientation.ORDER_BY_LANDSCAPE)
        //Then/ Assert
        assertThat(viewModel.sortBy,Matchers.`is`(Constants.OrderByOrientation.ORDER_BY_LANDSCAPE))

    }

    @Test
    fun applyFilterClicked_FilterByPortraitOrientation_PortraitReturned() {
        // Given / Arrange

        // When / Act
        viewModel.setFilterData(Constants.SORT_BY_FILTER,Constants.OrderByOrientation.ORDER_BY_PORTRAIT)
        //Then/ Assert
        assertThat(viewModel.sortBy,Matchers.`is`(Constants.OrderByOrientation.ORDER_BY_PORTRAIT))

    }

    @Test
    fun applyFilterClicked_FilterBySquareOrientation_SquareReturned() {
        // Given / Arrange

        // When / Act
        viewModel.setFilterData(Constants.SORT_BY_FILTER,Constants.OrderByOrientation.ORDER_BY_SQUARISH)
        //Then/ Assert
        assertThat(viewModel.sortBy,Matchers.`is`(Constants.OrderByOrientation.ORDER_BY_SQUARISH))

    }

    @Test
    fun clearData() {
        // Given / Arrange 
        // When / Act
        viewModel.clearData()
        //Then/ Assert

        assertThat(viewModel.fullViewList.size,Matchers.`is`(0))
        assertThat(viewModel.thumbnailList.size,Matchers.`is`(0))



    }
}