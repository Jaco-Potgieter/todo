package com.pvt.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pvt.todo.model.TaskStatus;
import com.pvt.todo.model.ToDoItem;

@Repository
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {
    List<ToDoItem> findByStatus(TaskStatus status);
}
