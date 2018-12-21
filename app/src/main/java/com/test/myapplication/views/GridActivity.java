package com.test.myapplication.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.test.myapplication.R;
import com.test.myapplication.adadpter.ImageAdapter;
import com.test.myapplication.listeners.SelectedCallback;
import com.test.myapplication.model.ImageStatus;

import java.util.ArrayList;

public class GridActivity extends AppCompatActivity implements SelectedCallback {

    private RecyclerView rvImageAdapter;
    private ArrayList<ImageStatus> imageStatuses;
    private TextView tvSelectedStatus;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        setTitle("Post Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        imageStatuses = new ArrayList<>();
        rvImageAdapter = findViewById(R.id.rvImageAdapter);
        tvSelectedStatus = findViewById(R.id.tvSelectedStatus);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        rvImageAdapter.setLayoutManager(layoutManager);
        rvImageAdapter.setAdapter(new ImageAdapter(getImages(), imageStatuses, this, this));
    }

    private ArrayList<Integer> getImages() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i <= 50; i++) {
            arrayList.add(R.drawable.ic_launcher_background);
            imageStatuses.add(new ImageStatus(false));
        }
        return arrayList;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select: {
                Intent intent = new Intent();
                intent.putExtra("count", count);
                setResult(RESULT_OK, intent);
                finish();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSelection(int count) {
        this.count = count;
        tvSelectedStatus.setText(count + " " + "Selected\n Maximum 3 can be selected");
        if (count == 3) {
            Intent intent = new Intent();
            intent.putExtra("count", count);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
