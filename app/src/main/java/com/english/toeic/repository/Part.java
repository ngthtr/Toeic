package com.english.toeic.repository;

import static com.english.toeic.constant.TestConstant.CATEGORY_LISTENING;
import static com.english.toeic.constant.TestConstant.CATEGORY_READING;
import static com.english.toeic.constant.TestConstant.PART_I;
import static com.english.toeic.constant.TestConstant.PART_II;
import static com.english.toeic.constant.TestConstant.PART_III;
import static com.english.toeic.constant.TestConstant.PART_IV;
import static com.english.toeic.constant.TestConstant.PART_V;
import static com.english.toeic.constant.TestConstant.PART_VI;
import static com.english.toeic.constant.TestConstant.PART_VII;

import android.util.Log;

import androidx.annotation.NonNull;

public class Part {
    private static final String TAG = "Part";
    public int year; // 2022
    public int test; // 1
    public int part; // 1
    public int numberTrue; // 1
    public int numberFalse; // 1
    public int numberNon; // 1
    public String type; // q
    public String link; // 18Q2OiwYjNDQp1EeKXqewLOwh_6calRZ3
    private String name; // PART_I
    public String file; // ets_lc_22_01_q_p1

    public Part(int year, int test, int part, String type, String file, String link) {
        this.year = year;
        this.test = test;
        this.part = part;
        this.type = type;
        this.file = file;
        this.link = link;
    }

    public Part() {}
    public Part(String file, String link) {
        this.file = file;
        this.link = link;
    }

    public Part(String name, int numberTrue, int numberFalse, int numberNon) {
        this.name = name;
        this.numberTrue = numberTrue;
        this.numberFalse = numberFalse;
        this.numberNon = numberNon;
    }

    public Part(int part, String file) {
        this.part = part;
        this.file = file;
    }

    @NonNull
    @Override
    public String toString() {
        return "Part: year = [" + year + "], test = [" + test + "], part = [" + part + "], " +
                "type = [" + type + "], file = [" + file + "], link = [" + link + "], " +
                "true = [" + numberTrue + "], false = [" + numberFalse + "], non = [" + numberNon + "]";
    }

    public String getName() {
        if (name != null) {
            return name;
        }
        switch (part) {
            case 1:
                return PART_I;
            case 2:
                return PART_II;
            case 3:
                return PART_III;
            case 4:
                return PART_IV;
            case 5:
                return PART_V;
            case 6:
                return PART_VI;
            case 7:
                return PART_VII;
            default:
                Log.i(TAG, "getName(): cannot get name of part");
                return null;
        }
    }

    public String getCategory() {
        switch (part) {
            case 1:
            case 2:
            case 3:
            case 4:
                return CATEGORY_LISTENING;
            case 5:
            case 6:
            case 7:
                return CATEGORY_READING;
            default:
                Log.e(TAG, "getCategory(): cannot get category of part");
                return null;
        }
    }

    public int getNumberAnswer() {
        return numberTrue + numberNon + numberFalse;
    }
}
