package com.cleanup.todoc.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.database.TaskRoomDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class RoomTaskRepository implements TaskRepository {

    private final TaskDao taskDao;

    public RoomTaskRepository(Application application) {
        // Initialise la BDD
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        taskDao = db.taskDao();
    }

    public LiveData<List<Task>> getTasks(Task.SortMethod sortMethod) {
        switch(sortMethod) {
            case ALPHABETICAL:
                return taskDao.getTasksSortedByAZ();
            case ALPHABETICAL_INVERTED:
                return taskDao.getTasksSortedByZA();
            case OLD_FIRST:
                return taskDao.getTasksSortedByOld();
            case RECENT_FIRST:
                return taskDao.getTasksSortedByRecent();
            default:
                return taskDao.getTasksUnsorted();
        }
    }

    public LiveData<List<Project>> getProjects() {
        return taskDao.getProjects();
    }

    public LiveData<Project> getProjectById(long id) {
        return taskDao.getProjectById(id);
    }

    public void add(Task task) {
        // Utilise ExecutorService pour exécuter les requêtes à la BDD de manière asynchrone
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> taskDao.insert(task));
    }

    public void remove(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> taskDao.delete(task));
    }
}
