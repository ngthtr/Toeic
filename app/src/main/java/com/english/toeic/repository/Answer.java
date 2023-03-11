package com.english.toeic.repository;

import androidx.annotation.NonNull;

public class Answer {

    public int year;
    public int test;
    public int part;
    public int number;
    public String correct;
    public String answer;
    public String audio;

    public Answer(int year, int test, int part, int number, String correct, String answer, String audio) {
        this.year = year;
        this.test = test;
        this.part = part;
        this.number = number;
        this.correct = correct;
        this.answer = answer;
        this.audio = audio;
    }

    public Answer(int part, int number, String correct) {
        this.part = part;
        this.number = number;
        this.correct = correct;
    }



    public Answer(int number, String correct, String answer, String audio) {
        this.number = number;
        this.correct = correct;
        this.answer = answer;
        this.audio = audio;
    }

    @NonNull
    @Override
    public String toString() {
        return "Answer: year = [" + year + "], test = [" +test + "], part = [" + part + "], " +
                "number = [" + number + "], correct = [" + correct + "], answer = [" + answer + "], " +
                "audio = [" + audio + "]";
    }
}
