package com.english.toeic.repository;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Information implements Serializable {
    public int year;
    public int test;
    public String type;
    public List<Integer> parts;

    public Information() {}

    @NonNull
    public String toString() {
        return "Information: year = [" + year + "], test = [" + test + "], type = [" + type + "], parts = " + parts;
    }
}
