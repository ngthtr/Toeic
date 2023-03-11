package com.english.toeic.repository;

import static com.english.toeic.view.ChartActivity.COLOR_FALSE;
import static com.english.toeic.view.ChartActivity.COLOR_NON;
import static com.english.toeic.view.ChartActivity.COLOR_TRUE;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.english.toeic.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class PartAdapter extends BaseAdapter {
    private static final String TAG = "PartAdapter";
    private ArrayList<Part> mArrPart;

    public PartAdapter(ArrayList<Part> arrPart) {
        mArrPart = arrPart;
    }

    @Override
    public int getCount() {
        return mArrPart.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrPart.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewPart;
        if (view == null) {
            viewPart = View.inflate(viewGroup.getContext(), R.layout.item_chart_part, null);
        } else {
            viewPart = view;
        }

        Part part = (Part) getItem(i);
        Log.i(TAG, "getView(): " + part);
        HorizontalBarChart barChart = (HorizontalBarChart) viewPart.findViewById(R.id.bar_chart_part);
        int percentTrue = (int) Math.floor(part.numberTrue * 100.0 / part.getNumberAnswer());
        int percentFalse = (int) Math.floor(part.numberFalse * 100.0 / part.getNumberAnswer());
        int percentNon = 100 - percentTrue - percentFalse;

        Log.i(TAG, "getView(): percent_true = [" + percentTrue + "], " +
                "percent_false = [" + percentFalse + "], percent_non = [" + percentNon + "]");
        ((TextView) viewPart.findViewById(R.id.tv_name_part)).setText(part.getName());
        ((TextView) viewPart.findViewById(R.id.tv_info_part)).setText("Correct " + part.numberTrue
                + " - Incorrect " + part.numberFalse + " - No Answer " + part.numberNon);

        List<BarEntry> listBarEntry = new ArrayList<>(Arrays.asList(
                new BarEntry(0, new float[]{percentTrue, percentFalse, percentNon}, null))
        );
        BarDataSet barDataSet = new BarDataSet(listBarEntry, null);
        barDataSet.setColors(COLOR_TRUE, COLOR_FALSE, COLOR_NON);
        barDataSet.setDrawValues(false);

        BarData barData = new BarData(barDataSet);
        barData.setDrawValues(false);
        barData.setHighlightEnabled(false);


        barChart.setData(barData);
        barChart.setDescription(null);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setAxisLineColor(Color.parseColor("#00000000"));
        barChart.getAxisLeft().setAxisMaximum(100);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setDrawLabels(false);
        barChart.getLegend().setEnabled(false);

        barChart.invalidate();
        return viewPart;
    }

}
