package sql.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sql.sql.InternalQuery;
import dataFiles.db.databaseStructures;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteProcessor implements IProcessor {
    static final Logger logger = LogManager.getLogger(InsertProcessor.class.getName());

    static DeleteProcessor instance = null;

    private String username = null;
    private String database = null;

    public static DeleteProcessor instance(databaseStructures dbsinjectintomethod){
        if(instance == null){
            instance = new DeleteProcessor();
        }
        return instance;
    }

    public String getDatabase(){return database;}
    HashMap<String, HashMap<String, String>> all_rows;
    @Override
    public databaseStructures process(InternalQuery query, String username, String database,databaseStructures dbs) {
        this.username = username;
        this.database = database;
        String condition = (String) query.get("condition");
        String table = (String) query.get("table");
        table = table.trim();
        all_rows = dbs.databasedata.get(table);
        Pattern pattern = Pattern.compile("\\s(.*?)\\s(.*?)\\s(.*?)?;");
        Matcher matcher = pattern.matcher(condition);
        boolean match = matcher.matches ();

        return dbs;

    }

}
