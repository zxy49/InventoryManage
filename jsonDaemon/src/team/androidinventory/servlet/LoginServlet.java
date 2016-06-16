package team.androidinventory.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import team.androidinventory.dao.impl.DBUtil;
import team.androidinventory.dao.impl.WorkerDaoImpl;
import team.androidinventory.model.Worker;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
	    resp.setContentType("text/json");	    
	    PrintWriter pw = resp.getWriter();
		//解析resp中的数据
	    BufferedReader br = new BufferedReader(new InputStreamReader(  
                (ServletInputStream) req.getInputStream(), "utf-8"));  
        StringBuffer sb = new StringBuffer("");  
        String temp;
        if((temp = br.readLine()) != null) {  
            sb.append(temp);  
            String jsonContent = sb.toString();
    		if (!jsonContent.equals("")) {
    		JSONObject jsonObject;
//    		pw.write(jsonContent+"\n");
    		try {
    			jsonObject = new JSONObject(jsonContent);
    			int workerID = jsonObject.getInt("workerID");
    			String password = jsonObject.getString("password");
    			WorkerDaoImpl wo = new WorkerDaoImpl();
    			Worker w = wo.login(workerID, password);
    			
    			if(w != null&&(w.getWorkerID()!=0)
//    					&&(w.getWorkerID()!=0)&&(w.getTypenumber()!=0)
    					){
    				JSONObject userinfo = new JSONObject();
    				userinfo.put("workerID", w.getWorkerID());
    				userinfo.put("typeName", w.getTypeNumber());
    				userinfo.put("name",w.getName() );
    				userinfo.put("address", w.getAddress());
    				userinfo.put("password", w.getPassword());
    				userinfo.put("position", w.getPosition());
    				userinfo.put("tel", w.getTel());
//    				userinfo.put("", "online");
    				System.out.println("=============="+userinfo);
    				pw.write(userinfo.toString());
    			}else{
    				pw.write("failed1");
    			}
    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			pw.write("failed2");
    			e.printStackTrace();
    		}
    		}else{
    			pw.write("failed3");
    		}
        }else{
        	pw.write("failed4");
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
