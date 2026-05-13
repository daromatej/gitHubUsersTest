package com.stack.test.domain

interface UserRepository {
  suspend fun getUsers(): List<User>
  fun toggleFollow(userId: Int)
  fun isFollowed(userId: Int): Boolean
}
