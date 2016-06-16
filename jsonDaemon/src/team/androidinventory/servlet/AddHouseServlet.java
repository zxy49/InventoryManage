package team.androidinventory.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import team.androidinventory.dao.impl.WorkerDaoImpl;
import team.androidinventory.model.HouseVolume;

@WebServlet("/addhouse")
public class AddHouseServlet extends HttpServlet {
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
				ArrayList<HouseVolume> houseVolumeList=new ArrayList<HouseVolume>();
				
				JSONArray house=object.getJSONArray("house_list");
								
				String name=object.getString("house_name");
//				double length = object.getDouble("house_length");
//				double width = object.getDouble("house_width");
//				double height = object.getDouble("house_height");
				for(int i=0;i<house.length();i++){
	                JSONObject item=house.getJSONObject(i);
	                String type=item.getString("unit_type");
	                int volume=item.getInt("unit_volume");
	                HouseVolume houseVolume=new HouseVolume();
	                houseVolume.setCommodityType(type);
	                houseVolume.setMaxNum(volume);
	                houseVolumeList.add(houseVolume);
				}
				
				WorkerDaoImpl wo = new WorkerDaoImpl();
				
				String result = wo.addHouse(name,houseVolumeList);
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
