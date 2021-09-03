package com.cleanup.todoc;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.RoomTaskRepository;
import com.cleanup.todoc.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository repository;
    private final LiveData<List<Task>> tasks;
    private final LiveData<List<Project>> projects;
    private final MutableLiveData<Task.SortMethod> sortMethod = new MutableLiveData<>();

    public TaskViewModel(Application application) {
        super(application);
        // Initialise le repository
        repository = new RoomTaskRepository(application);
        setSort(Task.SortMethod.OLD_FIRST);
        // Permet de mettre à jour notre LiveData quand on modifie l'ordre de tri
        tasks = Transformations.switchMap(sortMethod, repository::getTasks);
        projects = repository.getProjects();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public LiveData<List<Project>> getProjects() {
        return projects;
    }

    public LiveData<Project> getProjectById(long id) {
        return repository.getProjectById(id);
    }

    public void add(Task task) {
        repository.add(task);
    }

    public void remove(Task task) {
        repository.remove(task);
    }

    public void setSort(Task.SortMethod sortMethod) {
        this.sortMethod.setValue(sortMethod);
    }
}
