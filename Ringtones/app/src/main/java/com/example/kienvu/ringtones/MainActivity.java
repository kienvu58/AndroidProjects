package com.example.kienvu.ringtones;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
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
    private MediaPlayer mediaPlayer;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: When UI has updated, uncomment the line below
//        AppRater.appLaunched(this);
        initView();
        initRingtoneList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rate_app:
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        ringtoneListView = (ListView) findViewById(R.id.ringtone_list);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottom_sheet);
    }

    private void initRingtoneList() {
        List<Ringtone> ringtones = RingtoneHelper.getAllRingtones(this);
        mediaPlayer = new MediaPlayer();
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

        // Handle when phone has incoming call
        IntentFilter filter = new IntentFilter("android.intent.action.PHONE_STATE");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);

                switch (telephonyManager.getCallState()) {
                    case TelephonyManager.CALL_STATE_RINGING :
                        ringtonesAdapter.stopAllPlaying();
                        break;
                    default:
                        ringtonesAdapter.stopAllPlaying();
                }
            }
        };
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        mediaPlayer.reset();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
