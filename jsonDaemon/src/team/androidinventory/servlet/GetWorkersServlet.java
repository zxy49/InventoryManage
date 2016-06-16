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
import team.androidinventory.model.Worker;

@WebServlet("/getworkers")
public class GetWorkersServlet extends HttpServlet{
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
			ArrayList<Worker> workers=daoImpl.getWorkerList();
			if(!workers.isEmpty()&&workers!=null){
				for(Worker worker:workers){
					JSONObject item=new JSONObject();
					item.put("worker_id", worker.getWorkerID());
					item.put("type_number", worker.getTypeNumber());
					item.put("worker_name", worker.getName());
					item.put("worker_address", worker.getAddress());
					item.put("worker_password", worker.getPassword());
					item.put("worker_position", worker.getPosition());
					item.put("worker_tel", worker.getTel());
					array.put(item);
				}
				object.put("worker_list", array);
				pw.write(object.toString());
			}else {
				pw.write("failed,no such record!s");
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
