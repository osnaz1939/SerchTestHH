package com.act_ex.a1221.serchtesthh;

/**
 * Created by brost_host17 on 13.04.2017.
 */

public class TableItems {
    public static final String NAME = TableItems.class.getSimpleName().toLowerCase();
    public static final String _ID = "_id";
    public static final String TEXT = "text";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + NAME +
                    " ( " +
                    _ID + " integer primary key autoincrement, " +
                    TEXT + " text " +
                    " ); ";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NAME;
    public static String[] Columns = new String[]{_ID, TEXT};
}