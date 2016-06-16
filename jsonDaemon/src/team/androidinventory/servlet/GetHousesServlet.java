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
import team.androidinventory.model.House;


@WebServlet("/gethouses")
public class GetHousesServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json");
		PrintWriter pw = resp.getWriter();
		BufferedReader br = new BufferedReader(new InputStreamReader(  
                (ServletInputStream) req.getInputStream(), "utf-8"));  
        StringBuffer sb = new StringBuffer("");
        
        
		
		try {
			JSONObject object=new JSONObject();
			JSONArray array=new JSONArray();
			WorkerDaoImpl daoImpl=new WorkerDaoImpl();
			ArrayList<House> houses=daoImpl.getHouseList();
			if(!houses.isEmpty()&&houses!=null){
				for(House house:houses){
					JSONObject item=new JSONObject();
					item.put("house_id", house.getHouseID());
					item.put("house_name", house.getName());
					item.put("house_state", house.getState());
					item.put("is_available", house.getIsAvailable());
					array.put(item);
				}
				object.put("house_list", array);
				pw.write(object.toString());
			}else {
				pw.write("failed,no such record!");
			}
			
		} catch (Exception e) {
			pw.write("failed");
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
