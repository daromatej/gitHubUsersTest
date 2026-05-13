package com.stack.test.ui

import androidx.annotation.StringRes
import com.stack.test.domain.User

sealed interface UiState {
  data object Loading : UiState
  data class Success(val users: List<User>) : UiState
  data class Error(@StringRes val messageRes: Int) : UiState
}
