package com.forbitbd.myfin.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

public class MyLabelFormatter implements IAxisValueFormatter {

    private String[] values;

    public MyLabelFormatter(List<String> values) {

        this.values = new String[values.size()];

        for(int i=0;i<values.size();i++){
            this.values[i]=values.get(i);
        }
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return values[(int) value];
    }
}
