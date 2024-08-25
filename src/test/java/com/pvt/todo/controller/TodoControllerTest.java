package com.pvt.todo.controller;

import com.pvt.todo.model.TaskStatus;
import com.pvt.todo.model.ToDoItem;
import com.pvt.todo.service.ToDoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    @Mock
    private ToDoService toDoService;

    @InjectMocks
    private TodoController todoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllItems() {
        List<ToDoItem> mockItems = Arrays.asList(new ToDoItem(), new ToDoItem());
        when(toDoService.getAllItems()).thenReturn(mockItems);

        ResponseEntity<List<ToDoItem>> response = todoController.getAllItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockItems, response.getBody());
        verify(toDoService, times(1)).getAllItems();
    }

    @Test
    void getItemById() {
        Long itemId = 1L;
        ToDoItem mockItem = new ToDoItem();
        when(toDoService.getItemById(itemId)).thenReturn(mockItem);

        ResponseEntity<ToDoItem> response = todoController.getItemById(itemId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockItem, response.getBody());
        verify(toDoService, times(1)).getItemById(itemId);
    }

    @Test
    void getItemById_notFound() {
        Long itemId = 1L;
        when(toDoService.getItemById(itemId)).thenThrow(new RuntimeException("Item not found"));

        ResponseEntity<ToDoItem> response = todoController.getItemById(itemId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(toDoService, times(1)).getItemById(itemId);
    }

    @Test
    void createItem() {
        ToDoItem newItem = new ToDoItem();
        ToDoItem savedItem = new ToDoItem();
        savedItem.setId(1L);

        when(toDoService.createItem(newItem)).thenReturn(savedItem);

        ResponseEntity<ToDoItem> response = todoController.createItem(newItem);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedItem, response.getBody());
        verify(toDoService, times(1)).createItem(newItem);
    }

    @Test
    void updateItem() {
        Long itemId = 1L;
        ToDoItem updatedDetails = new ToDoItem();
        ToDoItem updatedItem = new ToDoItem();
        updatedItem.setId(itemId);

        when(toDoService.updateItem(itemId, updatedDetails)).thenReturn(updatedItem);

        ResponseEntity<ToDoItem> response = todoController.updateItem(itemId, updatedDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedItem, response.getBody());
        verify(toDoService, times(1)).updateItem(eq(itemId), any(ToDoItem.class));
    }

    @Test
    void deleteItem() {
        Long itemId = 1L;

        doNothing().when(toDoService).deleteItem(itemId);

        ResponseEntity<Void> response = todoController.deleteItem(itemId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(toDoService, times(1)).deleteItem(itemId);
    }

    @Test
    void getItemsByStatus() {
        TaskStatus status = TaskStatus.NEW;
        List<ToDoItem> mockItems = Arrays.asList(new ToDoItem(), new ToDoItem());

        when(toDoService.getItemsByStatus(status)).thenReturn(mockItems);

        ResponseEntity<List<ToDoItem>> response = todoController.getItemsByStatus(status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockItems, response.getBody());
        verify(toDoService, times(1)).getItemsByStatus(status);
    }
}
