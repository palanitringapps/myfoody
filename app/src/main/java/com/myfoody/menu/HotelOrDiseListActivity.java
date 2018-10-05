package com.myfoody.menu;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.myfoody.R;
import com.myfoody.base.BaseActivity;
import com.myfoody.model.HotelDetails;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HotelOrDiseListActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        RecyclerView list = findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        list.setLayoutManager(layoutManager);
        List<HotelDetails> details = new ArrayList<>();
        list.setAdapter(new HotelListAdapter(this, details));
    }

}
