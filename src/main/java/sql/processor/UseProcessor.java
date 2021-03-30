package sql.processor;

import dataFiles.db.databaseStructures;
import logging.events.QueryListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sql.sql.InternalQuery;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UseProcessor implements IProcessor {
    static final Logger logger = LogManager.getLogger(UseProcessor.class.getName());
    static final QueryListener queryListener = new QueryListener();


    static UseProcessor instance = null;

    private String username = null;
    private String database = null;

    public static UseProcessor instance(databaseStructures dbsinjectintomethod){
        if(instance == null){
            instance = new UseProcessor();
        }
        return instance;
    }

    public String getDatabase() {
        return database;
    }

    @Override
    public databaseStructures process(InternalQuery query, String username, String database,databaseStructures dbs) {
        this.username = username;
        this.database = database;
        String newDatabase = (String) query.get("database");
//        System.out.println(newDatabase);
        dbs.populateDatabaseData(newDatabase);
        if(dbs.databasedata.get(newDatabase)!=null){
            if(dbs.selectedDb == null){
                System.out.println("Selecting database '"+ newDatabase+"'");
            }else{
                System.out.println("Changing database from '"+ dbs.selectedDb +"' to '"+ newDatabase+"'" );
            }
            dbs.selectedDb = newDatabase;
        }
        else{
            System.out.println("Database doesn't exist.");
        }
        return dbs;
    }
}
