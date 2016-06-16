package team.androidinventory.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import team.androidinventory.dao.impl.WorkerDaoImpl;
import team.androidinventory.model.VarietyEnum;

@WebServlet("/addunit")
public class AddUnitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static BufferedReader reader;
	private static PrintWriter writer;
	private static StringBuffer buffer;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json");
		writer = resp.getWriter();
		reader = new BufferedReader(new InputStreamReader((ServletInputStream) req.getInputStream(), "utf-8"));
		StringBuffer buffer = new StringBuffer("");
		String temp;
		while ((temp = reader.readLine()) != null) {
			buffer.append(temp);
		}
		String content = buffer.toString();
		if (content != "") {
			JSONObject object;
			try {
				object = new JSONObject(content);
				String type=object.getString("unit_type");
				String volume=object.getString("unit_volume");
				String typeName=object.getString("unit_typeName");
				VarietyEnum item=new VarietyEnum();
				item.setType(type);
				item.setTypeName(typeName);
				item.setVolume(volume);
				
				WorkerDaoImpl wo = new WorkerDaoImpl();
				
				String result = wo.addUnit(item);
				writer.write(result);
			} catch (Exception e) {
				e.printStackTrace();
				writer.write("failed 0");
			}
		} else {
			writer.write("failed 1");
		}

		reader.close();

		writer.flush();
		writer.close();

		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

}
