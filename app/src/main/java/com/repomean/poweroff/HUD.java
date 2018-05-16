package com.repomean.poweroff;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HUD extends Service {
    LinearLayout oView;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;
    @Override
    public void onCreate() {
        super.onCreate();
        mDevicePolicyManager = (DevicePolicyManager)getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, MyAdminReceiver.class);
        oView = new LinearLayout(this);
        int col =  Color.parseColor("#4286f4");
        oView.setBackgroundColor(col);
        int width = 50;
        int height = 50;
        int x = Resources.getSystem().getDisplayMetrics().widthPixels;
        int y = 0;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(width, height, x, y,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.overlay, null);
        layout.findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean isAdminImgView = mDevicePolicyManager.isAdminActive(mComponentName);
                if (isAdminImgView) {
                    mDevicePolicyManager.lockNow();
                }else{
                    Toast.makeText(getApplicationContext(), "Not Registered as admin", Toast.LENGTH_SHORT).show();
                }
            }
        });
        oView.addView(layout);
        wm.addView(oView, params);
    }
    public void onDestroy() {
        super.onDestroy();
        if(oView!=null){
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.removeView(oView);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

