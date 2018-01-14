package com.example.android.uda_habit_tracker;

import android.provider.BaseColumns;

/**
 * API Contract for the Habits app.
 */
public final class HabitContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private HabitContract() {
    }

    /**
     * Inner class that defines constant values for the habits database table.
     * Each entry in the table represents a single habit.
     */
    public static final class HabitEntry implements BaseColumns {

        /**
         * Name of database table for habits
         */
        public final static String TABLE_NAME = "habits";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_HABIT_NAME = "name";
        public final static String COLUMN_HABIT_PLACE = "place";
        public final static String COLUMN_HABIT_NUMBER = "number";
        public final static String COLUMN_HABIT_DURATION = "duration";


        public static final String NUMBER_SINGLE = "single";
        public static final String NUMBER_GROUP = "group";
        public static final String NUMBER_DEPENDS = "depends";
    }

}


