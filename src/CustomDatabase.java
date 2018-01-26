import java.sql.Blob;

public class CustomDatabase {

    // Connection con;
    public CustomDatabase(){
        //  con =DBConnection.createConnection();
    }

    // select * from table
    public String fetchData(Object[] cols,String table){
        String sql="select ";
        for (int i=0;i<cols.length-1;i++){
            sql+=cols[i]+",";
        }
        sql+=cols[cols.length-1];
        sql+=" from " + table;
        return sql;
    }

    //select * from table where condition= value AND condition = value
    public String fetchData(Object[] cols, String table ,String[] key,Object[] value,String operator){
        String sql=fetchData(cols,table);
        sql+=" where ";
        for (int i=0;i<key.length-1;i++){
            if(value[i] instanceof Integer)
                sql+=" "+key[i]+" = "+value[i]+" " +operator;
            else
                sql+=" "+key[i]+" = '"+value[i]+"' " +operator;
        }
        if(value[key.length-1] instanceof Integer)
            sql+=" "+key[key.length-1]+" = "+value[key.length-1];
        else
            sql+=" "+key[key.length-1]+" = '"+value[key.length-1]+"'";
        System.out.println(sql);
        return sql;
    }

    // Insert into table
    public String insertData(String table,Object[] value){
        String sql="insert into "+table+" values(";
        for(int i=0;i<value.length-1;i++){

            if(value[i] instanceof Integer)
                sql+=value[i]+",";
            else if(value[i] instanceof Blob)
                sql+=value[i]+",";
            else
                sql+="'"+value[i]+"',";
        }

        if(value[value.length-1] instanceof Integer)
            sql+=value[value.length-1]+")";
        else
            sql+="'"+value[value.length-1]+"')";
        System.out.println(sql);
        return sql;
    }


}
