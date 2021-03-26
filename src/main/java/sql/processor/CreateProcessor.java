package sql.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import logging.events.CrashListener;
import logging.events.DatabaseListener;
import logging.events.QueryListener;
import sql.sql.InternalQuery;

import java.io.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;

public class CreateProcessor implements IProcessor {
    static final Logger logger = LogManager.getLogger(CreateProcessor.class.getName());
    static final CrashListener crashListener = new CrashListener();
    static final DatabaseListener databaseListener = new DatabaseListener();
    static final QueryListener queryListener = new QueryListener();
    private HashMap<String, String> primaryKey_Hashtable = new HashMap<String, String>();
    private HashMap<String, String> foreignKey_Hashtable = new HashMap<String, String>();
    private HashMap<String, HashMap<String,String>> datatable = new HashMap<String, HashMap<String,String>>(); // Create an ArrayList object

    String BASE_PATH = "src/main/java/dataFiles/";
    String DB_PATH = "src/main/java/dataFiles/databases.txt";
    static CreateProcessor instance = null;

    private boolean databaseExists = false;
    private String username = null;
    private String database = null;

    public static CreateProcessor instance(){
        if(instance == null){
            instance = new CreateProcessor();
        }
        return instance;
    }

    public boolean processCreateQuery(InternalQuery internalQuery, String query, String username, String database) {
        this.username = username;
        this.database = database;
        logger.info("Checking if database exists!");
        if(internalQuery.get("type").equals("database")){
            return createDB(internalQuery);
        }
        else {
            return createTable(internalQuery,query, username, database);
        }
    }

    private boolean createDB(InternalQuery internalQuery) {
        String name = (String) internalQuery.get("name");
        String path = BASE_PATH + name ;
        System.out.println(path);
        File file = new File(path);
        System.out.println(path);
        boolean bool = file.mkdir();
        if(bool){
            System.out.println("DB created successfully");
            logger.info("DB "+name+" created successfully!");
            databaseListener.recordEvent();
            //parseDBFile(name);
        }else{
            System.out.println("Sorry couldn’t create DB");
            crashListener.recordEvent();
        }
        return true;
    }

    private boolean createTable(InternalQuery internalQuery, String query, String username, String database) {
        HashMap<String, String> columns_list = new HashMap<String, String>(); // Create an ArrayList object

        query = query.replaceAll(";", "");
        query = query.replaceAll(",", " ");
        query = query.replaceAll("\\(", " ").replaceAll("\\)"," ");
        query = query.replaceAll("[^a-zA-Z ]", "");
        query = query.replaceAll("  ", " ");

        String[] sqlWords = query.split(" ");

        int primaryIndex = sqlWords.length-1;
        int foreignIndex = 0;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(query.toLowerCase().contains("primary key")) {
            for(int i = 0; i< sqlWords.length; i++) {
                if(sqlWords[i].equalsIgnoreCase("primary")) {
                    primaryIndex = i;
                    break;
                }
            }
            String[] primaryKeys = Arrays.copyOfRange(sqlWords, primaryIndex, primaryIndex+3);
            System.out.println(primaryKeys);
            primaryKey_Hashtable.put("type","primary");
        }
        if(query.toLowerCase().contains("foreign key")) {
            for(int i = 0; i< sqlWords.length; i++) {
                if(sqlWords[i].equalsIgnoreCase("foreign")) {
                    foreignIndex = i;
                    break;
                }
            }
            String[] foreignKeys = Arrays.copyOfRange(sqlWords, foreignIndex, sqlWords.length);
            foreignKey_Hashtable.put("type","foreign");
        }
        //sqlWords = Arrays.copyOfRange(sqlWords, 0, primaryIndex);

        for(int i = 3; i< sqlWords.length; i+=2) {
            columns_list.put(sqlWords[i], sqlWords[i+1]);
            System.out.println(columns_list);
        }
        logger.info("Adding indexes to table!");
        String tableName = (String) internalQuery.get("name");
        System.out.println("\n" +tableName);
        System.out.println("Column List" +columns_list);

        if(datatable!=null) {
            System.out.println("Here we go");
            datatable.put(tableName, columns_list);
        }
        String fileContent = tableName + "=" +datatable.get(tableName);
        try (FileWriter file = new FileWriter(BASE_PATH + database +"/"+tableName+".txt")) {
            file.write(fileContent);
            logger.info("Table "+tableName+" created successfully!");
            databaseListener.recordEvent();
        } catch (IOException e) {
            e.printStackTrace();
            crashListener.recordEvent();
        }

        return true;
    }

    @Override
    public boolean process(InternalQuery query, String username, String database) {
        return false;
    }
}