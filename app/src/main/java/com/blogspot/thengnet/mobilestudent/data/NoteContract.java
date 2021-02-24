package com.blogspot.thengnet.mobilestudent.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NoteContract {

    public static final String TABLE_NAME = "notes";

    public static final String SCHEMA = "content://";
    public static final String CONTENT_AUTHORITY = "com.blogspot.thengnet.mobilestudent";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEMA + CONTENT_AUTHORITY);
    public static final String NOTES_PATH = "notes";

    /**
     * A private constructor to prevent instantiation of the contract class.
     */
    private NoteContract () {}

    public static final class NoteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, NOTES_PATH);

        /**
         * ID column of the database: Type - TEXT.
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Subject/title of the note: Type - TEXT.
         */
        public static final String COLUMN_NOTE_TITLE = "title";

        /**
         * Content of the note: Type - TEXT.
         */
        public static final String COLUMN_NOTE_CONTENT = "content";

        /**
         * Date and time the note was last updated: Type - TEXT.
         */
        public static final String COLUMN_NOTE_PREVIOUS_UPDATE = "last_update";

    }
}
