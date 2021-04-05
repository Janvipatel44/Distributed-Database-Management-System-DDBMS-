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

    public databaseStructures process(InternalQuery query,  String q,String username,  String database, databaseStructures dbs) {
        this.username = username;
        this.database = database;
        int columnFlag = 0;
        int conditionFlag = 0;

        logger.info("Identifying requested columns");
        String table = query.getTableName().trim().replaceAll(",", "");
        String y = query.getOption();
        String[] columnValue = y.split(" ");
        for (int i=0;i<columnValue.length;i++) {

            System.out.println(columnValue[i]);
        }

        String x = query.getCondition().trim().replaceAll(",", "");
        String[] conditions = x.split(" ");
        for (int i=0;i<conditions.length;i++) {

            System.out.println(conditions[i]);
        }
        //Check if column names in the query is valid
       /* String[] columnInTable = dbs.databasedata.get("employee").get(1).keySet().toArray(new String[0]);
        for (int i=0;i<columnInTable.length;i++) {
            System.out.println("columnName" +columnInTable[i]);
        }
        for (String column : columnInTable) {
                if (column.equals(columnValue[0]))
                {
                    columnFlag = 1;
                }
                if (column.equals(conditions[0]))
                {
                    conditionFlag = 1;
                }
        }
        logger.info("Identifying requested conditions");*/


        //if (columnFlag == 1 && conditionFlag == 1) {

        System.out.println(dbs.databasedata.get(table).get("row1").get(conditions[0]));
        System.out.println("condition " +conditions[2]);
                for (int j = 0; j < columnValue.length; j++) {
                    System.out.println("J" +j);

                    for (int i = 0; i < conditions.length; i++) {

                        System.out.println("I" +i);
                        if (conditions[i].equals("=")) {
                            for(int k=1;k<dbs.databasedata.get(table).size();k++) {
                                if (dbs.databasedata.get(table).get("row"+k).get(conditions[i - 1]).equals(conditions[i + 1])) {
                                    dbs.databasedata.get(table).get("row"+k).put(columnValue[j], columnValue[j + 2]);
                                }
                            }
                        } else if (conditions[1].equals(">") ) {
                            if (Integer.parseInt((dbs.databasedata.get(table).get("row1").get(conditions[i-1]))) > Integer.parseInt(conditions[i+1])) {
                                dbs.databasedata.get(table).get("row1").put(columnValue[j], columnValue[j + 2]);
                            }
                        }
                            else if (conditions[1].equals(">=") ) {
                            if (Integer.parseInt((dbs.databasedata.get(table).get("row1").get(conditions[i-1]))) >= Integer.parseInt(conditions[i+1])) {
                                dbs.databasedata.get(table).get("row1").put(columnValue[j], columnValue[j + 2]);
                            }
                        }
                        else if (conditions[1].equals("<")) {
                            if (Integer.parseInt((dbs.databasedata.get(table).get("row1").get(conditions[i-1]))) < Integer.parseInt(conditions[i+1])) {

                                dbs.databasedata.get(table).get("row1").put(columnValue[j], columnValue[j + 2]);
                            }
                        }
                            else if (conditions[1].equals("<=")) {
                            if (Integer.parseInt((dbs.databasedata.get(table).get("row1").get(conditions[i-1]))) <= Integer.parseInt(conditions[i+1])) {

                                dbs.databasedata.get(table).get("row1").put(columnValue[j], columnValue[j + 2]);
                            }
                        }
                         else if (conditions[1].equals("!=")) {
                            if (Integer.parseInt((dbs.databasedata.get(table).get("row1").get(conditions[i-1]))) != Integer.parseInt(conditions[i+1])) {

                                dbs.databasedata.get(table).get("row1").put(columnValue[j], columnValue[j + 2]);
                            }
                        } else {
                            System.out.println("Check your conditions and try again !!");
                        }
                    }
                }
                System.out.println("Update done successfully !!" +dbs.databasedata.get(table).get("row1"));
                System.out.println("Update done successfully !!" +dbs.databasedata.get(table).get("row2"));

        //   } else {
                System.out.println("Invalid column name !!");
                //return null;
        //}

        //System.out.println("Sorry wrong condition !!");
        return dbs;
    }
}