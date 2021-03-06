package com.example.afs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.*;

public class ExerciseList extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;
    private ListView exerList;
    private List<Exercise> exercisesDB;
    private BodyPart bodyPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_list);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.ExerciseListToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });


        bodyPart = new BodyPart(this);
        String body = getIntent().getStringExtra("body");
        getSupportActionBar().setTitle(body);
        exercisesDB = bodyPart.bodyToExercise.get(body);

        //store the list into array
        updateExerciseAdapter(exercisesDB);

        exerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise clickedObj =  (Exercise)parent.getItemAtPosition(position);
                exerDetail(clickedObj);

            }
        });

    }

    private void back() {
        Intent intent = new Intent(this, WorkOut.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void exerDetail(Exercise e) {
        Intent intent = new Intent(this, EquipmentInfo.class);
        intent.putExtra("name", e.getName());
        intent.putExtra("eqpt", e.getEquipment());
        intent.putExtra("instruction", e.getInstructions());
        intent.putExtra("image", e.getImagePath());
        intent.putExtra("met", e.getMet());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void updateExerciseAdapter(List<Exercise> exerciseList)
    {
        //store the list into array

        //get the food list gadget
        exerList = (ListView)findViewById(R.id.exercise_list);
        //set up the adapter
        ExerciseAdapter exerciseAdapter= new ExerciseAdapter(getApplicationContext(), exerciseList);
        exerList.setAdapter(exerciseAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
