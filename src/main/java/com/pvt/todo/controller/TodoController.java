package com.pvt.todo.controller;

import com.pvt.todo.model.TaskStatus;
import com.pvt.todo.model.ToDoItem;
import com.pvt.todo.service.ToDoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@Slf4j
@RequiredArgsConstructor
public class TodoController {

    private final ToDoService toDoService;

    @GetMapping
    public ResponseEntity<List<ToDoItem>> getAllItems() {
        try {
            List<ToDoItem> items = toDoService.getAllItems();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Error fetching all ToDo items", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoItem> getItemById(@PathVariable Long id) {
        try {
            ToDoItem item = toDoService.getItemById(id);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            log.error("Error fetching ToDo item with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching ToDo item with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/createItem")
    public ResponseEntity<ToDoItem> createItem(@RequestBody ToDoItem item) {
        try {
            ToDoItem createdItem = toDoService.createItem(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (Exception e) {
            log.error("Error creating ToDo item: {}", item.getTitle(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToDoItem> updateItem(@PathVariable Long id, @RequestBody ToDoItem itemDetails) {
        try {
            ToDoItem updatedItem = toDoService.updateItem(id, itemDetails);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            log.error("Error updating ToDo item with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating ToDo item with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        try {
            toDoService.deleteItem(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting ToDo item with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error occurred while deleting ToDo item with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ToDoItem>> getItemsByStatus(@PathVariable TaskStatus status) {
        try {
            List<ToDoItem> items = toDoService.getItemsByStatus(status);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Error fetching ToDo items with status: {}", status, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
