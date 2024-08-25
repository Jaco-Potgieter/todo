package com.pvt.todo.service;

import com.pvt.todo.exception.ItemNotFoundException;
import com.pvt.todo.exception.StatusReversionException;
import com.pvt.todo.model.TaskStatus;
import com.pvt.todo.model.ToDoItem;
import com.pvt.todo.repository.ToDoItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoItemRepository toDoItemRepository;

    @Cacheable("todoItems")
    public List<ToDoItem> getAllItems() {
        log.info("Fetching all ToDo items");
        return toDoItemRepository.findAll();
    }

    public ToDoItem getItemById(Long id) {
        log.info("Fetching ToDo item with ID: {}", id);
        return toDoItemRepository.findById(id)
            .orElseThrow(() -> {
                log.error("ToDo item with ID {} not found", id);
                return new ItemNotFoundException("Item not found with ID: " + id);
            });
    }

    @Transactional
    public ToDoItem createItem(ToDoItem item) {
        log.info("Creating a new ToDo item: {}", item.getTitle());
        validateItem(item, null);
        item = item.toBuilder()
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        ToDoItem savedItem = toDoItemRepository.save(item);
        log.info("ToDo item created with ID: {}", savedItem.getId());
        return savedItem;
    }

    @Transactional
    public ToDoItem updateItem(Long id, ToDoItem updatedItem) {
        ToDoItem existingItem = getItemById(id);

        existingItem = existingItem.toBuilder()
            .title(Optional.ofNullable(updatedItem.getTitle()).orElse(existingItem.getTitle()))
            .description(Optional.ofNullable(updatedItem.getDescription()).orElse(existingItem.getDescription()))
            .completed(Optional.ofNullable(updatedItem.getCompleted()).orElse(existingItem.getCompleted()))
            .status(Optional.ofNullable(updatedItem.getStatus()).orElse(existingItem.getStatus()))
            .updatedAt(LocalDateTime.now())
            .build();

        // Validate status transitions if status was changed
        if (updatedItem.getStatus() != null) {
            validateItem(existingItem, existingItem.getStatus());
        }

        if (Boolean.TRUE.equals(existingItem.getCompleted())) {
            existingItem.setStatus(TaskStatus.COMPLETED);
            log.info("Item marked as completed, status updated to COMPLETED");
        }

        return toDoItemRepository.save(existingItem);
    }

    @Transactional
    public void deleteItem(Long id) {
        log.info("Deleting ToDo item with ID: {}", id);
        ToDoItem item = getItemById(id);
        toDoItemRepository.delete(item);
        log.info("ToDo item with ID {} deleted successfully", id);
    }

    @Cacheable("todoItemsByStatus")
    public List<ToDoItem> getItemsByStatus(TaskStatus status) {
        log.info("Fetching ToDo items with status: {}", status);
        return toDoItemRepository.findByStatus(status);
    }

    private void validateItem(ToDoItem item, TaskStatus previousStatus) {
        if (previousStatus != null && item.getStatus() == TaskStatus.NEW && previousStatus != TaskStatus.NEW) {
            log.error("Status cannot be reverted to NEW for item with previous status: {}", previousStatus);
            throw new StatusReversionException("Cannot revert status to NEW");
        }

        if (Boolean.TRUE.equals(item.getCompleted()) && item.getStatus() != TaskStatus.COMPLETED) {
            item.setStatus(TaskStatus.COMPLETED);
            log.info("Item marked as completed, status updated to COMPLETED");
        }
    }
}
