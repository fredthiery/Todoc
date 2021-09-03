package com.cleanup.todoc;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskRepository {

    // TODO: Créer une interface repository

    private final TaskDao taskDao;

    TaskRepository(Application application) {
        // Initialise la BDD
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        taskDao = db.taskDao();
    }

    public LiveData<List<Task>> getTasks(SortOrder order) {
        switch(order) {
            case AZ:
                return taskDao.getTasksSortedByAZ();
            case ZA:
                return taskDao.getTasksSortedByZA();
            case OLD:
                return taskDao.getTasksSortedByOld();
            case RECENT:
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
