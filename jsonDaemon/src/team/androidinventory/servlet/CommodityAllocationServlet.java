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

@WebServlet("/allocation")
public class CommodityAllocationServlet extends HttpServlet {
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
//				WorkerDaoImpl wo = new WorkerDaoImpl();
				
				JSONArray allocateArray = jsonObject.getJSONArray("commodityAllocationItems");
				String commodityExist = "";
				boolean ce = false;
				String providerExist = "";
				boolean pe = false;
				String houseExist="";
				boolean he = false;
				boolean alhvbl = false;
				Utils utils = new Utils();
				ArrayList<Integer> houseIDs = new ArrayList<Integer>();
				for(int i=0;i<allocateArray.length();i++){
					JSONObject commodityAllocationItem = allocateArray.getJSONObject(i);
					String commodityID = commodityAllocationItem.getString("commodityID");
					commodityExist = utils.isCommodityExist(commodityID);
					if(commodityExist.contains("不存在")){
						ce=true;
						pw.write(commodityExist+"\n");
					}
					int providerID = commodityAllocationItem.getInt("providerID");
					providerExist = utils.isProviderExist(providerID);
					if(providerExist .contains("不存在")){
						pe=true;
						pw.write(providerExist+"\n");
					}			
					int inHouseID = commodityAllocationItem.getInt("inHouseID");
					houseExist = utils.isHouseExist(inHouseID);
					if(houseExist .contains("不存在")){
						he=true;
						pw.write(houseExist+"\n");
					}else
						houseIDs.add(inHouseID);		
					int outHouseID = commodityAllocationItem.getInt("outHouseID");
					houseExist = utils.isHouseExist(outHouseID);
					if(houseExist .contains("不存在")){
						he=true;
						pw.write(houseExist+"\n");
					}else
						houseIDs.add(outHouseID);		
				}//存在性判断
				HouseVolumeDaoImpl hvdi = new HouseVolumeDaoImpl();				
				ArrayList<HouseVolume> alhv = new ArrayList<HouseVolume>();
				ArrayList<StoreItemList> alsil = new ArrayList<StoreItemList>();
				houseIDs = utils.RemoveSame(houseIDs);
				if(houseIDs.size()>0&&he==false){
					Set set = new HashSet(alhv);
					for(int i=0;i<houseIDs.size();i++){
						alhv =hvdi.getHouseVolume(houseIDs.get(i));//可优化为只提供当前要求的仓库-类型对
						if(alhv.size()>0){
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
				
				//下面开始判断能否调拨
				boolean inNumDiff = false;
				boolean outNumDiff = false;
				if(ce==false&&pe==false&&he==false&&alhvbl==false){
					for(int i=0;i<allocateArray.length();i++){
						JSONObject allocateItem = allocateArray.getJSONObject(i);
						String commodityID = allocateItem.getString("commodityID");
						int providerID = allocateItem.getInt("providerID");
						int inHouseID = allocateItem.getInt("inHouseID");
						int outHouseID = allocateItem.getInt("outHouseID");
						int totalNumber = allocateItem.getInt("totalNumber");
						boolean isAllocateItemRepeat = false;
						for(int j =0;j<alsil.size();j++){
							if(commodityID.equals(alsil.get(j).getCommodityID())&&providerID==alsil.get(j).getProviderID()
									&&inHouseID==alsil.get(j).getHouseID()){
								isAllocateItemRepeat = true;
								break;
							}
							if(!isAllocateItemRepeat){
								StoreItemDaoImpl sidi = new StoreItemDaoImpl();
								StoreItemList si = new StoreItemList();
								si.setCommodityID(commodityID);
								si.setHouseID(inHouseID);
								si.setProviderID(providerID);
								si = sidi.getStoreItemList(si);
								if(si.getNumber()>0){
									alsil.add(sidi.getStoreItemList(si));
								}else{
									System.out.println("错误所在位置：StoreItemDaoImpl.getStoreItemList(StoreItemList)");
								}
							}
						}
						for(int j =0;j<alsil.size();j++){
							if(commodityID.equals(alsil.get(j).getCommodityID())&&providerID==alsil.get(j).getProviderID()
									&&outHouseID==alsil.get(j).getHouseID()){
								isAllocateItemRepeat = true;
								break;
							}
							if(!isAllocateItemRepeat){
								StoreItemDaoImpl sidi = new StoreItemDaoImpl();
								StoreItemList si = new StoreItemList();
								si.setCommodityID(commodityID);
								si.setHouseID(outHouseID);
								si.setProviderID(providerID);
								si = sidi.getStoreItemList(si);
								if(si.getNumber()>0){
									alsil.add(sidi.getStoreItemList(si));
								}else{
									System.out.println("错误所在位置：StoreItemDaoImpl.getStoreItemList(StoreItemList)");
								}
							}
						}
						
					}
					
					for(int i=0;i<allocateArray.length();i++){
						if(inNumDiff){
							break;
						}
						JSONObject allocateItem = allocateArray.getJSONObject(i);
						String commodityID = allocateItem.getString("commodityID");
						int providerID = allocateItem.getInt("providerID");
						int inHouseID = allocateItem.getInt("inHouseID");
						int outHouseID = allocateItem.getInt("outHouseID");
						int totalNumber = allocateItem.getInt("totalNumber");
						for(int j=0;j<alsil.size();j++){
							StoreItemList si = alsil.get(j);
							if(commodityID.equals(si.getCommodityID())&&inHouseID==si.getHouseID()
									&&providerID==si.getProviderID()){
								int number = si.getNumber();
								int diff = number - totalNumber;
								alsil.get(j).setNumber(diff);
							}
						}
						String commodityType = hvdi.getCommodityType(commodityID);
						int inCount=0;
						for(int j=0;j<alhv.size();j++){
							HouseVolume hv = alhv.get(j);
							if(commodityType.equals(hv.getCommodityType())&&inHouseID==hv.getHouseID()){
								int maxNum = hv.getMaxNum();
								int currentUsefulNum = hv.getCurrentUsefulNum();
								int diff = currentUsefulNum + totalNumber;
							    alhv.get(j).setCurrentUsefulNum(diff);
								break;
							}else{
								inCount++;
							}
						}
						if(inCount>=alhv.size()){
							inNumDiff = true;
						}
						for(int j=0;j<alsil.size();j++){
							StoreItemList si = alsil.get(j);
							if(commodityID.equals(si.getCommodityID())&&outHouseID==si.getHouseID()
									&&providerID==si.getProviderID()){
								int number = si.getNumber();
								int diff = number + totalNumber;
								alsil.get(j).setNumber(diff);
							}
						}
						int outCount=0;
						for(int j=0;j<alhv.size();j++){
							HouseVolume hv = alhv.get(j);
							if(commodityType.equals(hv.getCommodityType())&&outHouseID==hv.getHouseID()){
								int maxNum = hv.getMaxNum();
								int currentUsefulNum = hv.getCurrentUsefulNum();
								int diff = currentUsefulNum - totalNumber;
							    alhv.get(j).setCurrentUsefulNum(diff);
								break;
							}else{
								outCount++;
							}
						}
						if(outCount>=alhv.size()){
							outNumDiff=true;
						}
						
					}
					for(int i=0;i<alsil.size();i++){
						if(alsil.get(i).getNumber()<0){
							inNumDiff=true;
							System.out.println("存在仓库商品数量不足以调出");
							break;
						}
					}
					for(int i=0;i<alhv.size();i++){
						if(alhv.get(i).getCurrentUsefulNum()<0){
							outNumDiff=true;
							pw.write("存在仓库容积不足以调入");
							break;
						}
						if(alhv.get(i).getCurrentUsefulNum()>alhv.get(i).getMaxNum()){
							inNumDiff=true;
							pw.write("存在仓库容积调拨异常");
							break;
						}
					}					
					if(!inNumDiff&&!outNumDiff){
						WorkerDaoImpl wo = new WorkerDaoImpl();
						int commodityAllocationID = wo.getCommodityAllocationID();
						int workerID = jsonObject.getInt("workerID");
						wo.commodityAllocation(workerID);
						for(int i=0;i<allocateArray.length();i++){
							JSONObject commodityAllocationItem = allocateArray.getJSONObject(i);
							String commodityID = commodityAllocationItem.getString("commodityID");
							int providerID = commodityAllocationItem.getInt("providerID");
							int inHouseID = commodityAllocationItem.getInt("inHouseID");
							int outHouseID = commodityAllocationItem.getInt("outHouseID");
							int totalNumber = commodityAllocationItem.getInt("totalNumber");
							String result = wo.commodityAllocationItem(commodityAllocationID, commodityID, inHouseID, outHouseID, totalNumber,providerID);
						}
						for(int i=0;i<alhv.size();i++){
							hvdi.updateHouseVolume(alhv.get(i));
						}
						System.out.println("调拨成功");
						pw.write("success");	
						pw.flush();
						pw.close();
					}
					
				}
				
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
				pw.write("warse failed 0");
				pw.flush();
				pw.close();
			}
		} else {
			pw.write("warse failed");
			pw.flush();
			pw.close();
		}

		
		br.close();

		

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

}
