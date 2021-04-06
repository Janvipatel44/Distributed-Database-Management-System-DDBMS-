package sql.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sql.sql.InternalQuery;
import dataFiles.db.databaseStructures;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class InsertProcessor implements IProcessor {
    static final Logger logger = LogManager.getLogger(InsertProcessor.class.getName());

    static InsertProcessor instance = null;

    private String username = null;
    private String database = null;

    public static InsertProcessor instance(databaseStructures dbsinjectintomethod)
    {
        if (instance == null) {
            instance = new InsertProcessor();
        }
        return instance;
    }

    public String getDatabase() {
        return database;
    }

    HashMap<String, String> rowdata;
    HashMap<String, HashMap<String, String>> all_rows;
    HashMap<String, HashMap<String, String>> first_entry;

    @Override
    public databaseStructures process(InternalQuery query, String q, String username, String database, databaseStructures dbs) {
        first_entry = new HashMap<>();
        rowdata = new HashMap<>();
        this.username = username;
        this.database = database;
        String table = (String) query.get("table");
        String primaryKey = null;

        if (dbs.primaryKey_Hashtable.containsKey(table)) {
            primaryKey = dbs.primaryKey_Hashtable.get(table);
        }

        ArrayList<String> uniqueItem = new ArrayList<>();
        if(dbs.databasedata.get(table) != null) {
            for (String row : dbs.databasedata.get(table).keySet())
            {
                for (String col : dbs.databasedata.get(table).get(row).keySet())
                {
                    if (col.equals(primaryKey))
                    {
                        uniqueItem.add(dbs.databasedata.get(table).get(row).get(col));
                    }
                }
            }
        }
        all_rows = dbs.databasedata.get(table);

        int flag = 0;
        if (all_rows == null) {
            flag = 1;
        }
        System.out.println(all_rows);
        String[] columns = (String[]) query.get("columns");
        String[] values = (String[]) query.get("values");
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].equals(primaryKey) && uniqueItem.contains(values[i])) {
                rowdata.clear();
                System.out.println("Can't insert due to primary key contraints");
                break;
            }
            rowdata.put(columns[i], values[i]);
        }

        System.out.println(rowdata);
        int hashmap_size = 0;
        if (flag == 1) {
            first_entry.put("row1", rowdata);
            dbs.databasedata.put(table, first_entry);
        } else {
            hashmap_size = all_rows.size();
            int next_row_to_enter = hashmap_size + 1;
            if(!rowdata.isEmpty()) {
                all_rows.put("row" + next_row_to_enter, rowdata);
            }
            dbs.databasedata.put(table, all_rows);
        }
        return dbs;
    }
}
