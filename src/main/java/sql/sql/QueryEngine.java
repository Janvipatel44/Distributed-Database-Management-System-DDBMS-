package sql.sql;

import sql.parser.CreateParser;
import sql.processor.CreateProcessor;

public class QueryEngine {
    private String database = null;

    public void run(String query, String username) {
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
            default:
                System.out.println ("invalid query!");
        }
    }

    private boolean checkDbSelected() {
        if (database == null) {
            System.out.println ("Please select a Database.");
            return false;
        } else {
            return true;
        }
    }
}
