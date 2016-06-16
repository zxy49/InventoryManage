package team.androidinventory.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import team.androidinventory.dao.impl.HouseVolumeDaoImpl;
import team.androidinventory.dao.impl.StoreItemDaoImpl;
import team.androidinventory.dao.impl.Utils;
import team.androidinventory.dao.impl.WorkerDaoImpl;
import team.androidinventory.model.HouseVolume;
import team.androidinventory.model.StoreItemList;

public class WarseOutTest {
	
	public static void main(String[] args){
			JSONArray jsonArray = new JSONArray();
			try{
				for(int i = 0;i<3;i++){
					JSONObject stockitem = new JSONObject();
					stockitem.put("commodityID",(12345678)+"");
					stockitem.put("houseID",1001);
					stockitem.put("totalNumber",1);
					stockitem.put("providerID",20000001);
					stockitem.put("customerID", 30000001);
//					stockitem.put("time", 10);
					jsonArray.put(stockitem);
				}
			}catch(Exception e){
				System.out.println("json创建失败");
			}
			
			try {
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
				long startTime = System.currentTimeMillis();
				System.out.println("开始出库检测");
				for (int i = 0; i < sellArray.length(); i++) {
					JSONObject stockitem = sellArray.getJSONObject(i);
					String commodityID = stockitem.getString("commodityID");//修改之后要判断是否为""或者Android端自行判断
					commodityExist = utils.isCommodityExist(commodityID);
					if(commodityExist.contains("不存在")){
						ce=true;
						System.out.println(commodityExist);
					}
					int houseID = stockitem.getInt("houseID");
					houseExist = utils.isHouseExist(houseID);
					if(houseExist .contains("不存在")){
						he=true;
						System.out.println(houseExist);
					}else
						houseIDs.add(houseID);		
					int providerID = stockitem.getInt("providerID");
					providerExist = utils.isProviderExist(providerID);
					if(providerExist .contains("不存在")){
						pe=true;
						System.out.println(providerExist);
					}
					int customerID = stockitem.getInt("customerID");
					customerExist = utils.isCustomerExist(customerID);
					if(customerExist .contains("不存在")){
						CE=true;
						System.out.println(customerExist+"  "+customerID);
					}	

				}
				utils.closeConn();
				long endTime = System.currentTimeMillis()-startTime;
				System.out.println("出库检测结束: "+endTime/1000F+"s");
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
							System.out.println(houseIDs.get(i)+"不存在可存储空间");
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
								if(diff>=0){
									alsil.get(j).setNumber(diff);
								}else{
									System.out.println("totalNumber:"+totalNumber);
									System.out.println("number:"+number);
									System.out.println("diff:"+diff);
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
								System.out.println("hvTotalNumber:"+totalNumber);
								System.out.println("currentUsefulNum:"+currentUsefulNum);
								System.out.println("maxNum:"+maxNum);
								if(diff>maxNum){
									System.out.println("仓库"+houseID+"的"+commodityType+"容量不足");
									numDiff=true;
									break;
								}else{
									alhv.get(j).setCurrentUsefulNum(diff);
									System.out.println("nowNum:"+alhv.get(j).getCurrentUsefulNum());
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
						int workerID = 10000002;
						String result = wo.waresOut(outputID, workerID, jsonArray);
						for(int i=0;i<alhv.size();i++){
							hvdi.updateHouseVolume(alhv.get(i));
//							System.out.println(alhv.get(i).getCommodityType()+"\n"+alhv.get(i).getHouseID());
						}
						System.out.println(result);
					}else{
						System.out.println("出库失败");
					}
				}
				
				
				
				
				

				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	}
}
