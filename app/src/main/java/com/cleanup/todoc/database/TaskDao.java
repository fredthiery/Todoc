package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Task> tasks);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Project project);

    @Delete
    void delete(Task task);

    @Delete
    void delete(Project project);

    @Query("SELECT * FROM task")
    LiveData<List<Task>> getTasksUnsorted();

    @Query("SELECT * FROM task ORDER BY name COLLATE NOCASE")
    LiveData<List<Task>> getTasksSortedByAZ();

    @Query("SELECT * FROM task ORDER BY name COLLATE NOCASE DESC")
    LiveData<List<Task>> getTasksSortedByZA();

    @Query("SELECT * FROM task ORDER BY creation_timestamp DESC")
    LiveData<List<Task>> getTasksSortedByRecent();

    @Query("SELECT * FROM task ORDER BY creation_timestamp ASC")
    LiveData<List<Task>> getTasksSortedByOld();

    @Query("SELECT * FROM project")
    LiveData<List<Project>> getProjects();

    @Query("SELECT * FROM project WHERE id = :project_id")
    LiveData<Project> getProjectById(long project_id);
}
