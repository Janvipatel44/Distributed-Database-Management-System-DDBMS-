package sql.processor;
import dataFiles.db.databaseStructures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import logging.events.CrashListener;
import logging.events.QueryListener;
import sql.sql.InternalQuery;

public class UpdateProcessor implements IProcessor
{
    static final Logger logger = LogManager.getLogger(UpdateProcessor.class.getName());
    static final CrashListener crashListener = new CrashListener();
    static final QueryListener queryListener = new QueryListener();

    String BASE_PATH = "src/main/java/dataFiles/";
    static UpdateProcessor instance = null;

    private String username = null;
    private String database = null;

    public static UpdateProcessor instance() {
        if (instance == null) {
            instance = new UpdateProcessor ();
        }
        return instance;
    }

    public databaseStructures process(InternalQuery query, String username,  String database, databaseStructures dbs) {
        this.username = username;
        this.database = database;
        int columnFlag = 0;
        int conditionFlag = 0;

        logger.info("Identifying requested columns");
        String table = query.getTableName().trim();
        String y = query.getOption().replaceAll("[^a-zA-Z=]", " ");
        String[] columnValue = y.split(" ");

        String x = query.getCondition().replaceAll("[^a-zA-Z1-9]", " ");
        String[] conditions = x.split(" ");

        //Check if column names in the query is valid
        String[] columnInTable = dbs.databasedata.get("db_table").get(1).keySet().toArray(new String[0]);
        for (String column : columnInTable) {
                if (column.equals(columnValue[0])) {
                    columnFlag = 1;
                }
                if (column.equals(conditions[0])) {
                    conditionFlag = 1;
                }
        }
        logger.info("Identifying requested conditions");


        if (columnFlag == 1 && conditionFlag == 1) {

                for (int j = 0; j < columnValue.length; j++) {
                    for (int i = 0; i < dbs.databasedata.get("db_table").size(); i++) {
                        if (conditions[1] == "=") {
                            if (dbs.databasedata.get("db_table").get(i).get(conditions[0]) == conditions[2]) {
                                dbs.databasedata.get("db_table").get(i).put(columnValue[j], columnValue[j + 1]);
                            }
                        } else if (conditions[1] == ">=") {
                            if (Integer.parseInt((dbs.databasedata.get("db_table").get(i).get(conditions[0]))) >= Integer.parseInt(conditions[2])) {
                                dbs.databasedata.get("db_table").get(i).put(columnValue[j], columnValue[j + 1]);
                            }
                        } else if (conditions[1] == "<=") {
                            if (Integer.parseInt((dbs.databasedata.get("db_table").get(i).get(conditions[0]))) <= Integer.parseInt(conditions[2])) {
                                dbs.databasedata.get("db_table").get(i).put(columnValue[j], columnValue[j + 1]);
                            }
                        } else if (conditions[1] == "!=") {
                            if (Integer.parseInt((dbs.databasedata.get("db_table").get(i).get(conditions[0]))) != Integer.parseInt(conditions[2])) {
                                dbs.databasedata.get("db_table").get(i).put(columnValue[j], columnValue[j + 1]);
                            }
                        } else {
                            System.out.println("Check your conditions and try again !!");
                            return null;
                        }
                    }
                }
                System.out.println("Update done successfully !!");
            } else {
                System.out.println("Invalid column name !!");
                return null;
        }

        System.out.println("Sorry wrong condition !!");
        return dbs;
    }
}