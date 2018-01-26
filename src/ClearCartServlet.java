import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

@WebServlet(name = "ClearCartServlet")
public class ClearCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int userid=0,itemid=0;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str;
        JSONObject json=null;

        while( (str = br.readLine()) != null ){
            sb.append(str);
        }
        System.out.println(sb.toString()+" data");
        JSONParser parser = new JSONParser();
        try {
            json = (JSONObject) parser.parse(sb.toString());
            userid = Integer.parseInt(json.get("userID").toString());
            itemid = Integer.parseInt(json.get("itemID").toString());
            System.out.println(userid +" " +itemid);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("servlet called");
        PrintWriter pt = response.getWriter();


        Connection conn = null;
        CustomDatabase db = new CustomDatabase();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        JSONArray array = new JSONArray();
        int flag = 0;
        try {
            conn = DBConnection.createConnection();
            if(itemid!=0){
                stmt = conn.prepareStatement("delete from service_cart where user_id=? AND sub_service_cat_id=?");
                stmt.setInt(2,itemid);
            }
            else
                stmt = conn.prepareStatement("delete from service_cart where user_id=?");
            stmt.setInt(1,userid);
            stmt.executeUpdate();
            pt.write("Done");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
