package com.undergrads.ryan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

/**
 * Created by sarahmedeiros on 4/27/17.
 */

public class EmergencyText extends BroadcastReceiver{
    int status;
    @Override
    public void onReceive(Context context, Intent intent) {
//        https://developer.android.com/training/monitoring-device-state/battery-monitoring.html#CurrentLevel
        status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

    }
    public int getStatus(){
        return status;
    }
}

