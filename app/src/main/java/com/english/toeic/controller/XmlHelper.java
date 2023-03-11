package com.english.toeic.controller;

import android.content.Context;
import android.util.Log;


import com.english.toeic.repository.Answer;
import com.english.toeic.repository.Part;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlHelper {
    private static final String TAG = "XmlHelper";
    private final String FILE_PART = "file_part";
    private final String FILE_ANSWER = "file_answer";

    private final String ATTR_YEAR = "year";
    private final String ATTR_TEST = "test";
    private final String ATTR_PART = "part";
    private final String ATTR_NUMBER = "number";
    private final String ATTR_TYPE = "type";
    private final String ATTR_CATEGORY = "category";
    private final String ATTR_FILE = "file";
    private final String ATTR_CORRECT = "correct";
    private final String ATTR_AUDIO = "audio";
    private final String ATTR_LINK = "link";

    private final String ELEMENT_ANSWER = "Answer";
    private final String ELEMENT_PART = "Part";

    private final String TYPE_XML = "xml";
    private Context mContext;

    public XmlHelper(Context context) {
        mContext = context;
    }

    private int getIdByName(String fileName, String type) {
        Log.i(TAG, "getIdByName(): is called with file_name = [" + fileName + "], type = [" + type + "]");

        return mContext.getResources().getIdentifier(fileName, type, mContext.getPackageName());
    }

    public List<Answer> getAnswerFromXml() {
        Log.i(TAG, "getAnswers(): is called");

        int fileId = getIdByName(FILE_ANSWER, TYPE_XML);
        XmlPullParser parser = mContext.getResources().getXml(fileId);
        if (parser == null) {
            Log.e(TAG, "getAnswers(): parser is null");
            return null;
        }

        List<Answer> answers = new ArrayList<>();
        try {
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                if (parser.getName().equals(ELEMENT_ANSWER)) {
                    int year = Integer.parseInt(parser.getAttributeValue(null, ATTR_YEAR));
                    int test = Integer.parseInt(parser.getAttributeValue(null, ATTR_TEST));
                    int part = Integer.parseInt(parser.getAttributeValue(null, ATTR_PART));
                    int number = Integer.parseInt(parser.getAttributeValue(null, ATTR_NUMBER));
                    String category = parser.getAttributeValue(null, ATTR_CATEGORY);
                    String correct = parser.getAttributeValue(null, ATTR_CORRECT);
                    String audio = parser.getAttributeValue(null, ATTR_AUDIO);
                    answers.add(new Answer(year, test, part, number, correct, null, audio));
                    Log.d(TAG, "getAnswers(): read success with year = [" + year + "], " +
                            "test = [" + test + "], part = [" + part + "], number = [" + number + "], " +
                            "category = [" + category + "], correct = [" + correct + "], " +
                            "answer = [null], audio = [" + audio + "]");
                }
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "getAnswers(): has a error", e);
        }

        return answers;
    }

    public List<Part> getPartFromXml() {
        Log.i(TAG, "getFiles(): is called");

        int fileId = getIdByName(FILE_PART, TYPE_XML);
        XmlPullParser parser = mContext.getResources().getXml(fileId);
        if (parser == null) {
            Log.e(TAG, "getFiles(): parser is null");
            return null;
        }

        List<Part> files = new ArrayList<>();
        try {
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                if (parser.getName().equals(ELEMENT_PART)) {
                    int year = Integer.parseInt(parser.getAttributeValue(null, ATTR_YEAR));
                    int test = Integer.parseInt(parser.getAttributeValue(null, ATTR_TEST));
                    int part = Integer.parseInt(parser.getAttributeValue(null, ATTR_PART));
                    String type = parser.getAttributeValue(null, ATTR_TYPE);
                    String file = parser.getAttributeValue(null, ATTR_FILE);
                    String link = parser.getAttributeValue(null, ATTR_LINK);
                    Log.d(TAG, "getFiles(): read success with year = [" + year + "], " +
                            "test = [" + test + "], part = [" + part + "], type = [" + type + "], " +
                            "name = [" + file + "], link = [" + link + "]");
                    files.add(new Part(year, test, part, type, file, link));
                }
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "getFiles(): has a error", e);
        }

        return files;
    }


}
