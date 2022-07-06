package com.example.android.roomwordssample;

import android.view.View;
/**
 *Double click implementation
 *
 *@ author Néstor Leiva Mora
 */
public abstract class DobleClickListener implements View.OnClickListener {

    private static final long DOUBLE_TIME = 500;
    private static long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime < DOUBLE_TIME) {
            onDoubleClick(v);
        }
        lastClickTime = currentTimeMillis;
    }

    /**
     *Double click event
     *
     *@ param V view
     */
    public abstract void onDoubleClick(View v);

}