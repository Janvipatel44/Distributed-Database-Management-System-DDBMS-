package DataDictionary;

import dataFiles.db.databaseStructures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class DataDictionary {
    databaseStructures dbs = new databaseStructures();

    public void generateDataDictionary(String database){
        dbs.populateDatabaseData(database);
        System.out.println(dbs.tableStructure);
       try {
           File file = new File("src/main/java/Datadictionary/" + database + ".txt");
           FileWriter fr = new FileWriter(file,false);
           fr.close();

           FileWriter fw = new FileWriter(file,true);
           String databasename = "Database = "+database+"\n\n";
           fw.write(databasename);

           for(String key : dbs.tableStructure.keySet())
           {
              fw.write(key+" {\n");
              for (String key2 : dbs.tableStructure.get(key).keySet()){
                  fw.write("\t"+ key2 + " : " + dbs.tableStructure.get(key).get(key2)+"\n");

              }
              fw.write("}\n");
           }
           fw.close();

       } catch (IOException e) {
           e.printStackTrace();
       }

    }

}
