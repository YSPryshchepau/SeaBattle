package com.example.serg.seabattle.common.enums;

import com.example.serg.seabattle.R;

public enum ColorCellType {
    BLUE_CELL(R.drawable.cl_blue),
    GREEN_CELL(R.drawable.cl_green),
    RED_CELL(R.drawable.cl_red),
    CRIMSON_CELL(R.drawable.cl_crimson),
    WHITE_CELL(R.drawable.cl_white);

    public final int colorID;

    ColorCellType(int colorID) {
        this.colorID = colorID;
    }
}

