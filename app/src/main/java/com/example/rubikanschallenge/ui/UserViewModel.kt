package com.example.rubikanschallenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.rubikanschallenge.utils.Resource
import com.example.rubikanschallenge.utils.UsersStats
import com.example.rubikanschallenge.data.repository.UserRepository
import com.example.rubikanschallenge.utils.UserStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _usersState = MutableStateFlow<UsersStats>(UsersStats())
    private val _userState = MutableStateFlow<UserStats>(UserStats())

    val users: LiveData<UsersStats>
        get() = _usersState.asLiveData()

    val user: LiveData<UserStats>
        get() = _userState.asLiveData()


    fun getUsers() {
        userRepository.getUsers().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _usersState.value = UsersStats(
                        users = result.data!! ?: emptyList()
                    )
                }
                is Resource.Error -> {
                    _usersState.value = UsersStats(
                        error = result.message ?: "An unexpected error happened"
                    )
                }
                is Resource.Loading -> {
                    _usersState.value = UsersStats(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUser(id: Long) {
        userRepository.getUser(id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _userState.value = UserStats(
                        user = result.data!!.user
                    )
                }
                is Resource.Error -> {
                    _userState.value = UserStats(
                        error = result.message ?: "An unexpected error happened"
                    )
                }
                is Resource.Loading -> {
                    _userState.value = UserStats(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }


}