package com.example.kienvu.ringtones;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kienvu.ringtones.adapter.RingtonesAdapter;
import com.example.kienvu.ringtones.helper.RingtoneHelper;
import com.example.kienvu.ringtones.model.Ringtone;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView ringtoneListView;
    private BottomSheetLayout bottomSheetLayout;

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
        ringtoneListView = (ListView) findViewById(R.id.ringtone_list);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottom_sheet);
    }

    private void initRingtoneList() {
        List<Ringtone> ringtones = RingtoneHelper.getAllRingtones(this);
        MediaPlayer mediaPlayer = new MediaPlayer();
        final RingtonesAdapter ringtonesAdapter = new RingtonesAdapter(ringtones, mediaPlayer, this);
        ringtoneListView.setAdapter(ringtonesAdapter);
        ringtoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                MenuSheetView menuSheetView =
                        new MenuSheetView(MainActivity.this, MenuSheetView.MenuType.LIST,
                                R.string.set_as, new MenuSheetView.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.alarm_ringtone:
                                        ((Ringtone) ringtonesAdapter.getItem(position)).setAsAlarmRingtone(MainActivity.this);
                                        break;
                                    case R.id.phone_ringtone:
                                        ((Ringtone) ringtonesAdapter.getItem(position)).setAsPhoneRingtone(MainActivity.this);
                                        break;
                                }
                                if (bottomSheetLayout.isSheetShowing()) {
                                    bottomSheetLayout.dismissSheet();
                                }
                                return true;
                            }
                        });
                menuSheetView.inflateMenu(R.menu.menu_set_as);
                bottomSheetLayout.showWithSheetView(menuSheetView);
            }
        });
    }
}
