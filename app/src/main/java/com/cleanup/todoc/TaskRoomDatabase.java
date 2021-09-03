package com.cleanup.todoc;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class, Project.class}, version = 1)
public abstract class TaskRoomDatabase extends RoomDatabase {

    // On utilise un Singleton comme instance de la base de données
    private static volatile TaskRoomDatabase INSTANCE;

    // L'ExecutorService sert pour les appels asymchrones à la base de données
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Le Data Access Object qui sert à faire les requêtes vers la base de données
    public abstract TaskDao taskDao();

    // Le Callback sert à pré-remplir la liste des projets quand la base de données est créée
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                TaskDao dao = INSTANCE.taskDao();
                dao.insert(new Project(1L, "Projet Tartampion", 0xFFEADAD1));
                dao.insert(new Project(2L, "Projet Lucidia", 0xFFB4CDBA));
                dao.insert(new Project(3L, "Projet Circus", 0xFFA3CED2));
            });
        }
    };

    // Renvoie l'instance de la base de données ou la crée si elle n'existe pas encore
    static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TaskRoomDatabase.class,
                            "task_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
