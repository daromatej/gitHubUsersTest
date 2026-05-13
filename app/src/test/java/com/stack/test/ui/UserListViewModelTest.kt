package com.stack.test.ui

import com.stack.test.domain.User
import com.stack.test.domain.UserRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {
    private lateinit var repository: UserRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    private val testUsers = listOf(
        User(id = USER_ID_1, displayName = "Jon Skeet", reputation = 1454978, profileImageUrl = "https://example.com/jon.png", isFollowed = false),
        User(id = USER_ID_2, displayName = "Gordon Linoff", reputation = 1234567, profileImageUrl = "https://example.com/gordon.png", isFollowed = false)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load emits success with users`() = runTest {
        // Given
        coEvery { repository.getUsers() } returns testUsers

        // When
        val viewModel = UserListViewModel(repository)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(testUsers, (state as UiState.Success).users)
    }

    @Test
    fun `initial load emits error on failure`() = runTest {
        // Given
        coEvery { repository.getUsers() } throws IOException("No internet")

        // When
        val viewModel = UserListViewModel(repository)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
    }

    @Test
    fun `toggleFollow updates user follow state`() = runTest {
        // Given
        coEvery { repository.getUsers() } returns testUsers
        every { repository.isFollowed(USER_ID_1) } returns true
        val viewModel = UserListViewModel(repository)

        // When
        viewModel.toggleFollow(USER_ID_1)

        // Then
        verify { repository.toggleFollow(USER_ID_1) }
        val state = viewModel.uiState.value as UiState.Success
        assertTrue(state.users[0].isFollowed)
        assertTrue(!state.users[1].isFollowed)
    }

    @Test
    fun `retry reloads users after error`() = runTest {
        // Given
        coEvery { repository.getUsers() } throws IOException("No internet")
        val viewModel = UserListViewModel(repository)
        assertTrue(viewModel.uiState.value is UiState.Error)

        // When
        coEvery { repository.getUsers() } returns testUsers
        viewModel.loadUsers()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(testUsers, (state as UiState.Success).users)
    }

    companion object {
        private const val USER_ID_1 = 1
        private const val USER_ID_2 = 2
    }
}
