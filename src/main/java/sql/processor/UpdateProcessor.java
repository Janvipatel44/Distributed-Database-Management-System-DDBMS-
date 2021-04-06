package sql.processor;
import dataFiles.db.databaseStructures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import logging.events.CrashListener;
import logging.events.QueryListener;
import sql.sql.InternalQuery;

import java.util.Locale;

public class UpdateProcessor implements IProcessor
{
    static final Logger logger = LogManager.getLogger(UpdateProcessor.class.getName());
    static final CrashListener crashListener = new CrashListener();
    static final QueryListener queryListener = new QueryListener();

    String BASE_PATH = "src/main/java/dataFiles/";
    static UpdateProcessor instance = null;

    private String username = null;
    private String database = null;

    public static UpdateProcessor instance(databaseStructures dbsinjectintomethod) {
        if (instance == null) {
            instance = new UpdateProcessor ();
        }
        return instance;
    }

    public String getDatabase(){
        return database;
    }

    public databaseStructures process(InternalQuery query, String q, String username, String database, databaseStructures dbs)
    {
        this.username = username;
        this.database = database;
        String name = query.getTableName();
        String option = query.getOption();
        String condition = query.getCondition();

        String conditions[] = condition.split(" ");
        String options[] = option.split(" ");
        name = name.trim();

        int i = 0;
        for(String key : dbs.databasedata.get(name).keySet()){
            for(String key2 : dbs.databasedata.get(name).get(key).keySet()){
                if(conditions[1].equals("=")){
                    if(key2.equals(conditions[0]) && dbs.databasedata.get(name).get(key).get(key2).equals(conditions[2])){
                        dbs.databasedata.get(name).get(key).put(key2, options[2]);
                        System.out.println("updated Entry");
                    }
                }
                else if(conditions[1].equals(">")){
                    if(key2.equals(conditions[0]) && Integer.parseInt(dbs.databasedata.get(name).get(key).get(key2)) > Integer.parseInt(conditions[2])){
                        dbs.databasedata.get(name).get(key).put(key2, options[2]);

                        System.out.println("updated Entry");

                    }
                }
                else if(conditions[1].equals("<")){
                    if(key2.equals(conditions[0]) && Integer.parseInt(dbs.databasedata.get(name).get(key).get(key2)) < Integer.parseInt(conditions[2])){
                        dbs.databasedata.get(name).get(key).put(key2, options[2]);

                        System.out.println("updated Entry");

                    }
                }
                else if(conditions[1].equals("!=")){
                    if(key2.equals(conditions[0]) && !dbs.databasedata.get(name).get(key).get(key2).equals(conditions[2])){
                        dbs.databasedata.get(name).get(key).put(key2, options[2]);
                        System.out.println("updated Entry");

                    }
                }
                else if(conditions[1].equals(">=")){
                    if(key2.equals(conditions[0]) && Integer.parseInt(dbs.databasedata.get(name).get(key).get(key2)) >= Integer.parseInt(conditions[2])){
                        dbs.databasedata.get(name).get(key).put(key2, options[2]);

                        System.out.println("updated Entry");

                    }
                }
                else if(conditions[1].equals("<=")){
                    if(key2.equals(conditions[0]) && Integer.parseInt(dbs.databasedata.get(name).get(key).get(key2)) <= Integer.parseInt(conditions[2])){
                        dbs.databasedata.get(name).get(key).put(key2, options[2]);
                        System.out.println("updated Entry");
                    }
                }
            }
            i = i + 1;
        }
        logger.info("Identifying requested columns");
        return dbs;
    }
}