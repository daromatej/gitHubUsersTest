package com.stack.test.data.api

import com.google.gson.annotations.SerializedName

data class UsersResponse(
  @SerializedName("items") val items: List<UserDto>
)

data class UserDto(
  @SerializedName("user_id") val userId: Int,
  @SerializedName("display_name") val displayName: String,
  @SerializedName("reputation") val reputation: Int,
  @SerializedName("profile_image") val profileImage: String?,
  @SerializedName("badge_counts") val badgeCounts: BadgeCountsDto?
)

data class BadgeCountsDto(
  @SerializedName("gold") val gold: Int,
  @SerializedName("silver") val silver: Int,
  @SerializedName("bronze") val bronze: Int
)
