package team.androidinventory.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import team.androidinventory.dao.impl.HouseVolumeDaoImpl;
import team.androidinventory.dao.impl.InputDaoImpl;
import team.androidinventory.dao.impl.Utils;
import team.androidinventory.dao.impl.WorkerDaoImpl;
import team.androidinventory.model.HouseVolume;

public class WarseInTest {
	WorkerDaoImpl wo = new WorkerDaoImpl();
	
	public static void main(String[] args){
		JSONObject jo = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try{
			for(int i = 0;i<100;i++){
				JSONObject stockitem = new JSONObject();
				stockitem.put("commodityID",(12345678)+"");
				stockitem.put("houseID",1001);
				stockitem.put("totalNumber",1);
				stockitem.put("providerID",20000001);
				stockitem.put("time", 10);
				jsonArray.put(stockitem);
			}
			jo.put("stockitems", jsonArray);
			jo.put("workerID", 10000002);
		}catch(Exception e){
			System.out.println("json创建失败");
		}
		try {
			InputDaoImpl wo = new InputDaoImpl();
			wo.warseIn(jo.toString());
			wo.closeConn();
			
//			JSONArray stockarray = jsonArray;
//			String commodityExist = "";
//			boolean ce = false;
//			String providerExist = "";
//			boolean pe = false;
//			String houseExist="";
//			boolean he = false;
//			boolean te =false;
//			Utils utils = new Utils();
//			boolean alhvbl = false;//仓库-类型对是否存在
//			ArrayList<Integer> houseIDs = new ArrayList<Integer>();
//			long startTime = System.currentTimeMillis();
//			System.out.println("开始入库检测");
//			for (int i = 0; i<stockarray.length();i++) {
//				JSONObject stockitem = stockarray.getJSONObject(i);
//				String commodityID = stockitem.getString("commodityID");
//				commodityExist = utils.isCommodityExist(commodityID);
//				if(commodityExist.contains("不存在")){
//					ce=true;
//					System.out.println(commodityExist);
//				}
//				int houseID = stockitem.getInt("houseID");
//				houseExist = utils.isHouseExist(houseID);
//				houseIDs.add(houseID);
//				if(houseExist .contains("不存在")){
//					he=true;
//					System.out.println(houseExist);
//				}		
//				int providerID = stockitem.getInt("providerID");
//				providerExist = utils.isProviderExist(providerID);
//				if(providerExist .contains("不存在")){
//					pe=true;
//					System.out.println(providerExist);
//				}			
//				Date time = new java.util.Date(System.currentTimeMillis());
//				int day = stockitem.getInt("time");
//				if(day<=0){
//					te=true;
//					System.out.println("存储期限错误");
//				}
//			}
//			long endTime = System.currentTimeMillis()-startTime;
//			System.out.println("入库检测结束: "+endTime/1000F+"s");
//			
//			startTime = System.currentTimeMillis();
//			HouseVolumeDaoImpl hvdi = new HouseVolumeDaoImpl();				
//			ArrayList<HouseVolume> alhv = new ArrayList<HouseVolume>();
//			houseIDs = utils.RemoveSame(houseIDs);
//			if(houseIDs.size()>0&&he==false){
//				Set set = new HashSet(alhv);
//				for(int i=0;i<houseIDs.size();i++){
//					alhv =hvdi.getHouseVolume(houseIDs.get(i));//可优化为只提供当前要求的仓库-类型对
//					if(alhv.size()>0){
//					    set.addAll(new HashSet(alhv));
//					}
//					else{
//						alhvbl = true;
//						System.out.println(houseIDs.get(i)+"不存在可存储空间");
//					}
//				}
//				alhv = new ArrayList<HouseVolume>(set);
//			}
//			if(alhv.size()<=0){
//				alhvbl = true;
//				System.out.println("不存在可存储空间");
//			}
//			
//			
//			//下面开始判断能否入库
//			boolean numDiff = false;
//			if(ce==false&&pe==false&&he==false&&te==false&&alhvbl==false){
//				for(int i=0;i<stockarray.length();i++){
//					if(numDiff){
//						break;
//					}
//					JSONObject stockitem = stockarray.getJSONObject(i);
//					int houseID = stockitem.getInt("houseID");
//					String commodityID = stockitem.getString("commodityID");
//					int totalNumber = stockitem.getInt("totalNumber");
//					String commodityType = hvdi.getCommodityType(commodityID);
//					for(int j=0;j<alhv.size();j++){
//						HouseVolume hv = alhv.get(j);
//						if(commodityType.equals(hv.getCommodityType())&&houseID==hv.getHouseID()){
//							int maxNum = hv.getMaxNum();
//							int currentUsefulNum = hv.getCurrentUsefulNum();
//							int diff = currentUsefulNum - totalNumber;
//							if(diff<0){
//								System.out.println("仓库"+houseID+"的"+commodityType+"无法容纳"+commodityID);
//								numDiff=true;
//								break;
//							}else{
//								alhv.get(j).setCurrentUsefulNum(diff);
//							}
//							break;
//						}
//					}
//				}
//				for(int i=0;i<alhv.size();i++){
//					if(alhv.get(i).getCurrentUsefulNum()<0){
//						numDiff=true;
//						break;
//					}
//				}
//				//可入库，更新inputlist、stocklist、storeitem（此处应优化成单独更新）  + housevolume
//				if(!numDiff){
//					String result = wo.waresIn(inputID, workerID, jsonArray);
//					for(int i=0;i<alhv.size();i++){
//						hvdi.updateHouseVolume(alhv.get(i));
//					}
//					System.out.println(result);
//				}else{
//					System.out.println("入库失败");
//				}
//			
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
