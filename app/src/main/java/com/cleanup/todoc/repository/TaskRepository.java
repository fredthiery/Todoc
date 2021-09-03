package com.cleanup.todoc.repository;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public interface TaskRepository {
    /**
     * Returns a list of tasks sorted by sortMethod
     * @param sortMethod sorting order
     * @return a list of tasks
     */
    LiveData<List<Task>> getTasks(Task.SortMethod sortMethod);

    /**
     * Returns a list of projects
     * @return a list of projects
     */
    LiveData<List<Project>> getProjects();

    /**
     * Returns the project associated with the id
     * @param id
     * @return the project
     */
    LiveData<Project> getProjectById(long id);

    /**
     * Adds a task to the list
     * @param task
     */
    void add(Task task);

    /**
     * Removes the task from the list
     * @param task
     */
    void remove(Task task);
}
