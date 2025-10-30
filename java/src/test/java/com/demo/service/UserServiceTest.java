package com.demo.service;

import com.demo.model.User;
import com.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService
 * Tests all CRUD operations and business logic methods
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser1;
    private User testUser2;
    private User testUser3;

    @BeforeEach
    void setUp() {
        testUser1 = new User(1L, "John Doe", "john@example.com", "Engineering", true);
        testUser2 = new User(2L, "Jane Smith", "jane@example.com", "Marketing", true);
        testUser3 = new User(3L, "Bob Johnson", "bob@example.com", "Engineering", false);
    }

    @Test
    void testGetAllUsers() {
        // Given
        List<User> users = Arrays.asList(testUser1, testUser2, testUser3);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Found() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser1));

        // When
        Optional<User> result = userService.getUserById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        assertEquals("john@example.com", result.get().getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.getUserById(99L);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void testCreateUser() {
        // Given
        User newUser = new User(null, "New User", "new@example.com", "Sales", null);
        User savedUser = new User(4L, "New User", "new@example.com", "Sales", true);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.createUser(newUser);

        // Then
        assertNotNull(result);
        assertEquals(4L, result.getId());
        assertTrue(result.getIsActive());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        // Given
        User updatedData = new User(null, "Updated Name", "updated@example.com", "HR", false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser1));
        when(userRepository.save(any(User.class))).thenReturn(testUser1);

        // When
        Optional<User> result = userService.updateUser(1L, updatedData);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Updated Name", testUser1.getName());
        assertEquals("updated@example.com", testUser1.getEmail());
        assertEquals("HR", testUser1.getDepartment());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(testUser1);
    }

    @Test
    void testUpdateUser_NotFound() {
        // Given
        User updatedData = new User(null, "Updated Name", "updated@example.com", "HR", false);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.updateUser(99L, updatedData);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(99L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // When
        boolean result = userService.deleteUser(1L);

        // Then
        assertTrue(result);
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        // Given
        when(userRepository.existsById(99L)).thenReturn(false);

        // When
        boolean result = userService.deleteUser(99L);

        // Then
        assertFalse(result);
        verify(userRepository, times(1)).existsById(99L);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetUsersByDepartment() {
        // Given
        List<User> engineeringUsers = Arrays.asList(testUser1, testUser3);
        when(userRepository.findByDepartment("Engineering")).thenReturn(engineeringUsers);

        // When
        List<User> result = userService.getUsersByDepartment("Engineering");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(u -> "Engineering".equals(u.getDepartment())));
        verify(userRepository, times(1)).findByDepartment("Engineering");
    }

    @Test
    void testGetActiveUsers() {
        // Given
        List<User> activeUsers = Arrays.asList(testUser1, testUser2);
        when(userRepository.findByIsActiveTrue()).thenReturn(activeUsers);

        // When
        List<User> result = userService.getActiveUsers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(User::getIsActive));
        verify(userRepository, times(1)).findByIsActiveTrue();
    }
}
