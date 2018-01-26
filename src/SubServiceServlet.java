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
import java.sql.SQLException;

@WebServlet(name = "SubServiceServlet")
public class SubServiceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = 1;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str;
        JSONObject json=null;

        while( (str = br.readLine()) != null ){
            sb.append(str);
        }
        System.out.println(sb.toString()+" data");
        // JSONObject jObj = new JSONObject(sb.toString());
        JSONParser parser = new JSONParser();
        try {
            json = (JSONObject) parser.parse(sb.toString());
            id =  Integer.parseInt(json.get("id").toString());
            System.out.println(id +"id " );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PreparedStatement stmt = null;
        ResultSet rs, rs1;
        JSONArray subcat;
        PrintWriter pt = response.getWriter();
        response.setHeader("Access-Control-Allow-Origin","*");
        JSONArray array= new JSONArray();
        Connection conn = DBConnection.createConnection();
        try {
            stmt = conn.prepareStatement("select * from subservices where Service = ?");
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("sub_id",rs.getInt(1));
                obj.put("sub_service_name",rs.getString(2));

                stmt = conn.prepareStatement("SELECT * from subservices_cat where subservice = ?");
                stmt.setInt(1,rs.getInt(1));
                rs1 = stmt.executeQuery();
                subcat = new JSONArray();
                while (rs1.next()) {
                    JSONObject obj1 = new JSONObject();
                    obj1.put("sub_service_cat_id",rs1.getInt(1));
                    obj1.put("cat_name",rs1.getString(2));
                    subcat.add(obj1);
                }

                obj.put("sub_service_cat",subcat); //adding an array of categories for subservices
                array.add(obj);
            }
            System.out.println(array.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        pt.write(array.toJSONString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
