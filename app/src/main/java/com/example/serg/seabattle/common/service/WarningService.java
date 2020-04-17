package com.example.serg.seabattle.common.service;

import android.content.Context;
import android.widget.Toast;

public class WarningService {
    private static WarningService warningService;

    private WarningService() {

    }

    public static WarningService getWarningService() {
        if (warningService == null) {
            warningService = new WarningService();
        }
        return warningService;
    }

    public void showWarning(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
