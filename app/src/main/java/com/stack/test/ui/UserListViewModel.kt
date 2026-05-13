package com.stack.test.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stack.test.R
import com.stack.test.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
  private val repository: UserRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
  val uiState: StateFlow<UiState> = _uiState

  init {
    loadUsers()
  }

  fun loadUsers() {
    viewModelScope.launch {
      _uiState.value = UiState.Loading
      try {
        val users = repository.getUsers()
        _uiState.value = UiState.Success(users)
      } catch (e: Exception) {
        _uiState.value = UiState.Error(R.string.error_loading_users)
      }
    }
  }

  fun toggleFollow(userId: Int) {
    repository.toggleFollow(userId)
    updateCurrentState(userId)
  }

  private fun updateCurrentState(userId: Int) {
    val currentState = _uiState.value
    if (currentState is UiState.Success) {
      val updatedUsers = currentState.users.map { user ->
        if (user.id == userId) {
          user.copy(isFollowed = repository.isFollowed(userId))
        } else {
          user
        }
      }
      _uiState.value = UiState.Success(updatedUsers)
    }
  }
}
