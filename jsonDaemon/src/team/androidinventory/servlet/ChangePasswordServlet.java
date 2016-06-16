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

import org.json.JSONObject;

import team.androidinventory.dao.impl.WorkerDaoImpl;
import team.androidinventory.model.Worker;


@WebServlet("/changepassword")
public class ChangePasswordServlet extends HttpServlet{
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
    			int workerID = jsonObject.getInt("worker_id");
    			String oldpassword = jsonObject.getString("old_password");
    			String newpassword = jsonObject.getString("new_password");
    			
    			WorkerDaoImpl wo = new WorkerDaoImpl();
    			boolean ischanged=wo.changePassword(workerID, oldpassword, newpassword);
    			if(ischanged){
    				Worker w=wo.login(workerID, newpassword);
    				if(w != null&&(w.getWorkerID()!=0)
//        					&&(w.getWorkerID()!=0)&&(w.getTypenumber()!=0)
        					){
        				JSONObject userinfo = new JSONObject();
        				userinfo.put("workerID", w.getWorkerID());
        				userinfo.put("typeName", w.getTypeNumber());
        				userinfo.put("name",w.getName() );
        				userinfo.put("address", w.getAddress());
        				userinfo.put("password", w.getPassword());
        				userinfo.put("position", w.getPosition());
        				userinfo.put("tel", w.getTel());
//        				userinfo.put("", "online");
        				System.out.println("=============="+userinfo);
        				pw.write(userinfo.toString());
        			}else{
        				pw.write("failed1");
        			}
    			}else {
    				pw.write("failed2");
				}
    			
    			
    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			pw.write("failed3");
    			e.printStackTrace();
    		}
    		}else{
    			pw.write("failed4");
    		}
        }else{
        	pw.write("failed5");
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
