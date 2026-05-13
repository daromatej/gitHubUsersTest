package com.stack.test.data

import com.stack.test.data.api.StackOverflowApi
import com.stack.test.data.api.UserDto
import com.stack.test.data.local.FollowStorage
import com.stack.test.domain.User
import com.stack.test.domain.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
  private val api: StackOverflowApi,
  private val followStorage: FollowStorage
) : UserRepository {

  override suspend fun getUsers(): List<User> {
    val response = api.getUsers()
    return response.items.map { it.toDomain() }
  }

  override fun toggleFollow(userId: Int) {
    followStorage.toggleFollow(userId)
  }

  override fun isFollowed(userId: Int): Boolean {
    return followStorage.isFollowed(userId)
  }

  private fun UserDto.toDomain(): User {
    return User(
      id = userId,
      displayName = displayName,
      reputation = reputation,
      profileImageUrl = profileImage,
      isFollowed = followStorage.isFollowed(userId)
    )
  }
}
