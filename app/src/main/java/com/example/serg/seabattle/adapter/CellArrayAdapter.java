package com.example.serg.seabattle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.serg.seabattle.R;
import com.example.serg.seabattle.gameplay.entity.Cell;

public class CellArrayAdapter extends BaseAdapter {
    private LayoutInflater lInflater;

    private Cell[] cells;

    public CellArrayAdapter(Context context, Cell[] cells) {
        setCells(cells);
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cells.length;
    }
    @Override
    public Object getItem(int position) {
        return cells[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.adapter_layout, parent, false);
        }
        ((ImageView) view.findViewById(R.id.image)).setImageResource(getCell(position).getPictureAddress());

        return view;
    }
    public Cell getCell(int position) {
        return cells[position];
    }

    public Cell[] getCells() {
        return cells;
    }

    public void setCells(Cell[] cells) {
        this.cells = cells;
    }
}
