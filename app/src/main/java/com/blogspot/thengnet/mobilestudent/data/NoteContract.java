package com.blogspot.thengnet.mobilestudent.data;

import android.provider.BaseColumns;

public final class NoteContract {

    public static final String TABLE_NAME = "notes";

    /**
     * A private constructor to prevent instantiation of the contract class.
     */
    private NoteContract () {}

    public static final class NoteEntry implements BaseColumns {

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
