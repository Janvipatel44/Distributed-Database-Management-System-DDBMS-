package erdgenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataFiles.db.databaseStructures;

public class ERDGenerator {
	String BASE_PATH = "src/main/java/dataFiles/db/";
	String ERD_PATH = "src/main/java/erdgenerator/";
	String childtable="";
	String parentTable="";
	String refColumn ="";

	public void generateERD(String username, String databaseName, databaseStructures dbs) {

		File db_structure = new File(BASE_PATH+databaseName+"Data.txt");
		File db_data = new File(BASE_PATH+databaseName+"Structure.txt");
		File db_keys = new File(BASE_PATH+databaseName+"Keys.txt");

		dbs.populateDatabaseData(databaseName);

		for(String tableName: dbs.tableStructure.keySet())
		{
			int count = 0;
			System.out.println(" ************ Entity name :" +tableName +"*******************");

			for(String attributesName: dbs.tableStructure.get(tableName).keySet())
			{
				System.out.println("Attribute name : " +attributesName);
				System.out.println("Datatype name : " +dbs.tableStructure.get(tableName).get(attributesName));
				System.out.println("\n");
			}
			if((dbs.primaryKey_Hashtable.keySet()).contains(tableName))
			{
				System.out.println("Primary Key : " +dbs.primaryKey_Hashtable.get(tableName));
			}
			for(String key: dbs.foreignKey_Hashtable.keySet()){
				String foreign_key = dbs.foreignKey_Hashtable.get(key);
				String tables[] = foreign_key.split(" ()");
				if(tables[2].contains(tableName)) {
					String foreignkey_table = tables[5];
					System.out.println("foreignkeytable:" + foreignkey_table);
					count++;
				}
			}
			if(count>1) {
				System.out.println("Cardinality for table" +tableName + "1:Many");
			}
			if(count==1) {
				System.out.println("Cardinality for table" +tableName + "1:1");
			}
		}
		System.out.println("ERD diagram generation:" +dbs.primaryKey_Hashtable);
	}
	
	private void writeERDFile(String contentWritter, String databaseName) {

        try (FileWriter file = new FileWriter(ERD_PATH+databaseName+"_ERD.json")) {
            file.write(contentWritter);
            file.flush();
            System.out.println("ERD generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to Generate ERD!");
        }
    }
}
