package sql.processor;

import com.google.gson.internal.bind.SqlDateTypeAdapter;
import dataFiles.db.databaseStructures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import logging.events.CrashListener;
import logging.events.DatabaseListener;
import logging.events.QueryListener;
import sql.sql.InternalQuery;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CreateProcessor implements IProcessor {
    static final Logger logger = LogManager.getLogger(CreateProcessor.class.getName());
    static final CrashListener crashListener = new CrashListener();
    static final DatabaseListener databaseListener = new DatabaseListener();
    static final QueryListener queryListener = new QueryListener();
    private HashMap<String, String> primaryKey_Hashtable = new HashMap<String, String>();
    private HashMap<String, String> foreignKey_Hashtable = new HashMap<String, String>();

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

    private databaseStructures createDB(InternalQuery internalQuery, String query, String username, String database, databaseStructures dbs)  {
        String name = (String) internalQuery.get("name");
        System.out.println(name);
        if(!dbs.database_list.contains(name)) {
            dbs.database_list.add(name);
            System.out.println("DB created successfully");
            try{
                File structurefile = new File("src/main/java/dataFiles/db/"+name+"Data.txt");
                File structurefile2 = new File("src/main/java/dataFiles/db/"+name+"Structure.txt");
                structurefile.createNewFile();
                structurefile2.createNewFile();
            }
            catch(Exception e){
                e.printStackTrace();
            }

            logger.info("DB "+name+" created successfully!");
            databaseListener.recordEvent();
        }
        else{
            System.out.println("Sorry couldn’t create DB");
            crashListener.recordEvent();
        }
        return dbs;
    }

    private databaseStructures createTable(InternalQuery internalQuery, String query, String username, String database, databaseStructures dbs)
    {
        HashMap<String,String> datatable = new HashMap<String,String>(); // Create an ArrayList object
        //System.out.println(internalQuery.get("name"));
        String name = (String) internalQuery.get("name");
        System.out.println(name);

        String location = (String) internalQuery.get("location");
        System.out.println();
        if(location.equals("local")){
            dbs.inlocal.add(name);
        }

        else if(location.contains("remote")){
            dbs.inremote.add(name);
        }
        System.out.println(dbs.inlocal);

        System.out.println(internalQuery.get("action"));
        System.out.println(internalQuery.get("type"));
        System.out.println(internalQuery.get("name"));
        query = query.replaceAll(";", "");
        query = query.replaceAll(",", " ");
        query = query.replaceAll("\\(", " ").replaceAll("\\)"," ");
        query = query.replaceAll("[^a-zA-Z ]", "");
        query = query.replaceAll("  ", " ");

        String[] sqlWords = query.split(" ");
        System.out.println("Column List" +sqlWords);

        int primaryIndex = 1;
        int foreignIndex = 1;
        int foreignkeylocation = 0;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(query.toLowerCase().contains("primary key")) {
            for(int i = 0; i< sqlWords.length; i++) {
                if(sqlWords[i].equalsIgnoreCase("primary")) {
                    dbs.primaryKey_Hashtable.put(name, sqlWords[i-2]);
                    primaryIndex++;
                    i++;
                }
            }
            List<String> list = new ArrayList<String>(Arrays.asList(sqlWords));
            list.remove("primary");
            list.remove("key");
            sqlWords = list.toArray(new String[0]);
        }

        System.out.println("Query" +query);
        if(query.toLowerCase().contains("foreign key")) {
            for(int i = 0; i< sqlWords.length; i++) {
                if(sqlWords[i].equalsIgnoreCase("foreign"))
                {
                    if(foreignkeylocation==0)
                        foreignkeylocation = i;
                    System.out.println(sqlWords[i+5]);

                    if(dbs.tableStructure.keySet().contains(sqlWords[i+4]))
                    {
                        String value = name + "(" + sqlWords[i+2] + ")" +" Reference To " + sqlWords[i+4] + "(" +sqlWords[i+5] + ")";
                        dbs.foreignKey_Hashtable.put("foreign key " + foreignIndex, value);
                    }
                    else {
                        System.out.println("Can't do it");
                    }
                    foreignIndex++;
                    i=i+7;
                }
            }
            String[] foreignKeys = Arrays.copyOfRange(sqlWords, 0, foreignkeylocation);
            sqlWords = foreignKeys;
        }

        System.out.println("foreign key:" +dbs.foreignKey_Hashtable);
        for(int i = 4; i< sqlWords.length; i+=2) {
            datatable.put(sqlWords[i], sqlWords[i+1]);
        }
        System.out.println("\n data table" +datatable);

        logger.info("Adding indexes to table!");
        String tableName = (String) internalQuery.get("name");
        System.out.println("\n" +tableName);

        if(datatable!=null) {
            dbs.tableStructure.put(tableName, datatable);
        }
        System.out.println("Column List" +dbs.tableStructure);
        System.out.println("Primary Key" +dbs.primaryKey_Hashtable);
        System.out.println("Foreign key" +dbs.foreignKey_Hashtable);

    /*    String fileContent = tableName + "=" +datatable.get(tableName);

        if(!primaryKey_Hashtable.isEmpty())
        {
            fileContent += "\n";
            for (int i=0;i<primaryKey_Hashtable.size();i++)    {
                String key = "primary key " + (i + 1);
                System.out.println(key);
                fileContent += key + "=" +primaryKey_Hashtable.get(key);
            }
        }

        if(!foreignKey_Hashtable.isEmpty())
        {
            fileContent += "\n";
            for (int i=0;i<foreignKey_Hashtable.size();i++)    {
                String key = "foreign key " + (i + 1);
                System.out.println(key);
                fileContent += key + "=" +foreignKey_Hashtable.get(key);
            }
        }
        try (FileWriter file = new FileWriter(BASE_PATH + database +"/"+tableName+".txt")) {
            file.write(fileContent);
            logger.info("Table "+tableName+" created successfully!");
            databaseListener.recordEvent();
        } catch (IOException e) {
            e.printStackTrace();
            crashListener.recordEvent();
        }*/
        return dbs;
    }

    public databaseStructures process(InternalQuery internalQuery, String query, String username, String database, databaseStructures dbs) {

        this.username = username;
        this.database = database;
        logger.info("Checking if database exists!");
        if(internalQuery.get("type").equals("database")){
            return createDB(internalQuery, query, username, database, dbs);
        }
        else {
            return createTable(internalQuery,query, username, database, dbs);
        }
    }
}