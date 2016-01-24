package com.example.kienvu.ringtones;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.ViewConfiguration;
import android.widget.ListView;

import com.example.kienvu.ringtones.adapter.RingtonesAdapter;
import com.example.kienvu.ringtones.helper.RingtoneHelper;
import com.example.kienvu.ringtones.model.Ringtone;

import java.lang.reflect.Field;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView ringtoneListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRingtoneList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        getOverflowMenu();
        ringtoneListView = (ListView) findViewById(R.id.ringtone_list);
    }

    private void initRingtoneList() {
        List<Ringtone> ringtones = RingtoneHelper.getAllRingtones(this);
        RingtonesAdapter ringtonesAdapter = new RingtonesAdapter(ringtones, this);
        ringtoneListView.setAdapter(ringtonesAdapter);
    }

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
