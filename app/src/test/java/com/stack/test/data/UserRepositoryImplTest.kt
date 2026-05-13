package com.stack.test.data

import com.stack.test.data.api.BadgeCountsDto
import com.stack.test.data.api.StackOverflowApi
import com.stack.test.data.api.UserDto
import com.stack.test.data.api.UsersResponse
import com.stack.test.data.local.FollowStorage
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {
    private lateinit var api: StackOverflowApi
    private lateinit var followStorage: FollowStorage
    private lateinit var repository: UserRepositoryImpl

    private val testDto = UserDto(
        userId = USER_ID,
        displayName = DISPLAY_NAME,
        reputation = REPUTATION,
        profileImage = PROFILE_IMAGE,
        badgeCounts = BadgeCountsDto(gold = 877, silver = 9202, bronze = 9255)
    )

    @Before
    fun setup() {
        api = mockk()
        followStorage = mockk(relaxed = true)
        repository = UserRepositoryImpl(api, followStorage)
    }

    @Test
    fun `getUsers maps dto to domain model`() = runTest {
        // Given
        coEvery { api.getUsers() } returns UsersResponse(items = listOf(testDto))
        every { followStorage.isFollowed(USER_ID) } returns false

        // When
        val users = repository.getUsers()

        // Then
        assertEquals(1, users.size)
        val user = users[0]
        assertEquals(USER_ID, user.id)
        assertEquals(DISPLAY_NAME, user.displayName)
        assertEquals(REPUTATION, user.reputation)
        assertEquals(PROFILE_IMAGE, user.profileImageUrl)
        assertFalse(user.isFollowed)
    }

    @Test
    fun `getUsers merges follow state from storage`() = runTest {
        // Given
        coEvery { api.getUsers() } returns UsersResponse(items = listOf(testDto))
        every { followStorage.isFollowed(USER_ID) } returns true

        // When
        val users = repository.getUsers()

        // Then
        assertTrue(users[0].isFollowed)
    }

    @Test
    fun `toggleFollow delegates to follow storage`() {
        // When
        repository.toggleFollow(USER_ID)

        // Then
        verify { followStorage.toggleFollow(USER_ID) }
    }

    @Test
    fun `isFollowed delegates to follow storage`() {
        // Given
        every { followStorage.isFollowed(USER_ID) } returns true

        // When / Then
        assertTrue(repository.isFollowed(USER_ID))
    }

    companion object {
        private const val USER_ID = 22656
        private const val DISPLAY_NAME = "Jon Skeet"
        private const val REPUTATION = 1454978
        private const val PROFILE_IMAGE = "https://example.com/jon.png"
    }
}
