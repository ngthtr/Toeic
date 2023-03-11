package com.english.toeic.constant;

public class DatabaseContract {
    public static final String DATABASE_NAME = "database";
    public static final int DATABASE_VERSION = 1;

    public static class AnswerTable {
        public static final String NAME_TABLE = "answer_table";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_TEST = "test";
        public static final String COLUMN_PART = "part";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_CORRECT = "correct";
        public static final String COLUMN_AUDIO = "audio";
    }

    public static class PartTable {
        public static final String NAME_TABLE = "file_table";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_TEST = "test";
        public static final String COLUMN_PART = "part";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_FILE = "file";
        public static final String COLUMN_LINK = "link";
    }


}
