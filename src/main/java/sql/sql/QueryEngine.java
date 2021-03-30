package sql.sql;

import dataFiles.db.databaseStructures;
import sql.parser.CreateParser;
import sql.parser.UseParser;
import sql.processor.CreateProcessor;
import sql.processor.UseProcessor;

public class QueryEngine {
    private String database = null;
    private databaseStructures databaseStructures;

    public QueryEngine(databaseStructures dbs){
        this.databaseStructures =dbs;
    }

    public databaseStructures run(String query, String username,databaseStructures dbs) {
        this.databaseStructures = dbs;
        InternalQuery internalQuery = null;
        String action = query.replaceAll (" .*", "");
        action = action.toLowerCase ();
        boolean success = false;
        switch (action) {
            case "use":
                internalQuery = UseParser.instance ().parse (query);
                UseProcessor useProcessor = UseProcessor.instance(databaseStructures);
                this.databaseStructures = useProcessor.process (internalQuery, username, database, this.databaseStructures);
                this.database = useProcessor.getDatabase ();
                break;

            case "create":
                internalQuery = CreateParser.instance ().parse (query);
                if (((String) internalQuery.get ("type")).equalsIgnoreCase ("database")) {
                    CreateProcessor.instance ().processCreateQuery (internalQuery, query, username, database);
                } else {
                    if (checkDbSelected ()) {
                        CreateProcessor.instance ().processCreateQuery (internalQuery, query, username, database);
                    }
                }
                break;

            default:
                System.out.println ("invalid query!");
        }
        return this.databaseStructures;
    }

    private boolean checkDbSelected() {
        database = "db";
        if (database == null) {
            System.out.println ("Please select a Database.");
            return false;
        } else {
            return true;
        }
    }
}
