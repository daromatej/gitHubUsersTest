package com.stack.test.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stack.test.R
import coil.compose.AsyncImage
import com.stack.test.domain.User
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
  viewModel: UserListViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(stringResource(R.string.screen_title)) }
      )
    }
  ) { padding ->
    when (val state = uiState) {
      is UiState.Loading -> LoadingContent(Modifier.padding(padding))
      is UiState.Error -> ErrorContent(
        message = stringResource(state.messageRes),
        onRetry = { viewModel.loadUsers() },
        modifier = Modifier.padding(padding)
      )
      is UiState.Success -> UserList(
        users = state.users,
        onFollowClick = { userId -> viewModel.toggleFollow(userId) },
        modifier = Modifier.padding(padding)
      )
    }
  }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    CircularProgressIndicator()
  }
}

@Composable
private fun ErrorContent(
  message: String,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = message)
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = onRetry) {
      Text(stringResource(R.string.button_retry))
    }
  }
}

@Composable
private fun UserList(
  users: List<User>,
  onFollowClick: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(modifier = modifier.fillMaxSize()) {
    items(users, key = { it.id }) { user ->
      UserListItem(
        user = user,
        onFollowClick = { onFollowClick(user.id) }
      )
    }
  }
}

@Composable
private fun UserListItem(
  user: User,
  onFollowClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    AsyncImage(
      model = user.profileImageUrl,
      contentDescription = stringResource(R.string.content_description_profile_image, user.displayName),
      modifier = Modifier
        .size(48.dp)
        .clip(CircleShape),
      contentScale = ContentScale.Crop
    )

    Spacer(modifier = Modifier.width(12.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = user.displayName,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = formatReputation(user.reputation),
        style = MaterialTheme.typography.bodyMedium,
      )
    }

    if (user.isFollowed) {
      OutlinedButton(onClick = onFollowClick) {
        Text(stringResource(R.string.button_following))
      }
    } else {
      Button(onClick = onFollowClick) {
        Text(stringResource(R.string.button_follow))
      }
    }
  }
}

private fun formatReputation(reputation: Int): String {
  return NumberFormat.getIntegerInstance().format(reputation)
}
