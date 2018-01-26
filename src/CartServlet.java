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

@WebServlet(name = "CartServlet")
public class CartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int cat_id = 0;
        int user_id = 0;
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
            cat_id =  Integer.parseInt(json.get("sub_service_cat_id").toString());
            user_id = Integer.parseInt(json.get("userid").toString());
            System.out.println(cat_id +"id " +user_id +"user");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PreparedStatement stmt = null;
        PrintWriter pt = response.getWriter();
        response.setHeader("Access-Control-Allow-Origin","*");
        Connection conn = DBConnection.createConnection();
        try {
            stmt = conn.prepareStatement("insert into service_cart(sub_service_cat_id,user_id) values(?,?)");
            stmt.setInt(1,cat_id);
            stmt.setInt(2,user_id);
            stmt.executeUpdate();
            pt.write("Done");

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
