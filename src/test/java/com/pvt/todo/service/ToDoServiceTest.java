package com.pvt.todo.service;

import com.pvt.todo.exception.ItemNotFoundException;
import com.pvt.todo.model.TaskStatus;
import com.pvt.todo.model.ToDoItem;
import com.pvt.todo.repository.ToDoItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToDoServiceTest {

    private ToDoItemRepository toDoItemRepository;
    private ToDoService toDoService;

    @BeforeEach
    void setUp() {
        toDoItemRepository = Mockito.mock(ToDoItemRepository.class);
        toDoService = new ToDoService(toDoItemRepository);
    }

    @Test
    void getAllItems() {
        List<ToDoItem> items = Arrays.asList(
            ToDoItem.builder()
                .id(1L)
                .title("Item 1")
                .description("Description 1")
                .status(TaskStatus.NEW)
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        );

        when(toDoItemRepository.findAll()).thenReturn(items);

        List<ToDoItem> result = toDoService.getAllItems();

        assertEquals(1, result.size());
        assertEquals("Item 1", result.get(0).getTitle());
        verify(toDoItemRepository, times(1)).findAll();
    }

    @Test
    void getItemById() {
        ToDoItem item = ToDoItem.builder()
            .id(1L)
            .title("Item 1")
            .description("Description 1")
            .status(TaskStatus.NEW)
            .completed(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(toDoItemRepository.findById(1L)).thenReturn(Optional.of(item));

        ToDoItem result = toDoService.getItemById(1L);

        assertNotNull(result);
        assertEquals("Item 1", result.getTitle());
        verify(toDoItemRepository, times(1)).findById(1L);
    }

    @Test
    void getItemById_notFound() {
        when(toDoItemRepository.findById(1L)).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            toDoService.getItemById(1L);
        });

        assertEquals("Item not found with ID: 1", exception.getMessage());
        verify(toDoItemRepository, times(1)).findById(1L);
    }

    @Test
    void createItem() {
        ToDoItem item = ToDoItem.builder()
            .id(1L)
            .title("New Item")
            .description("New Description")
            .status(TaskStatus.NEW)
            .completed(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(toDoItemRepository.save(any(ToDoItem.class))).thenReturn(item);

        ToDoItem result = toDoService.createItem(item);

        assertNotNull(result);
        assertEquals("New Item", result.getTitle());
        verify(toDoItemRepository, times(1)).save(any(ToDoItem.class));
    }

    @Test
    void updateItem() {
        ToDoItem existingItem = ToDoItem.builder()
            .id(1L)
            .title("Existing Item")
            .description("Existing Description")
            .status(TaskStatus.NEW)
            .completed(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        ToDoItem updatedItem = ToDoItem.builder()
            .title("Updated Item")
            .description(null)
            .status(TaskStatus.IN_PROGRESS)
            .completed(true)
            .build();

        ToDoItem updatedExistingItem = existingItem.toBuilder()
            .title("Updated Item")
            .completed(true)
            .status(TaskStatus.COMPLETED)
            .updatedAt(LocalDateTime.now())
            .build();

        when(toDoItemRepository.findById(1L)).thenReturn(Optional.of(existingItem));
        when(toDoItemRepository.save(any(ToDoItem.class))).thenReturn(updatedExistingItem);

        ToDoItem result = toDoService.updateItem(1L, updatedItem);

        assertEquals("Updated Item", result.getTitle());
        assertTrue(result.getCompleted());
        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        verify(toDoItemRepository, times(1)).findById(1L);
        verify(toDoItemRepository, times(1)).save(any(ToDoItem.class));
    }

    @Test
    void deleteItem() {
        ToDoItem item = ToDoItem.builder()
            .id(1L)
            .title("Item to delete")
            .description("Description")
            .status(TaskStatus.NEW)
            .completed(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(toDoItemRepository.findById(1L)).thenReturn(Optional.of(item));
        doNothing().when(toDoItemRepository).delete(item);

        toDoService.deleteItem(1L);

        verify(toDoItemRepository, times(1)).findById(1L);
        verify(toDoItemRepository, times(1)).delete(item);
    }

    @Test
    void getItemsByStatus() {
        List<ToDoItem> items = Arrays.asList(
            ToDoItem.builder()
                .id(1L)
                .title("Item 1")
                .description("Description 1")
                .status(TaskStatus.NEW)
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        );

        when(toDoItemRepository.findByStatus(TaskStatus.NEW)).thenReturn(items);

        List<ToDoItem> result = toDoService.getItemsByStatus(TaskStatus.NEW);

        assertEquals(1, result.size());
        assertEquals("Item 1", result.get(0).getTitle());
        verify(toDoItemRepository, times(1)).findByStatus(TaskStatus.NEW);
    }
}
