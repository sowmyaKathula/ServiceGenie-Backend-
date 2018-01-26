import org.json.simple.*;
import org.json.simple.parser.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;
        CustomDatabase db = new CustomDatabase();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        PrintWriter out= response.getWriter();

        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str;
        JSONObject json=null;
        int isProvider= 0;
        String name="",phone="",password="";

        while( (str = br.readLine()) != null ){
            sb.append(str);
        }
        System.out.println(sb.toString()+" data");
       // JSONObject jObj = new JSONObject(sb.toString());
        JSONParser parser = new JSONParser();
        try {
             json = (JSONObject) parser.parse(sb.toString());
            name = (String) json.get("name");
            phone = String.valueOf(json.get("phone"));
            password = (String) json.get("password");
            System.out.println(name + " " + phone +" " +password);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("**********************");
        boolean isProviderb = (boolean) json.get("isProvider");
        if(isProviderb)
            isProvider=1;
        int count = 1;

        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement(db.fetchData(new String[]{"count(*)"}, "user",new String[]{"phone"},new String[]{phone},new String("AND")));
            rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            System.out.println("---------->"+count);
            if(count==0){
                stmt=conn.prepareStatement(db.insertData("user (name, phone, password, isProvider)",new Object[]{name,phone,password,isProvider}));
                stmt.executeUpdate();
                out.print("{\"status\": 1, \"message\": \"Registration Successful\"}");
            }
            else{
                out.print("{\"status\": 0, \"message\": \"User already exists... Try Login\" }");
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
