import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "ListServicesServlet")
public class ListServicesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PreparedStatement stmt = null;
        ResultSet rs;
        PrintWriter pt = response.getWriter();
        response.setHeader("Access-Control-Allow-Origin","*");
        JSONArray array= new JSONArray();
        Connection conn = DBConnection.createConnection();
        try {
            stmt = conn.prepareStatement("select * from Service");
            rs = stmt.executeQuery();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id",rs.getInt(1));
                obj.put("service_name",rs.getString(2));
                obj.put("service_img",rs.getString(3));
                obj.put("service_dec",rs.getString(4));
                array.add(obj);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        pt.write(array.toJSONString());
    }
}
