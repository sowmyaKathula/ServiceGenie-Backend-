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

@WebServlet(name = "RequestServiceServlet")
public class RequestServiceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String cat_name=null;
        int service_id = 0,userid = 0,service_cat_id = 0;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str;

        JSONArray data = null;
        JSONObject json=null;

        while( (str = br.readLine()) != null ){
            sb.append(str);
        }
        System.out.println(sb.toString()+" data");

        PreparedStatement stmt = null;
        PrintWriter pt = response.getWriter();
        response.setHeader("Access-Control-Allow-Origin","*");
        Connection conn = DBConnection.createConnection();

        JSONParser parser = new JSONParser();
        try {
            data = (JSONArray) parser.parse(sb.toString());

            for(int i=0; i < data.size() ; i++){
                json = (JSONObject) data.get(i);

                cat_name = json.get("cat_name").toString();
                service_cat_id = Integer.parseInt(json.get("sub_service_cat_id").toString());
                userid = Integer.parseInt(json.get("userid").toString());
                service_id = Integer.parseInt(json.get("service_id").toString());
                System.out.println(i +"  ----->   "+userid +" " +cat_name +" " +service_id);

                stmt = conn.prepareStatement("insert into user_service(user_id,requested_cat_service,service_id) values(?,?,?)");
                stmt.setInt(1,userid);
                stmt.setInt(2,service_cat_id);
                stmt.setInt(3,service_id);
                stmt.executeUpdate();
                pt.write("Done");
            }

            stmt = conn.prepareStatement("delete from service_cart where user_id=?");
            stmt.setInt(1,userid);
            stmt.executeUpdate();
            pt.write("Delete Done");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
