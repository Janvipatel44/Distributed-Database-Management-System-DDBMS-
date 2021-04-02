package sql.processor;

import logging.events.CrashListener;
import logging.events.DatabaseListener;
import logging.events.QueryListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sql.sql.InternalQuery;

import java.io.*;

public class InsertProcessor {
    static final Logger logger = LogManager.getLogger(InsertProcessor.class.getName());
    static final CrashListener crashListener = new CrashListener();
    static final DatabaseListener databaseListener = new DatabaseListener();
    static final QueryListener queryListener = new QueryListener();

    String BASE_PATH = "src/main/java/dataFiles/";
    static InsertProcessor instance = null;

    private String username = null;
    private String database = null;

    public static InsertProcessor instance(){
        if(instance == null){
            instance = new InsertProcessor();
        }
        return instance;
    }


    public boolean process(InternalQuery query, String username, String database) throws IOException {
        this.username = username;
        this.database = database;

        String table = (String) query.get("table");
        logger.info("Identifying columns");
        String[] columns = (String[]) query.get("columns");
        logger.info("Identifying values");
        String[] values = (String[]) query.get("values");

        if(columns.length != values.length){
            System.out.println("Invalid columns and values pair.");
            return false;
        }

        logger.info("Selecting/Creating internal files to update");
        String path = BASE_PATH+"db"+"/"+table+".txt";
        String data_to_store = "";
        FileWriter fileWriter = new FileWriter(path,true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(int i=0; i< columns.length;i++){
            data_to_store = data_to_store + values[i];
            if(i == columns.length - 1) {
                break;
            }
            else {
                data_to_store = data_to_store.concat(",");
            }
        }
        printWriter.println(data_to_store);
        printWriter.println("\n");
        printWriter.close();

        return true;
    }

}
