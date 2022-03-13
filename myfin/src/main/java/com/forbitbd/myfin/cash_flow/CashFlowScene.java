package com.forbitbd.myfin.cash_flow;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.CashFlow;
import com.forbitbd.myfin.utils.MyBarDataSet;
import com.forbitbd.myfin.utils.MyLabelFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;


public class CashFlowScene extends Fragment {

    private Communicator communicator;

    private BarChart barChart;



    public CashFlowScene() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communicator = (Communicator) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cash_flow_scene, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        barChart = view.findViewById(R.id.bar_chart);
    }

    @Override
    public void onResume() {
        super.onResume();
        communicator.setTitle(communicator.getProject().getName().concat(" ").concat(getString(R.string.cash_flow)));
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(true);

        barChart.setDrawGridBackground(false);
        barChart.getLegend().setEnabled(false);

        List<CashFlow> cashFlowList = communicator.getDashboard().getCashFlow();

        Log.d("HHHHH",cashFlowList.size()+" Cash Flow Size");


        List<String> xAxisLabels = new ArrayList<>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (CashFlow x: cashFlowList) {
            yVals1.add(new BarEntry(cashFlowList.indexOf(x), (float) x.getAmount()));
            xAxisLabels.add(MyUtil.getStringDate(x.getDate()));
        }

        IAxisValueFormatter xAxisFormatter = new MyLabelFormatter(xAxisLabels);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelRotationAngle(90f);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);

        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawLabels(false); // no axis labels
        leftAxis.setDrawAxisLine(false); // no axis line
        leftAxis.setDrawGridLines(false); // no grid lines
        leftAxis.setDrawZeroLine(true); // draw a zero line
        leftAxis.setEnabled(false);
        barChart.getAxisRight().setEnabled(false); // no right axis

        BarDataSet dataSet = new MyBarDataSet(yVals1,getString(R.string.cash_flow));
        dataSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
                ContextCompat.getColor(getContext(), R.color.red)});


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(dataSet);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(12f);
        data.setValueTypeface(ResourcesCompat.getFont(getContext(), R.font.arima_madurai_medium));
        data.setBarWidth(0.9f);
        //data.setValueFormatter(new MyCurrencyFormatter(userLocalStore.getCurrency()));
        barChart.setData(data);
        barChart.animateXY(1000,1000);
        barChart.setVisibleXRangeMaximum(10f);
        barChart.invalidate();
    }
}