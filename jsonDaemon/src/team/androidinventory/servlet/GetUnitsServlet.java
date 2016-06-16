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
import team.androidinventory.model.VarietyEnum;


@WebServlet("/getunits")
public class GetUnitsServlet extends HttpServlet{
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
			ArrayList<VarietyEnum> units=daoImpl.getUnits();
			if(!units.isEmpty()&&units!=null){
				for(VarietyEnum varietyEnum:units){
					JSONObject item=new JSONObject();
					item.put("unit_id", varietyEnum.getVarietyID());
					item.put("unit_type", varietyEnum.getType());
					item.put("unit_volume", varietyEnum.getVolume());
					item.put("unit_typeName", varietyEnum.getTypeName());
					
					
					array.put(item);
				}
				object.put("unit_list", array);
				pw.write(object.toString());
			}else {
				pw.write("获取失败,no such record!");
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
