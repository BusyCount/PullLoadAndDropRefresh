package com.busycount.loadrefresh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.busycount.pullloaddroprefresh.LRListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LRListView mListView;
    private List<String> data = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (LRListView) findViewById(R.id.mListView);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (data.isEmpty()) {
            for (int i = 0; i <= 30; i++) {
                data.add("Test data " + i);
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
