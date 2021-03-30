package sql.processor;

import dataFiles.db.databaseStructures;
import sql.sql.InternalQuery;

public interface IProcessor {
    databaseStructures process(InternalQuery query, String username, String database,databaseStructures dbs);
}
