package com.nfsmw.androidevolved;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class CheatOverlayService extends Service {
    private WindowManager windowManager;
    private LinearLayout overlayView, menuLayout;
    private boolean menuVisible = false;
    public static boolean neverBusted = true;
    public static boolean instantCooldown = true;
    public static boolean noCops = false;

    public static native void updateCheats(boolean nb, boolean ic, boolean nc);
    static { try { System.loadLibrary("nfsmw_android"); } catch (Exception e) {} }

    @Override public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        overlayView = new LinearLayout(this);
        overlayView.setOrientation(LinearLayout.VERTICAL);
        TextView btn = new TextView(this);
        btn.setText("MW"); btn.setTextColor(Color.WHITE);
        btn.setBackgroundColor(Color.argb(180, 0, 150, 255));
        btn.setPadding(50,50,50,50); btn.setTextSize(16);
        btn.setOnClickListener(v -> toggleMenu());
        overlayView.addView(btn);
        WindowManager.LayoutParams p = new WindowManager.LayoutParams(-2,-2, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        p.gravity = Gravity.TOP | Gravity.START; p.x=20; p.y=200;
        try { windowManager.addView(overlayView, p); Toast.makeText(this, "MW Cheats Loaded!", Toast.LENGTH_SHORT).show(); } catch (Exception e) { Toast.makeText(this, "Allow overlay!", Toast.LENGTH_LONG).show(); }
    }
    private void toggleMenu() {
        if (menuVisible) { if (menuLayout!=null) try{windowManager.removeView(menuLayout);}catch(Exception e){} menuVisible=false; }
        else { showMenu(); menuVisible=true; }
    }
    private void showMenu() {
        menuLayout = new LinearLayout(this); menuLayout.setOrientation(LinearLayout.VERTICAL);
        menuLayout.setBackgroundColor(Color.argb(235,25,25,25)); menuLayout.setPadding(50,50,50,50);
        TextView t = new TextView(this); t.setText("MW v0.1.11 Cheats"); t.setTextColor(Color.CYAN); t.setTextSize(18); menuLayout.addView(t);
        Switch sw1 = new Switch(this); sw1.setText("  Never Busted"); sw1.setTextColor(Color.WHITE); sw1.setChecked(neverBusted);
        sw1.setOnCheckedChangeListener((b,c)->{neverBusted=c; updateCheats(neverBusted,instantCooldown,noCops);}); menuLayout.addView(sw1);
        Switch sw2 = new Switch(this); sw2.setText("  Instant Cooldown"); sw2.setTextColor(Color.WHITE); sw2.setChecked(instantCooldown);
        sw2.setOnCheckedChangeListener((b,c)->{instantCooldown=c; updateCheats(neverBusted,instantCooldown,noCops);}); menuLayout.addView(sw2);
        Switch sw3 = new Switch(this); sw3.setText("  No Cops"); sw3.setTextColor(Color.WHITE); sw3.setChecked(noCops);
        sw3.setOnCheckedChangeListener((b,c)->{noCops=c; updateCheats(neverBusted,instantCooldown,noCops); Toast.makeText(this,c?"Cops OFF":"Cops ON",Toast.LENGTH_SHORT).show();}); menuLayout.addView(sw3);
        TextView cl = new TextView(this); cl.setText("\n[ CLOSE ]"); cl.setTextColor(Color.YELLOW); cl.setTextSize(16); cl.setOnClickListener(v->toggleMenu()); menuLayout.addView(cl);
        WindowManager.LayoutParams p2 = new WindowManager.LayoutParams(750,-2, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        p2.gravity=Gravity.CENTER; try{windowManager.addView(menuLayout,p2);}catch(Exception e){}
    }
    @Override public IBinder onBind(Intent i){return null;}
    @Override public void onDestroy(){if(overlayView!=null)try{windowManager.removeView(overlayView);}catch(Exception e){} if(menuLayout!=null)try{windowManager.removeView(menuLayout);}catch(Exception e){} super.onDestroy();}
}
