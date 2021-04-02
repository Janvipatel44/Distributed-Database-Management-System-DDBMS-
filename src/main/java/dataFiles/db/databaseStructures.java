package dataFiles.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class databaseStructures {

    public HashMap <String,String> primaryKey_Hashtable = new HashMap<>();
    public HashMap <String,String> foreignKey_Hashtable = new HashMap<>();

    public String selectedDb = null;

    public HashMap<String,HashMap<String,HashMap<String,String>>> databasedata = new HashMap<>();
    public HashMap<String,HashMap<String ,String>> tableStructure = new HashMap<>();

    public databaseStructures populateDatabaseData(String DatabaseName){
    try {

        File structurefile = new File("src/main/java/dataFiles/db/"+DatabaseName+"Structure.txt");
//        System.out.println(structurefile.getPath());
        FileReader fr = new FileReader(structurefile);
        BufferedReader br = new BufferedReader(fr);
        String line ;
        while((line=br.readLine())!=null){
            String[] splitted_part = line.split("=");

            if(splitted_part[0].trim().equals("tablename")){
                String tablenametemp = splitted_part[1].trim();
                HashMap<String,String> tempcolumnHashmap = new HashMap<>();;
//                System.out.println(tablenametemp);
                String[] tablemetadata = splitted_part[2].replaceAll("[{}]"," ").trim().split(",");
                for (int j= 0; j<tablemetadata.length; j++){
                    String[] columnattributes = tablemetadata[j].trim().split(":");

                    tempcolumnHashmap.put(columnattributes[0].trim(),columnattributes[1].trim());
//                    System.out.println(columnattributes[0]+"->"+columnattributes[1]);
                }
                this.tableStructure.put(tablenametemp,tempcolumnHashmap);
//                System.out.println();
            }else if(splitted_part[0].trim().equals("databasename")){

            }
        }

        File structurefile2 = new File("src/main/java/dataFiles/db/"+DatabaseName+"Data.txt");
        FileReader fr2 = new FileReader(structurefile2);
        BufferedReader br2 = new BufferedReader(fr2);
        String line2 ;


        HashMap<String,String> rowdata;
        HashMap<String,HashMap<String,String>> allrows;


        while((line2=br2.readLine())!=null){
            allrows = new HashMap<>();
            String[] spliting_line = line2.split("=",2);
            String tempTablename = spliting_line[0].trim();
            String[] spliting_data = spliting_line[1].split("} ,");
            int rownum = 1;
            for(int i=0;i<spliting_data.length;i++){

                rowdata = new HashMap<String,String>();
                String[] splitting_rows = spliting_data[i].replaceAll("[{}]","").trim().split(",");

                String rowdetails = "row"+rownum;
                for(int j=0;j<splitting_rows.length;j++)
                {
                    String[] splitting_columns = splitting_rows[j].trim().split("=");
                    String column_name = splitting_columns[0].trim();
                    String column_value = splitting_columns[1].replaceAll("\"","").trim();
                    rowdata.put(column_name,column_value);
                }
                allrows.put(rowdetails,rowdata);
                rownum=rownum+1;
            }
            this.databasedata.put(tempTablename,allrows);
        }

    }catch (Exception e){
        e.printStackTrace();
//        System.out.println("UnknownDatabase");
    }
        System.out.println("Selected DB "+ this.selectedDb);
        System.out.println("this is my structure hash map:"+this.tableStructure);
        System.out.println((tableStructure.get("employee")).get("id"));
        System.out.println("This is my Data Hashmap"+this.databasedata);
        return this;
    }

    public void storePermanatly(String DatabaseName){



        try {
            File structurefile = new File("src/main/java/dataFiles/db/"+DatabaseName+"StructureTest.txt");
            File structurefile2 = new File("src/main/java/dataFiles/db/"+DatabaseName+"Data.txt");
            FileWriter fw = new FileWriter(structurefile, false);
            fw.close();
            FileWriter fr = new FileWriter(structurefile,true);

            String dbString = "databasename = "+DatabaseName+"\n \n";
            System.out.println("Datatatatatat"+dbString);
            fr.write(dbString);


            for(String key :  tableStructure.keySet()){
                String tableStruct = "tablename = ";
                String tablename = "";

                tablename = key+" = { ";
                String allColumns = "" ;
                for(String key2 : tableStructure.get(key).keySet()){
                    //System.out.println(key2);
                    String columnName = key2+" : "+tableStructure.get(key).get(key2)+", ";
                    allColumns = allColumns + columnName;
                }
                StringBuffer sb = new StringBuffer(allColumns);
                sb.deleteCharAt(sb.length()-2);
                String enddelimiter = "}\n";
                //System.out.println(sb);
                tablename = tablename + sb + enddelimiter ;
                //System.out.println(tablename);
                tableStruct = tableStruct + tablename;
                fr.write(tableStruct);
                //System.out.println(tableStruct);
            }
            fr.close();
            FileWriter fileWriter = new FileWriter(structurefile2,false);
            fileWriter.close();
            FileWriter fileWriter1 = new FileWriter(structurefile2,true);
            for(String key : databasedata.keySet()){
                String table = "";
                table = key+" = { ";
                String table_data = "";
                String all_data = "";
                for(String key2 : databasedata.get(key).keySet()){
                    String column = "";
                    String data = "";
                    String final_data = "";

                    for(String key3 : databasedata.get(key).get(key2).keySet()){
                        column = key3+" = \"";
                        data = column+databasedata.get(key).get(key2).get(key3)+"\" , ";
                        final_data = final_data+data;

                    }
                    //System.out.println(final_data);
                    StringBuffer sb = new StringBuffer(final_data);
                    sb.deleteCharAt(sb.length()-2);
                    String sb1 = sb+"";
                    sb1 = sb1.trim();
                all_data = all_data+sb1+" } , { ";
                }
                table_data = table+all_data;
                table_data = table_data.substring(0,table_data.length()-5);
                System.out.println(table_data);
                fileWriter1.write(table_data);
                fileWriter1.write("\n");
            }
            fileWriter1.close();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
