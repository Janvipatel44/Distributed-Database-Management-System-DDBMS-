package sql.sql;

import sql.parser.CreateParser;
import sql.processor.CreateProcessor;
import sql.parser.InsertParser;
import sql.processor.InsertProcessor;

import java.io.IOException;

public class QueryEngine {
    private String database = null;

    public void run(String query, String username) throws IOException {
        InternalQuery internalQuery = null;
        String action = query.replaceAll (" .*", "");
        action = action.toLowerCase ();
        boolean success = false;
        switch (action) {
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
            case "insert":
                if (checkDbSelected ()) {
                    internalQuery = InsertParser.instance ().parse (query);
                    InsertProcessor.instance ().process (internalQuery, username, database);
                }
                break;
            default:
                System.out.println ("invalid query!");
        }
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
