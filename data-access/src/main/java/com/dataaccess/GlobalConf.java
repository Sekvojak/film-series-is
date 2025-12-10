package com.dataaccess;

import com.dataaccess.connector.IDataConnector;
import com.dataaccess.connector.SqlConnector;
import com.dataaccess.connector.TextConnector;
import com.dataaccess.db.DatabaseInit;
import lombok.Getter;

public class GlobalConf {

    @Getter
    private static IDataConnector connector;

    @Getter
    private static DatabaseMode currentMode = DatabaseMode.SQL;

    public static void init() {
        setDatabaseMode(currentMode);
    }

    public static void setDatabaseMode(DatabaseMode mode) {
        currentMode = mode;

        if (mode == DatabaseMode.SQL) {
            DatabaseInit.init();
            connector = new SqlConnector();
            System.out.println("Používa sa SQL databáza");
        } else {
            connector = new TextConnector();
            System.out.println("Používa sa textová databáza");
        }
    }

    public static boolean isTextMode() {
        return currentMode == DatabaseMode.TEXT;
    }
}
