package com.cleanup.todoc;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository repository;
    private final LiveData<List<Task>> tasks;
    private final LiveData<List<Project>> projects;
    private final MutableLiveData<SortOrder> sortOrder = new MutableLiveData<>();

    public TaskViewModel(Application application) {
        super(application);
        // Initialise le repository
        repository = new TaskRepository(application);
        setSort(SortOrder.OLD);
        // Permet de mettre à jour notre LiveData quand on modifie l'ordre de tri
        tasks = Transformations.switchMap(sortOrder, repository::getTasks);
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

    public void setSort(SortOrder order) {
        sortOrder.setValue(order);
    }
}
