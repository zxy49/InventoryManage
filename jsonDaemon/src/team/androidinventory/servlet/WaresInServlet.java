package team.androidinventory.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import team.androidinventory.dao.InputDao;
import team.androidinventory.dao.impl.HouseVolumeDaoImpl;
import team.androidinventory.dao.impl.InputDaoImpl;
import team.androidinventory.dao.impl.Utils;
import team.androidinventory.dao.impl.WorkerDaoImpl;
import team.androidinventory.model.HouseVolume;

@WebServlet("/waresIn")
public class WaresInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		String storeItemLists = sb.toString();
		InputDao idil = new InputDaoImpl();
		String result = idil.warseIn(storeItemLists);
//		if (jsonContent != "") {
//			JSONObject jsonObject;
//			try {
//				jsonObject = new JSONObject(jsonContent);
//				WorkerDaoImpl wo = new WorkerDaoImpl();
//				int inputID = wo.getInputID();
//				int workerID = jsonObject.getInt("workerID");
//				JSONArray jsonArray = jsonObject.getJSONArray("stockitems");
//				JSONArray stockarray = jsonArray;
//				String commodityExist = "";
//				boolean ce = false;
//				String providerExist = "";
//				boolean pe = false;
//				String houseExist="";
//				boolean he = false;
//				boolean te =false;
//				boolean alhvbl = false;//仓库-类型对是否存在
//				Utils utils = new Utils();
//				ArrayList<Integer> houseIDs = new ArrayList<Integer>();
//				for (int i = 0; i<stockarray.length();i++) {
//					JSONObject stockitem = stockarray.getJSONObject(i);
//					String commodityID = stockitem.getString("commodityID");
//					commodityExist = utils.isCommodityExist(commodityID);
//					if(commodityExist.contains("不存在")){
//						ce=true;
//						pw.write(commodityExist+"\n");
//					}
//					int houseID = stockitem.getInt("houseID");
//					
//					houseExist = utils.isHouseExist(houseID);
//					
//					if(houseExist .contains("不存在")){
//						he=true;
//						pw.write(houseExist+"\n");
//					}else
//						houseIDs.add(houseID);
//					int providerID = stockitem.getInt("providerID");
//					providerExist = utils.isProviderExist(providerID);
//					if(providerExist .contains("不存在")){
//						pe=true;
//						pw.write(providerExist+"\n");
//					}			
////					Date time = new java.sql.Date(System.currentTimeMillis());
//					int time = stockitem.getInt("time");
//					if(time<=0){
//						te=true;
//						pw.write(commodityID+"存储期限错误");
//					}
//				}//存在性判断
//				HouseVolumeDaoImpl hvdi = new HouseVolumeDaoImpl();				
//				ArrayList<HouseVolume> alhv = new ArrayList<HouseVolume>();
//				houseIDs = utils.RemoveSame(houseIDs);
//				if(houseIDs.size()>0&&he==false){
//					Set set = new HashSet(alhv);
//					for(int i=0;i<houseIDs.size();i++){
//						alhv =hvdi.getHouseVolume(houseIDs.get(i));//可优化为只提供当前要求的仓库-类型对
//						if(alhv.size()>0){
//						    set.addAll(new HashSet(alhv));
//						}
//						else{
//							alhvbl = true;
//							pw.write(houseIDs.get(i)+"不存在可存储空间");
//						}
//					}
//					alhv = new ArrayList<HouseVolume>(set);
//				}
//				if(alhv.size()<=0){
//					alhvbl = true;					
//				}
//				
//				
//				//下面开始判断能否入库
//				boolean numDiff = false;
//				if(ce==false&&pe==false&&he==false&&te==false&&alhvbl==false){
//					for(int i=0;i<stockarray.length();i++){
//						if(numDiff){
//							break;
//						}
//						JSONObject stockitem = stockarray.getJSONObject(i);
//						int houseID = stockitem.getInt("houseID");
//						String commodityID = stockitem.getString("commodityID");
//						int totalNumber = stockitem.getInt("totalNumber");
//						String commodityType = hvdi.getCommodityType(commodityID);
//						for(int j=0;j<alhv.size();j++){
//							HouseVolume hv = alhv.get(j);
//							if(commodityType.equals(hv.getCommodityType())&&houseID==hv.getHouseID()){
//								int maxNum = hv.getMaxNum();
//								int currentUsefulNum = hv.getCurrentUsefulNum();
//								int diff = currentUsefulNum - totalNumber;
//								if(diff<0){
//									pw.write("仓库"+houseID+"的"+commodityType+"无法容纳"+commodityID);
//									numDiff=true;
//									break;
//								}else{
//									alhv.get(j).setCurrentUsefulNum(diff);
//								}
//								break;
//							}
//						}
//					}
//					for(int i=0;i<alhv.size();i++){
//						if(alhv.get(i).getCurrentUsefulNum()<0){
//							numDiff=true;
//							break;
//						}
//					}
//					//可入库，更新inputlist、stocklist、storeitem（此处应优化成单独更新）  + housevolume
//					if(!numDiff){
//						String result = wo.waresIn(inputID, workerID, jsonArray);
//						for(int i=0;i<alhv.size();i++){
//							hvdi.updateHouseVolume(alhv.get(i));
//						}
//						pw.write(result);
//					}
//				}
//				
//				
//				
//				
//				
//				
//				
//				
//				
//				
////				
////				if(ce==false&&pe==false&&he==false&&te==false){
////					
////					boolean volumeAvailable = false;
////					for(int i=0;i<houseIDs.size();i++){
////						double currentVolume = 0.0;
////						double availableVolume =utils.currentHouseAvailableVolume(houseIDs.get(i));
////						for(int j=0;j<stockarray.length();j++){
////							JSONObject stockitem = stockarray.getJSONObject(i);
////							int houseID = stockitem.getInt("houseID");
////							if(houseID==houseIDs.get(i)){
////								String commodityID = stockitem.getString("commodityID");
////								int totalNumber = stockitem.getInt("totalNumber");
////								currentVolume += utils.getCommodityVolume(commodityID)*totalNumber;
////							}							
////						}
////						if(currentVolume>availableVolume){
////							volumeAvailable = true;
////							pw.write("仓库"+houseIDs.get(i)+"空间不足;");
////						}
////					}
////					if(!volumeAvailable){
////						System.out.println("1111");
////						String result = wo.waresIn(inputID, workerID, jsonArray);
////						pw.write(result);
////					}				
////				}				
//			} catch (Exception e) {
//				e.printStackTrace();
//				pw.write("入库失败");
//			}
//		} else {
//			pw.write("入库失败");
//		}

		br.close();
		pw.write(result);
		pw.flush();
		pw.close();

		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

}
