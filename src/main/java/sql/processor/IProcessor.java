package sql.processor;

import sql.sql.InternalQuery;

import java.io.IOException;

public interface IProcessor {
    boolean process(InternalQuery query, String username, String database) throws IOException;
}
