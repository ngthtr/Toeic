package com.english.toeic.view;

import static com.english.toeic.constant.TestConstant.TYPE_ANSWER;
import static com.english.toeic.view.TestActivity.KEY_INFORMATION;
import static com.english.toeic.view.TestActivity.mAnswers;
import static com.english.toeic.view.TestActivity.mInformation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.english.toeic.R;
import com.english.toeic.repository.Answer;
import com.english.toeic.repository.Part;
import com.english.toeic.repository.PartAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ChartActivity extends AppCompatActivity {

    private static final String TAG = "ChartAnswer";
    public static final int COLOR_TRUE = Color.GREEN;
    public static final int COLOR_FALSE = Color.RED;
    public static final int COLOR_NON = Color.GRAY;
    private final String NUMBER_TRUE = "number_true";
    private final String NUMBER_FALSE = "number_false";
    private final String NUMBER_NON = "number_non";
    private final float SLICE_SPACE = 4f;
    private final String LABEL_TRUE = "Correct";
    private final String LABEL_FALSE = "Incorrect";
    private final String LABEL_NON = "No Answer";
    private final float FORM_SIZE_LEGEND = 10;
    private final float FORM_WIDTH_LEGEND = 10;
    private PieChart mPieChart;
    private Button mBtnReview;
    private Button mBtnNext;
    private ListView mLvPart;
    private View.OnClickListener mOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_answer);

        List<Part> listPart = getDataPart(mAnswers);
        initView();
        setUpPieChart(mAnswers);
        setUpBarChart(listPart);
        listenView();
    }

    private void initView() {
        Log.i(TAG, "initView(): is called");

        mPieChart = findViewById(R.id.pie_chart_answer);
        mBtnNext = findViewById(R.id.btn_next);
        mBtnReview = findViewById(R.id.btn_review);
        mLvPart = findViewById(R.id.lv_part);

        mOnClickListener = view -> {
            switch (view.getId()) {
                case R.id.btn_review:
                    moveToReviewActivity();
                    break;
                case R.id.btn_next:
                    moveToHomeActivity();
                    break;
            }
        };
    }

    private void moveToHomeActivity() {
        Log.i(TAG, "moveToHomeActivity(): is called");
    }

    private void moveToReviewActivity() {
        Log.i(TAG, "moveToReviewActivity(): is called");
        Intent intent = new Intent(this, TestActivity.class);
        mInformation.type = TYPE_ANSWER;
        intent.putExtra(KEY_INFORMATION, mInformation);
        startActivity(intent);
    }

    private void listenView() {
        Log.i(TAG, "listenView(): is called");

        mBtnReview.setOnClickListener(mOnClickListener);
        mBtnReview.setOnClickListener(mOnClickListener);
    }

    private void setUpBarChart(List<Part> listPart) {
        Log.i(TAG, "setUpBarChart(): is called");

        PartAdapter partAdapter = new PartAdapter((ArrayList<Part>) listPart);
        mLvPart.setAdapter(partAdapter);
    }

    private void setUpPieChart(List<Answer> listAnswer) {
        Log.i(TAG, "setUpPieChart(): is called");

        List<PieEntry> data = setUpDataPieChart(listAnswer);
        List<Integer> colors = new ArrayList<>(Arrays.asList(
                COLOR_TRUE, COLOR_FALSE, COLOR_NON
        ));

        List<LegendEntry> listLegend = new ArrayList<>(Arrays.asList(
                new LegendEntry(LABEL_TRUE, Legend.LegendForm.SQUARE, FORM_SIZE_LEGEND, FORM_WIDTH_LEGEND, null, COLOR_TRUE),
                new LegendEntry(LABEL_FALSE, Legend.LegendForm.SQUARE, FORM_SIZE_LEGEND, FORM_WIDTH_LEGEND, null, COLOR_FALSE),
                new LegendEntry(LABEL_NON, Legend.LegendForm.SQUARE, FORM_SIZE_LEGEND, FORM_WIDTH_LEGEND, null, COLOR_NON)
        ));

        Legend legend = mPieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextColor(Color.WHITE);
        legend.setDrawInside(false);
        legend.setEnabled(true);
        legend.setCustom(listLegend);
        legend.setTextSize(15f);

        PieDataSet dataSet = new PieDataSet(data, "Expense Category");
        dataSet.setSliceSpace(SLICE_SPACE);
        dataSet.setValueLineColor(Color.WHITE);
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new ValueFormatInt(mPieChart));
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTypeface(Typeface.DEFAULT_BOLD);
        pieData.setValueTextSize(18f);
        pieData.setDrawValues(true);
        mPieChart.setData(pieData);

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setUsePercentValues(true);
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setCenterTextSize(25f);
        mPieChart.setCenterTextColor(Color.GREEN);


        mPieChart.invalidate();
    }


    private List<PieEntry> setUpDataPieChart(List<Answer> listAnswer) {
        Log.i(TAG, "getDataSummary(): is call with size of list number = [" + listAnswer.size() + "]");

        Part partAbstract = new Part();
        countAnswer(partAbstract, listAnswer);

        mPieChart.setCenterText(partAbstract.numberTrue + "/" + (partAbstract.numberTrue + partAbstract.numberFalse + partAbstract.numberNon));

        return new ArrayList<>(Arrays.asList(
                new PieEntry(partAbstract.numberTrue, partAbstract.numberTrue),
                new PieEntry(partAbstract.numberFalse, partAbstract.numberFalse),
                new PieEntry(partAbstract.numberNon, partAbstract.numberNon)
        ));
    }

    private static class ValueFormatInt extends ValueFormatter {
        private DecimalFormat mFormat;
        private PieChart mPieChart;

        public ValueFormatInt() {
            mFormat = new DecimalFormat("###,###,##");
        }

        public ValueFormatInt(PieChart pieChart) {
            this();
            mPieChart = pieChart;
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + " %";
        }

        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            if (mPieChart != null && mPieChart.isUsePercentValuesEnabled()) {
                return getFormattedValue(value);
            } else {
                return mFormat.format(value);
            }
        }

    }

    private List<Part> getDataPart(List<Answer> listAnswer) {
        Log.i(TAG, "getDataPart(): is called with size of list answer = [" + listAnswer.size() + "]");

        List<Part> dataParts = new ArrayList<>();

        for (Part part : TestActivity.mParts) {
            List<Answer> listAnswerByPart = listAnswer.stream()
                    .filter(answer -> answer.part == part.part)
                    .collect(Collectors.toList());
            countAnswer(part, listAnswerByPart);
            dataParts.add(part);
        }

        return dataParts;
    }

    private void countAnswer(Part part, List<Answer> listAnswer) {
        Log.i(TAG, "countAnswer(): is called");

        part.numberTrue = 0;
        part.numberFalse = 0;
        part.numberNon = 0;
        for (Answer answer : listAnswer) {
            Log.i(TAG, "countAnswer: " + answer);
            if (answer.answer == null) {
                part.numberNon += 1;
                continue;
            }
            if (answer.answer.equals(answer.correct)) {
                part.numberTrue += 1;
            } else {
                part.numberFalse += 1;
            }
        }
        Log.i(TAG, "countAnswer(): " + part);
    }
}