package com.english.toeic.constant;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TestConstant {
    public static final String TYPE_QUESTION = "q";
    public static final String TYPE_ANSWER = "a";

    public static final String CATEGORY_LISTENING = "lc";
    public static final String CATEGORY_READING = "rc";

    public static final String ANSWER_A = "a";
    public static final String ANSWER_B = "b";
    public static final String ANSWER_C = "c";
    public static final String ANSWER_D = "d";

    public static final String PART_I = "PART_I";
    public static final String PART_II = "PART_II";
    public static final String PART_III = "PART_III";
    public static final String PART_IV = "PART_IV";
    public static final String PART_V = "PART_V";
    public static final String PART_VI = "PART_VI";
    public static final String PART_VII = "PART_VII";


    @StringDef({
            PART_I,
            PART_II,
            PART_III,
            PART_IV,
            PART_V,
            PART_VI,
            PART_VII
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PartName {

    }

    @StringDef({
            ANSWER_A,
            ANSWER_B,
            ANSWER_C,
            ANSWER_D
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Answers {

    }

}
