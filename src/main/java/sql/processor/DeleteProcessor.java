package sql.processor;

import logging.events.CrashListener;
import logging.events.DatabaseListener;
import logging.events.QueryListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sql.sql.InternalQuery;
import dataFiles.db.databaseStructures;

import java.util.HashMap;

public class DeleteProcessor implements IProcessor {

    static DeleteProcessor instance = null;
    static final CrashListener crashListener = new CrashListener();
    static final DatabaseListener databaseListener = new DatabaseListener();
    static final QueryListener queryListener = new QueryListener();
    static final Logger logger = LogManager.getLogger(CreateProcessor.class.getName());

    private String username = null;
    private String database = null;

    public static DeleteProcessor instance(databaseStructures dbsinjectintomethod){
        if(instance == null){
            instance = new DeleteProcessor();
        }
        return instance;
    }

    public String getDatabase()
    {
        return database;
    }
    HashMap<String, HashMap<String, String>> all_rows;
    @Override
    public databaseStructures process(InternalQuery query, String q, String username, String database,databaseStructures dbs)
    {
        queryListener.recordEvent();
        String table = (String) query.get("table");

        if(!dbs.database_list.contains(table)) {
            crashListener.recordEvent();
        }

        this.database = database;
        String condition = (String) query.get("condition");
        table = table.trim();
        all_rows = dbs.databasedata.get(table);
        String conditions[] = condition.split(" ");

        String to_delete[] = new String[dbs.databasedata.get(table).size()];
        int i = 0;
        for(String key : dbs.databasedata.get(table).keySet()){
            for(String key2 : dbs.databasedata.get(table).get(key).keySet()){
                if(conditions[1].equals("=")){
                   if(key2.equals(conditions[0]) && dbs.databasedata.get(table).get(key).get(key2).equals(conditions[2])){
                       to_delete[i] = key;
                       databaseListener.recordEvent();
                       logger.info("deleted Entry");
                   }
                }
                if(conditions[1].equals("!=")){
                    if(key2.equals(conditions[0]) && !dbs.databasedata.get(table).get(key).get(key2).equals(conditions[2])){
                        to_delete[i] = key;
                        databaseListener.recordEvent();
                        logger.info("deleted Entry");
                    }
                }
                if(conditions[1].equals(">")){
                    if(key2.equals(conditions[0]) && Integer.parseInt(dbs.databasedata.get(table).get(key).get(key2)) > Integer.parseInt(conditions[2])){
                        to_delete[i] = key;
                        databaseListener.recordEvent();
                        logger.info("deleted Entry");
                    }
                }
                if(conditions[1].equals("<")){
                    if(key2.equals(conditions[0]) && Integer.parseInt(dbs.databasedata.get(table).get(key).get(key2)) < Integer.parseInt(conditions[2])){
                        to_delete[i] = key;
                        databaseListener.recordEvent();
                        logger.info("deleted Entry");
                    }
                }
                if(conditions[1].equals(">=")){
                    if(key2.equals(conditions[0]) && Integer.parseInt(dbs.databasedata.get(table).get(key).get(key2)) >= Integer.parseInt(conditions[2])){
                        to_delete[i] = key;
                        databaseListener.recordEvent();
                        logger.info("deleted Entry");
                    }
                }
                if(conditions[1].equals("<=")){
                    if(key2.equals(conditions[0]) && Integer.parseInt(dbs.databasedata.get(table).get(key).get(key2)) <= Integer.parseInt(conditions[2])){
                        to_delete[i] = key;
                        databaseListener.recordEvent();
                        logger.info("deleted Entry");
                    }
                }
            }
            i = i + 1;
        }
        for(int j = 0; j < to_delete.length; j++) {
            dbs.databasedata.get(table).remove(to_delete[j]);
        }
        return dbs;

    }

}
