package com.cleanup.todoc;

import static com.cleanup.todoc.TestUtils.getOrAwaitValue;
import static com.google.common.truth.Truth.assertThat;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.database.TaskRoomDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseUnitTest {
    private TaskDao dao;
    private TaskRoomDatabase db;
    final Task task1 = new Task(1, 1, "aaa", 123);
    final Task task2 = new Task(2, 2, "zzz", 124);
    final Task task3 = new Task(3, 3, "hhh", 125);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TaskRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.taskDao();
        dao.insert(new Project(1L, "Projet Tartampion", 0xFFEADAD1));
        dao.insert(new Project(2L, "Projet Lucidia", 0xFFB4CDBA));
        dao.insert(new Project(3L, "Projet Circus", 0xFFA3CED2));
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeTaskAndReadInList() throws InterruptedException {
        // Given
        List<Task> expectedTasks = Arrays.asList(task1, task2, task3);
        // When
        dao.insert(Arrays.asList(task1, task2, task3));
        List<Task> result = getOrAwaitValue(dao.getTasksUnsorted());
        // Then
        assertThat(result).isEqualTo(expectedTasks);
    }

    @Test
    public void testSortAZ() throws InterruptedException {
        // Given
        List<Task> expectedTasks = Arrays.asList(task1, task3, task2);
        // When
        dao.insert(Arrays.asList(task1, task2, task3));
        List<Task> result = getOrAwaitValue(dao.getTasksSortedByAZ());
        // Then
        assertThat(result).isEqualTo(expectedTasks);
    }

    @Test
    public void testSortZA() throws InterruptedException {
        // Given
        List<Task> expectedTasks = Arrays.asList(task2, task3, task1);
        // When
        dao.insert(Arrays.asList(task1, task2, task3));
        List<Task> result = getOrAwaitValue(dao.getTasksSortedByZA());
        // Then
        assertThat(result).isEqualTo(expectedTasks);
    }

    @Test
    public void testSortRecent() throws InterruptedException {
        // Given
        List<Task> expectedTasks = Arrays.asList(task3, task2, task1);
        // When
        dao.insert(Arrays.asList(task1, task2, task3));
        List<Task> result = getOrAwaitValue(dao.getTasksSortedByRecent());
        // Then
        assertThat(result).isEqualTo(expectedTasks);
    }

    @Test
    public void testSortOld() throws InterruptedException {
        // Given
        List<Task> expectedTasks = Arrays.asList(task1, task2, task3);
        // When
        dao.insert(Arrays.asList(task1, task2, task3));
        List<Task> result = getOrAwaitValue(dao.getTasksSortedByOld());
        // Then
        assertThat(result).isEqualTo(expectedTasks);
    }
}
