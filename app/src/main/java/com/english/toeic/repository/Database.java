package com.english.toeic.repository;

import static com.english.toeic.constant.DatabaseContract.DATABASE_NAME;
import static com.english.toeic.constant.DatabaseContract.DATABASE_VERSION;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.english.toeic.constant.DatabaseContract;
import com.english.toeic.controller.XmlHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String TAG = "Database";

    private static Database mDatabase;
    private XmlHelper mXmlHelper;

    public static Database getInstance(@Nullable Context context) {
        if (mDatabase == null) {
            mDatabase = new Database(context);
        }
        return mDatabase;
    }

    private Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mXmlHelper = new XmlHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createAnswerTable(sqLiteDatabase);
        initDataAnswer(sqLiteDatabase);

        createPartTable(sqLiteDatabase);
        initDataPart((sqLiteDatabase));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void createAnswerTable(SQLiteDatabase database) {
        Log.i(TAG, "createAnswerTable(): is called");

        String query = "CREATE TABLE IF NOT EXISTS " + DatabaseContract.AnswerTable.NAME_TABLE + " ("
                + DatabaseContract.AnswerTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.AnswerTable.COLUMN_YEAR + " INTEGER, "
                + DatabaseContract.AnswerTable.COLUMN_TEST + " INTEGER, "
                + DatabaseContract.AnswerTable.COLUMN_PART + " INTEGER, "
                + DatabaseContract.AnswerTable.COLUMN_NUMBER + " INTEGER, "
                + DatabaseContract.AnswerTable.COLUMN_CORRECT + " TEXT, "
                + DatabaseContract.AnswerTable.COLUMN_AUDIO + " TEXT)";
        database.execSQL(query);
    }

    private void createPartTable(SQLiteDatabase database) {
        Log.i(TAG, "createFileTable(): is called");

        String query = "CREATE TABLE IF NOT EXISTS " + DatabaseContract.PartTable.NAME_TABLE + " ("
                + DatabaseContract.PartTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.PartTable.COLUMN_YEAR + " INTEGER, "
                + DatabaseContract.PartTable.COLUMN_TEST + " INTEGER, "
                + DatabaseContract.PartTable.COLUMN_PART + " INTEGER, "
                + DatabaseContract.PartTable.COLUMN_TYPE + " TEXT, "
                + DatabaseContract.PartTable.COLUMN_FILE + " TEXT, "
                + DatabaseContract.PartTable.COLUMN_LINK + " TEXT)";
        database.execSQL(query);
    }

    private void initDataAnswer(SQLiteDatabase database) {
        Log.i(TAG, "initDataAnswer(): is called");

        List<Answer> answers = mXmlHelper.getAnswerFromXml();
        if (answers == null) {
            Log.e(TAG, "insertAnswers(): list of answers is null");
            return;
        }
        for (Answer answer : answers) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.AnswerTable.COLUMN_YEAR, answer.year);
            values.put(DatabaseContract.AnswerTable.COLUMN_TEST, answer.test);
            values.put(DatabaseContract.AnswerTable.COLUMN_PART, answer.part);
            values.put(DatabaseContract.AnswerTable.COLUMN_NUMBER, answer.number);
            values.put(DatabaseContract.AnswerTable.COLUMN_CORRECT, answer.correct);
            values.put(DatabaseContract.AnswerTable.COLUMN_AUDIO, answer.audio);
            database.insert(DatabaseContract.AnswerTable.NAME_TABLE, null, values);
            Log.d(TAG, "initDataAnswer(): insert success with "  + answer);
        }
    }

    private void initDataPart(SQLiteDatabase database) {
        Log.i(TAG, "initDataFile(): is called");

        List<Part> parts = mXmlHelper.getPartFromXml();
        if (parts == null) {
            Log.e(TAG, "initDataFile(): list of answers is null");
            return;
        }
        for (Part part : parts) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.PartTable.COLUMN_YEAR, part.year);
            values.put(DatabaseContract.PartTable.COLUMN_TEST, part.test);
            values.put(DatabaseContract.PartTable.COLUMN_PART, part.part);
            values.put(DatabaseContract.PartTable.COLUMN_TYPE, part.type);
            values.put(DatabaseContract.PartTable.COLUMN_FILE, part.file);
            values.put(DatabaseContract.PartTable.COLUMN_LINK, part.link);
            database.insert(DatabaseContract.PartTable.NAME_TABLE, null, values);
            Log.d(TAG, "initDataFile(): insert success with " + part);
        }
    }

    public List<Answer> getAnswers(int year, int test, int part) {
        Log.i(TAG, "getAnswers(): is called with year = [" + year + "], test = [" + test + "], part = [" + part + "]");

        SQLiteDatabase database = getReadableDatabase();
        String selection = DatabaseContract.AnswerTable.COLUMN_YEAR + " = ? and " + DatabaseContract.AnswerTable.COLUMN_TEST + " = ? and " + DatabaseContract.PartTable.COLUMN_PART + " = ?";
        String[] selectionArgs = {String.valueOf(year), String.valueOf(test), String.valueOf(part)};

        Cursor cursor = database.query(DatabaseContract.AnswerTable.NAME_TABLE, null, selection, selectionArgs, null, null, DatabaseContract.AnswerTable.COLUMN_NUMBER);
        if (cursor == null) {
            Log.e(TAG, "getAnswers(): cursor is null");
            return null;
        }
        List<Answer> answers = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    int indexNumber = cursor.getColumnIndex(DatabaseContract.AnswerTable.COLUMN_NUMBER);
                    int number = cursor.getInt(indexNumber);
                    int indexCorrect = cursor.getColumnIndex(DatabaseContract.AnswerTable.COLUMN_CORRECT);
                    String correct = cursor.getString(indexCorrect);
                    int indexAudio = cursor.getColumnIndex(DatabaseContract.AnswerTable.COLUMN_AUDIO);
                    String audio = cursor.getString(indexAudio);
                    Answer answer = new Answer(year, test, part, number, correct, null, audio);
                    answers.add(answer);
                    Log.d(TAG, "getAnswers(): get answer success with " + answer);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "getAnswers(): has a error");
        }

        return answers;
    }

    public Part getPart(int year, int test, int part) {
        Log.i(TAG, "getPart(): is called with year = [" + year + "], test = [" + test + "], part = [" + part + "]");

        SQLiteDatabase database = getReadableDatabase();
        String selection = DatabaseContract.PartTable.COLUMN_YEAR + " = ? and " + DatabaseContract.PartTable.COLUMN_TEST + " = ? and " + DatabaseContract.PartTable.COLUMN_PART + " = ?";
        String[] selectionArgs = {String.valueOf(year), String.valueOf(test), String.valueOf(part)};

        Cursor cursor = database.query(DatabaseContract.PartTable.NAME_TABLE, null, selection, selectionArgs, null, null, DatabaseContract.PartTable.COLUMN_PART);
        if (cursor == null) {
            Log.e(TAG, "getPart(): cursor is null");
            return null;
        }

        List<Part> parts = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    int indexFile = cursor.getColumnIndex(DatabaseContract.PartTable.COLUMN_FILE);
                    String file = cursor.getString(indexFile);
                    Part partObject = new Part(part, file);
                    parts.add(partObject);
                    Log.d(TAG, "getPart(): get part success with " + partObject);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "getPart(): has a error");
        }
        if (parts.size() != 1) {
            Log.e(TAG, "getPart(): Empty or more than one part is found");
            return null;
        }
        return parts.get(0);
    }

    public List<String> getMainAudio(int year, int test) {
        Log.i(TAG, "getMainAudio(): is called with year = [" + year + "], test = [" + test + "]");

        SQLiteDatabase database = getReadableDatabase();
        String selection = DatabaseContract.PartTable.COLUMN_YEAR + " = ? and " + DatabaseContract.PartTable.COLUMN_TEST + " = ? and " + DatabaseContract.PartTable.COLUMN_TYPE + " = 'au'";
        String[] selectionArgs = {String.valueOf(year), String.valueOf(test)};

        Cursor cursor = database.query(DatabaseContract.PartTable.NAME_TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor == null) {
            Log.e(TAG, "getMainAudio(): cursor is null");
            return null;
        }
        List<String> nameAudios = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    int index = cursor.getColumnIndex(DatabaseContract.PartTable.COLUMN_FILE);
                    String nameAudio = cursor.getString(index);
                    nameAudios.add(nameAudio);
                    Log.d(TAG, "getMainAudio(): get success with name_audio = [" + nameAudio + "]");
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "getMainAudio(): has a error");
        }
        return nameAudios;
    }


    public List<String> getAllYears() {
        Log.i(TAG, "getAllYears(): is called");

        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {DatabaseContract.PartTable.COLUMN_YEAR};

        Cursor cursor = database.query(DatabaseContract.PartTable.NAME_TABLE, columns, null, null, DatabaseContract.PartTable.COLUMN_YEAR, null, null);

        if (cursor == null) {
            Log.e(TAG, "getAllYears(): cursor is null");
            return null;
        }

        List<String> years = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    int indexYear = cursor.getColumnIndex(DatabaseContract.PartTable.COLUMN_YEAR);
                    String year = cursor.getString(indexYear);
                    Log.d(TAG, "getTests(): get success year = [" + year + "]");
                    years.add(year);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "getTests(): has a error", e);
        }

        return years;
    }

    public Part getPartFileLink(int year, int test, int part) {
        Log.i(TAG, "getPartFileLink(): is called with year = [" + year + "], test = [" + test + "], part = [" + part + "]");

        SQLiteDatabase database = getReadableDatabase();

        String select = DatabaseContract.PartTable.COLUMN_YEAR + " = ? and " + DatabaseContract.PartTable.COLUMN_TEST + " = ? and " + DatabaseContract.PartTable.COLUMN_PART + " = ?";
        String[] selectionArgs = {String.valueOf(year), String.valueOf(test), String.valueOf(part)};
        Cursor cursor = database.query(DatabaseContract.PartTable.NAME_TABLE, null, select, selectionArgs, null, null, null);
        if (cursor == null) {
            Log.i(TAG, "getPartFileLink(): cursor is null");
            return null;
        }

        List<Part> parts = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    int indexFile = cursor.getColumnIndex(DatabaseContract.PartTable.COLUMN_FILE);
                    String file = cursor.getString(indexFile);
                    int indexLink = cursor.getColumnIndex(DatabaseContract.PartTable.COLUMN_LINK);
                    String link = cursor.getString(indexLink);
                    Part partObject = new Part(file, link);
                    Log.i(TAG, "getPartFileLink(): get part success with " + partObject);
                    parts.add(partObject);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "getPartFileLink(): has a error ", e);
        }

        if (parts.size() != 1) {
            Log.e(TAG, "getPartFileLink(): Empty or mote than one part is found");
            return null;
        }
        return parts.get(0);
    }
}
