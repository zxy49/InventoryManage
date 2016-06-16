package team.androidinventory.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

import jdk.nashorn.internal.scripts.JS;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.media.sound.JavaSoundAudioClip;

import team.androidinventory.dao.impl.WorkerDaoImpl;
import team.androidinventory.model.StoreItemList;

@WebServlet("/getstoreitems")
public class HouseQueryServlet extends HttpServlet{
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

		// 解析resp中的数据
		reader = new BufferedReader(
				new InputStreamReader((ServletInputStream) req.getInputStream(), "utf-8"));
		buffer = new StringBuffer("");
		String temp;
		while ((temp = reader.readLine()) != null) {
			buffer.append(temp);
		}
		String content = buffer.toString();
		System.out.println("************"+content);
		if (content != "") {
			JSONObject object;
			try {
				object = new JSONObject(content);
				int houseId=object.getInt("house_id");
				WorkerDaoImpl wo = new WorkerDaoImpl();
				
				ArrayList<StoreItemList> items=wo.houseQuery(houseId);
				
				if(!items.isEmpty()&&items!=null){
					JSONObject returnObject=new JSONObject();
					JSONArray array=new JSONArray();
					for(StoreItemList item:items){
						JSONObject mObject=new JSONObject();
						mObject.put("storeitem_id", item.getStoreItemID());
						mObject.put("house_id", item.getHouseID());
						mObject.put("commodity_id", item.getCommodityID());
						mObject.put("number", item.getNumber());
						mObject.put("provider_id", item.getProviderID());
						array.put(mObject);
					}
					returnObject.put("storeitem_list", array);
					System.out.println(returnObject.toString());
					writer.write(returnObject.toString());
				}else {
					writer.write("failed,no such record!");
				}
				
				
				
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
