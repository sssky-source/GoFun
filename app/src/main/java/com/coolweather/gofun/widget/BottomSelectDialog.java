package com.coolweather.gofun.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;

import com.coolweather.gofun.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomSelectDialog extends BottomSheetDialog {

    private BottomSelectDialog mBottomSheetDialog;
    public BottomSelectDialog(@NonNull Context context) {
        super(context);
        initViews();
    }

    private void initViews() {
        View view = getLayoutInflater().inflate(R.layout.dialog_bottom_select, null);
        setContentView(view);
    }
}
