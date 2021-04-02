package sql.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sql.sql.InternalQuery;
import dataFiles.db.databaseStructures;

import java.util.HashMap;

public class InsertProcessor implements IProcessor {
    static final Logger logger = LogManager.getLogger(InsertProcessor.class.getName());

    static InsertProcessor instance = null;

    private String username = null;
    private String database = null;

    public static InsertProcessor instance(databaseStructures dbsinjectintomethod){
        if(instance == null){
            instance = new InsertProcessor();
        }
        return instance;
    }

    public String getDatabase(){return database;}
    HashMap<String ,String> rowdata;
    HashMap<String, HashMap<String, String>> all_rows;
    @Override
    public databaseStructures process(InternalQuery query, String username, String database,databaseStructures dbs) {
        rowdata = new HashMap<>();
        this.username = username;
        this.database = database;
        String table = (String) query.get("table");
        all_rows = dbs.databasedata.get(table);
        System.out.println(all_rows);
        String[] columns = (String[]) query.get("columns");
        String[] values = (String[]) query.get("values");
        for (int i = 0; i < columns.length; i++){
            rowdata.put(columns[i],values[i]);
        }
        //System.out.println(rowdata);
        int hashmap_size = all_rows.size();
        int next_row_to_enter = hashmap_size + 1;
        all_rows.put("row"+next_row_to_enter,rowdata);
        //System.out.println(all_rows);
        dbs.databasedata.put(table,all_rows);

        System.out.println(dbs.databasedata);

        return dbs;

    }

}
