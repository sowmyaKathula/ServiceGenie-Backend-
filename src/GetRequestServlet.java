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

@WebServlet(name = "GetRequestServlet")
public class GetRequestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int userid=0;
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
            userid = Integer.parseInt(json.get("id").toString());
            System.out.println("userid: "+userid );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("Get request servlet called");
        PrintWriter pt = response.getWriter();
        response.setHeader("Access-Control-Allow-Origin","*");
        Connection conn = null;
        CustomDatabase db = new CustomDatabase();
        ResultSet rs = null,rs1 = null;
        PreparedStatement stmt = null;
        JSONArray array = new JSONArray();
        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement("select * from user_service where user_id=?");
            stmt.setInt(1,userid);
            rs = stmt.executeQuery();
            while(rs.next()){
                JSONObject obj1 = new JSONObject();
                obj1.put("user_id",rs.getInt(2));
                obj1.put("requested_cat_service",rs.getInt(3));
                stmt = conn.prepareStatement("SELECT name from subservices_cat where sub_service_cat_id=?");
                stmt.setInt(1,rs.getInt(3));
                rs1 = stmt.executeQuery();
                if(rs1.next())
                    obj1.put("requested_cat_service_name",rs1.getString(1));
                obj1.put("accepted",rs.getInt(5));
                obj1.put("accepted_userid",rs.getInt(6));
                array.add(obj1);
            }
            pt.write(array.toJSONString());
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
