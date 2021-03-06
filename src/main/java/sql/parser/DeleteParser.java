package sql.parser;

import sql.sql.InternalQuery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteParser implements IParser {
    static DeleteParser instance = null;

    public static DeleteParser instance(){
        if(instance == null){
            instance = new DeleteParser();
        }
        return instance;
    }

    @Override
    public InternalQuery parse(String query) {
        Pattern pattern = Pattern.compile("delete\\s(.*?)from\\s(.*?)where\\s(.*?)?;");
        Matcher matcher = pattern.matcher(query);
        boolean match = matcher.matches ();

        if(match == true) {
            String tableName = matcher.group (2);
            String condition = matcher.group (3);

            InternalQuery internalQuery = new InternalQuery ();
            internalQuery.set ("table",tableName);
            internalQuery.set ("condition",condition);
            return internalQuery;
        }
        System.out.println ("Invalid query !!");
        return null;
    }
}
