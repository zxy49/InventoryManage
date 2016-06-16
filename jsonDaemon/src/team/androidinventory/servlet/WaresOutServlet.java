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

import team.androidinventory.dao.impl.HouseVolumeDaoImpl;
import team.androidinventory.dao.impl.StoreItemDaoImpl;
import team.androidinventory.dao.impl.Utils;
import team.androidinventory.dao.impl.WorkerDaoImpl;
import team.androidinventory.model.HouseVolume;
import team.androidinventory.model.StoreItemList;

@WebServlet("/waresOut")
public class WaresOutServlet extends HttpServlet{
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
				JSONArray jsonArray = jsonObject.getJSONArray("stockitems");
				JSONArray sellArray = jsonArray;
				String commodityExist = "";
				boolean ce = false;
				String providerExist = "";
				boolean pe = false;
				String houseExist="";
				boolean he = false;
				String customerExist = "";
				boolean CE = false;
				boolean te =false;
				boolean alhvbl = false;
				Utils utils = new Utils();
				ArrayList<Integer> houseIDs = new ArrayList<Integer>();
				for (int i = 0; i < sellArray.length(); i++) {
					JSONObject stockitem = sellArray.getJSONObject(i);
					String commodityID = stockitem.getString("commodityID");
					commodityExist = utils.isCommodityExist(commodityID);
					if(commodityExist.contains("不存在")){
						ce=true;
						pw.write(commodityExist+"\n");
					}
					int houseID = stockitem.getInt("houseID");
					houseExist = utils.isHouseExist(houseID);
					
					if(houseExist .contains("不存在")){
						he=true;
						pw.write(houseExist+"\n");
					}else
						houseIDs.add(houseID);
					int providerID = stockitem.getInt("providerID");
					providerExist = utils.isProviderExist(providerID);
					if(providerExist .contains("不存在")){
						pe=true;
						pw.write(providerExist+"\n");
					}			
					int customerID = stockitem.getInt("customerID");
					customerExist = utils.isCustomerExist(customerID);
					if(customerExist .contains("不存在")){
						CE=true;
						pw.write(customerExist+"\n");
					}	
				}//存在性判断
				HouseVolumeDaoImpl hvdi = new HouseVolumeDaoImpl();				
				ArrayList<HouseVolume> alhv = new ArrayList<HouseVolume>();
				ArrayList<StoreItemList> alsil = new ArrayList<StoreItemList>();
				houseIDs = utils.RemoveSame(houseIDs);
				if(houseIDs.size()>0&&he==false){
					Set set = new HashSet(alhv);
					for(int i=0;i<houseIDs.size();i++){
						alhv =hvdi.getHouseVolume(houseIDs.get(i));//可优化为只提供当前要求的仓库-类型对
						if(alhv.size()>0&&alhv.get(i).getCurrentUsefulNum()<alhv.get(i).getMaxNum()){
						    set.addAll(new HashSet(alhv));
						}
						else{
							alhvbl = true;
							pw.write(houseIDs.get(i)+"不存在可存储空间");
						}
					}
					alhv = new ArrayList<HouseVolume>(set);
				}
				if(alhv.size()<=0){
					alhvbl = true;					
				}
				//判断能否出库
//				boolean notOutput =false;
				boolean numDiff = false;
				if(ce==false&&pe==false&&he==false&&CE==false&&alhvbl==false){
					for(int i=0;i<sellArray.length();i++){
						JSONObject stockitem = sellArray.getJSONObject(i);
						String commodityID = stockitem.getString("commodityID");
						int providerID = stockitem.getInt("providerID");
						int houseID = stockitem.getInt("houseID");
						int totalNumber = stockitem.getInt("totalNumber");
						//暂时不考虑容量判断，只考虑出库库存量判断
						boolean isSellItemRepeat = false;
						for(int j =0;j<alsil.size();j++){
							if(commodityID.equals(alsil.get(j).getCommodityID())&&providerID==alsil.get(j).getProviderID()
									&&houseID==alsil.get(j).getHouseID()){
								isSellItemRepeat = true;
								break;
							}
						}
						if(!isSellItemRepeat){
							StoreItemDaoImpl sidi = new StoreItemDaoImpl();
							StoreItemList si = new StoreItemList();
							si.setCommodityID(commodityID);
							si.setHouseID(houseID);
							si.setProviderID(providerID);
							si = sidi.getStoreItemList(si);
							if(si.getNumber()>0){
								alsil.add(sidi.getStoreItemList(si));
							}else{
								System.out.println("错误所在位置：StoreItemDaoImpl.getStoreItemList(StoreItemList)");
							}		
						}
					}
					
					
					//库存项是否充足的判断
					for(int i=0;i<sellArray.length();i++){
						if(numDiff){
							break;
						}
						JSONObject stockitem = sellArray.getJSONObject(i);
						String commodityID = stockitem.getString("commodityID");
						int providerID = stockitem.getInt("providerID");
						int houseID = stockitem.getInt("houseID");
						int totalNumber = stockitem.getInt("totalNumber");
						for(int j=0;j<alsil.size();j++){
							StoreItemList si = alsil.get(j);
							if(commodityID.equals(si.getCommodityID())&&houseID==si.getHouseID()
									&&providerID==si.getProviderID()){
								int number = si.getNumber();
								int diff = number - totalNumber;
								if(diff>0){
									alsil.get(j).setNumber(diff);
								}else{
									pw.write("仓库"+houseID+"由"+providerID+"供应的"+commodityID+"数量不足以出库");
									System.out.println("仓库"+houseID+"由"+providerID+"供应的"+commodityID+"数量不足以出库");
									numDiff=true;
									break;
								}
							}
						}
						String commodityType = hvdi.getCommodityType(commodityID);
						for(int j=0;j<alhv.size();j++){
							HouseVolume hv = alhv.get(j);
							if(commodityType.equals(hv.getCommodityType())&&houseID==hv.getHouseID()){
								int maxNum = hv.getMaxNum();
								int currentUsefulNum = hv.getCurrentUsefulNum();
								int diff = currentUsefulNum + totalNumber;
								if(diff>maxNum){
									pw.write("仓库"+houseID+"的"+commodityType+"容量不足");
									System.out.println("仓库"+houseID+"的"+commodityType+"容量不足");
									numDiff=true;
									break;
								}else{
									alhv.get(j).setCurrentUsefulNum(diff);
								}
								break;
							}
						}
					}
					for(int i=0;i<alsil.size();i++){
						if(alsil.get(i).getNumber()<0){
							numDiff=true;
							break;
						}
					}
					
					if(!numDiff){
						WorkerDaoImpl wo = new WorkerDaoImpl();
						int outputID = wo.getOutputID();
						int workerID = jsonObject.getInt("workerID");
						String result = wo.waresOut(outputID, workerID, jsonArray);
						for(int i=0;i<alhv.size();i++){
							hvdi.updateHouseVolume(alhv.get(i));
						}
						pw.write(result);
					}
				}
				
				
				
				
				
//				if(ce==false&&pe==false&&he==false&&CE==false){				
//					WorkerDaoImpl wo = new WorkerDaoImpl();
//					int outputID = wo.getOutputID();
//					int workerID = jsonObject.getInt("workerID");
//					String result = wo.waresOut(outputID, workerID, jsonArray);
//					pw.write(result);
//				}
				
			} catch (Exception e) {
				e.printStackTrace();
				pw.write("warse failed 0");
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
