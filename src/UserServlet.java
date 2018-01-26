import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "UserServlet")
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = null;
        String phone = null;
        String password = null;
        Boolean isProvider = false;
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
            phone = (String) json.get("phone");
            password = (String) json.get("password");
            System.out.println(phone +" " +password);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("servlet called");
        PrintWriter pt = response.getWriter();


        Connection conn = null;
        CustomDatabase db = new CustomDatabase();
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement stmt = null;
        PreparedStatement s1 = null;
        PreparedStatement s2 = null;
        JSONArray array = new JSONArray();
        int flag = 0;
        try {
            conn = DBConnection.createConnection();
            stmt = conn.prepareStatement(db.fetchData(new Object[]{"password", "isProvider", "name","id"}, "user", new String[]{"phone"}, new String[]{phone}, new String("AND")));
            rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getString("password").equals(password)) {
                    JSONObject obj = new JSONObject();
                    obj.put("id",rs.getInt("id"));
                    obj.put("name", rs.getString("name"));
                    obj.put("phone", phone);
                    obj.put("status",1);
                    obj.put("message","Login Successful");
                    if (rs.getInt("isProvider") == 0) {
                        obj.put("isProvider",isProvider); //endUser

                    } else {
                        isProvider = true;
                        obj.put("isProvider",isProvider);;//Restaurant Owner
                    }

                    stmt = conn.prepareStatement("SELECT * FROM `service_cart` JOIN subservices_cat on service_cart.sub_service_cat_id= subservices_cat.sub_service_cat_id WHERE service_cart.user_id = ?");
                    stmt.setInt(1,rs.getInt("id"));
                    rs1 = stmt.executeQuery();
                    while (rs1.next()){
                        JSONObject cart = new JSONObject();
                        cart.put("cat_name",rs1.getString("name"));
                        cart.put("sub_service_id",rs1.getInt("sub_service_id"));
                        cart.put("sub_service_cat_id",rs1.getInt("sub_service_cat_id"));
                        cart.put("user_id",rs1.getInt("user_id"));
                        array.add(cart);
                    }

                    obj.put("usercart",array);
                    pt.print(obj.toJSONString());
                } else
                    pt.print("{\"status\": 0, \"message\": \"Login Failed...Try Again\"}"); //Login Failed
            } else
                pt.print("{\"status\": 0, \"message\": \"Not Registered\"}"); //Not Registered


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}
