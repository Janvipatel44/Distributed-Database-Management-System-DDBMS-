package sql.processor;

import dataFiles.db.databaseStructures;
import sql.sql.InternalQuery;

public class DropProcessor implements IProcessor{
    static DropProcessor instance = null;

    private String username = null;
    private String database = null;

    public static DropProcessor instance(databaseStructures dbsinjectintomethod){
        if(instance == null){
            instance = new DropProcessor();
        }
        return instance;
    }

    public String getDatabase(){return database;}
    @Override
    public databaseStructures process(InternalQuery query,  String q,String username, String database, databaseStructures dbs) {
        this.username = username;
        this.database = database;

        String table = (String) query.get("table");
        table = table.trim();
        String to_remove = "";
        for(String key : dbs.databasedata.keySet())
        {
            if (key.equals(table)){
                to_remove = key;
            }


        }
        for (String key : dbs.tableStructure.keySet())
        {
            if(key.equals(table)){
                to_remove = key;
            }
        }
        dbs.databasedata.remove(to_remove);
        dbs.tableStructure.remove(to_remove);
        if(to_remove.equals("")){
            System.out.println("No table found");

        }
        else
            System.out.println("Deleted table");
        return dbs;
    }
}
