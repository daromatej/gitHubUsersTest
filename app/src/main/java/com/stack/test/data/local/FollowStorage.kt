package com.stack.test.data.local

import android.content.SharedPreferences
import javax.inject.Inject

class FollowStorage @Inject constructor(
    private val prefs: SharedPreferences
) {
    companion object {
        private const val KEY_FOLLOWED_IDS = "followed_user_ids"
    }

    fun isFollowed(userId: Int): Boolean {
        return getFollowedIds().contains(userId.toString())
    }

    fun toggleFollow(userId: Int) {
        val ids = getFollowedIds().toMutableSet()
        val idStr = userId.toString()
        if (ids.contains(idStr)) {
            ids.remove(idStr)
        } else {
            ids.add(idStr)
        }
        prefs.edit().putStringSet(KEY_FOLLOWED_IDS, ids).apply()
    }

    private fun getFollowedIds(): Set<String> {
        return prefs.getStringSet(KEY_FOLLOWED_IDS, emptySet()) ?: emptySet()
    }
}
