package com.english.toeic.repository;

import static com.english.toeic.constant.TestConstant.TYPE_QUESTION;
import static com.english.toeic.view.TestActivity.KEY_INFORMATION;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.english.toeic.view.CheckResourceActivity;
import com.english.toeic.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@RequiresApi(api = Build.VERSION_CODES.O)
public class TestAdapter extends BaseAdapter {
    private static final String TAG = "TestAdapter";
    private final Context mContext;
    private final int mWidthDialog;
    private final int mHeightDialog;
    private final List<Integer> mIdRadios;
    private final List<Integer> mIdCheckBoxes;
    private List<String> mArrTest;
    private Information mInformation;

    public TestAdapter(Context context, List<String> arrTest) {
        mContext = context;
        mArrTest = arrTest;

        mWidthDialog = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
        mHeightDialog = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.8);

        mIdRadios = new ArrayList<>(Arrays.asList(
                R.id.rb_test_01, R.id.rb_test_02, R.id.rb_test_03, R.id.rb_test_04, R.id.rb_test_05,
                R.id.rb_test_06, R.id.rb_test_07, R.id.rb_test_08, R.id.rb_test_09, R.id.rb_test_10
        ));

        mIdCheckBoxes = new ArrayList<>(Arrays.asList(
                R.id.cb_part_1, R.id.cb_part_2, R.id.cb_part_3, R.id.cb_part_4, R.id.cb_part_5,
                R.id.cb_part_6, R.id.cb_part_7
        ));

        mInformation = new Information();
    }

    @Override
    public int getCount() {
        return mArrTest.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrTest.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewTest;
        if (view == null) {
            viewTest = View.inflate(viewGroup.getContext(), R.layout.item_test, null);
        } else {
            viewTest = view;
        }

        String test = (String) getItem(i);
        Button btnTest = viewTest.findViewById(R.id.btn_test);
        btnTest.setText(test);
        btnTest.setOnClickListener(view1 -> {
            mInformation.type = TYPE_QUESTION;
            mInformation.year = Integer.parseInt(test);
            showDialogSelectTest();
        });
        return viewTest;
    }

    private void showDialogSelectTest() {
        Log.i(TAG, "showDialogSelectTest(): is called");

        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_select_test);
        dialog.getWindow().setLayout(mWidthDialog, mHeightDialog);

        Button.OnClickListener onClickListener = view -> {
            switch (view.getId()) {
                case R.id.btn_agree:
                    RadioGroup rgTest = dialog.findViewById(R.id.rg_test);
                    int idRadios = rgTest.getCheckedRadioButtonId();
                    for (int id : mIdRadios) {
                        if (idRadios == id) {
                            mInformation.test = mIdRadios.indexOf(id) + 1;
                            break;
                        }
                    }
                    if (rgTest.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(mContext, "You should select at least a test!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    dialog.dismiss();
                    showDialogSelectPart();
                    break;
                case R.id.btn_cancel:
                    dialog.dismiss();
                    break;
            }

        };

        Button btnAgree = dialog.findViewById(R.id.btn_agree);
        btnAgree.setOnClickListener(onClickListener);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(onClickListener);

        dialog.show();
    }

    private void showDialogSelectPart() {
        Log.i(TAG, "showDialogSelect(): is called");

        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_select_part);
        dialog.getWindow().setLayout(mWidthDialog, mHeightDialog);

        View.OnClickListener onClickListener = view -> {
            switch (view.getId()) {
                case R.id.btn_full_test:
                    handleSelectPart(dialog, 0, mIdCheckBoxes.size());
                    break;
                case R.id.btn_reading_test:
                    handleSelectPart(dialog, 4, mIdCheckBoxes.size());
                    break;
                case R.id.btn_listening_test:
                    handleSelectPart(dialog, 0, 4);
                    break;
                case R.id.btn_agree:
                    List<Integer> parts = getSelectedParts(dialog);
                    if (parts.size() == 0) {
                        Toast.makeText(mContext, "You should select at least a part", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    mInformation.parts = parts;
                    dialog.dismiss();
                    Log.i(TAG, "showDialogSelectPart: " + mInformation.toString());
                    startCheckResourceActivity();
                    break;
                case R.id.btn_cancel:
                    dialog.dismiss();
                    break;
            }
        };

        dialog.findViewById(R.id.btn_full_test).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.btn_reading_test).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.btn_listening_test).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.btn_agree).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(onClickListener);
        dialog.show();
    }

    private void handleSelectPart(Dialog dialog, int idStart, int idEnd) {
        Log.i(TAG, "handleSelectPart: is called with id_start = [" + idStart + "], id_end = [" + idEnd + "]");
        for (int id : mIdCheckBoxes) {
            ((CheckBox) dialog.findViewById(id)).setChecked(false);
        }
        for (int i = idStart; i < idEnd; i++) {
            CheckBox checkBox = dialog.findViewById(mIdCheckBoxes.get(i));
            checkBox.setChecked(true);
        }
    }

    private List<Integer> getSelectedParts(Dialog dialog) {
        Log.i(TAG, "getSelectedParts(): is called");

        List<Integer> parts = new ArrayList<>();
        for (int id : mIdCheckBoxes) {
            CheckBox checkBox = dialog.findViewById(id);
            if (checkBox.isChecked()) {
                parts.add(mIdCheckBoxes.indexOf(id) + 1);
            }
        }
        return parts;
    }

    private void startCheckResourceActivity() {
        Log.i(TAG, "startCheckResourceActivity(): is called");

        Intent intent = new Intent(mContext, CheckResourceActivity.class);
        intent.putExtra(KEY_INFORMATION,  mInformation);
        mContext.startActivity(intent);
    }

}













