package com.example.roomdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PersonActivity extends AppCompatActivity {
    private FloatingActionButton fabAdd;
    private RecyclerView mRecyclerView;
    private PersonAdapter mAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        fabAdd = findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonActivity.this,
                        EditPersonActivity.class));
            }
        });

        mRecyclerView = findViewById(R.id.rvPerson);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize the adapter and attach it to the RecyclerView
        mAdapter = new PersonAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mDb = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app_database").build();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            //called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Here is where you'll implement swipe to the delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Person> tasks = mAdapter.getTasks();
                        mDb.personDao().delete(tasks.get(position));
                        retrieveTasks();
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTasks();
    }

    private void retrieveTasks(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Person> persons = mDb.personDao().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setTasks(persons);
                    }
                });
            }
        });
    }
}