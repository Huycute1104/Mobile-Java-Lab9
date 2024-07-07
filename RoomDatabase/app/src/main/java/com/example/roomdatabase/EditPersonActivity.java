package com.example.roomdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

public class EditPersonActivity extends AppCompatActivity {
    private EditText editFirstName;
    private EditText editLastName;
    private Button btnSave;

    private int mPersonId;

    private Intent intent;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        mDb = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app_database").build();

        intent = getIntent();
        if(intent != null && intent.hasExtra(Constants.UPDATE_Person_Id)){
            btnSave.setText("Update");
            mPersonId = intent.getIntExtra(Constants.UPDATE_Person_Id, 1);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Person person = mDb.personDao().loadPersonById(mPersonId);
                    populateUI(person);
                }
            });
        }
    }

    private void populateUI(Person person){
        if(person == null){
            return;
        }

        editFirstName.setText(person.getFirstName());
        editLastName.setText(person.getLastName());
    }

    private void initViews(){
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    public void onSaveButtonClicked(){
        final Person person = new Person(
                editFirstName.getText().toString(),
                editLastName.getText().toString()
        );

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(!intent.hasExtra(Constants.UPDATE_Person_Id)){
                    mDb.personDao().insert(person);
                }else{
                    person.setUid(mPersonId);
                    mDb.personDao().update(person);
                }
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //Respond to the action bar's home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}