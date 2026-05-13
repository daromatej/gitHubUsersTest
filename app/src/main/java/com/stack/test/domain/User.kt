package com.stack.test.domain

data class User(
  val id: Int,
  val displayName: String,
  val reputation: Int,
  val profileImageUrl: String?,
  val isFollowed: Boolean
)
