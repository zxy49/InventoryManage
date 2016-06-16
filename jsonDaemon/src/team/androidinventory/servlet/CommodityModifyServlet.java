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
@WebServlet("/setCommodity")
public class CommodityModifyServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json");
		PrintWriter pw = resp.getWriter();

		// 解析resp中的数据
		BufferedReader br = new BufferedReader(
				new InputStreamReader((ServletInputStream) req.getInputStream(), "utf-8"));
		StringBuffer sb = new StringBuffer("");
		String temp;
		while ((temp = br.readLine()) != null) {
			sb.append(temp);
		}
		String jsonContent = sb.toString();
		if (jsonContent != "") {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonContent);
				WorkerDaoImpl wo = new WorkerDaoImpl();
//				int inputID = wo.getOutputID();
				int totalNumber = jsonObject.getInt("totalNumber");
				String commodityID = jsonObject.getString("commodityID");
				String type = jsonObject.getString("type");
				double price = jsonObject.getDouble("price");
				String name = jsonObject.getString("name");
				String result = wo.commodityModify(commodityID, name,type, price);
				pw.write(result);
			} catch (Exception e) {
				e.printStackTrace();
				pw.write("commodity modify failed");
			}
		} else {
			pw.write("warse failed");
		}

		br.close();

		pw.flush();
		pw.close();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

}
