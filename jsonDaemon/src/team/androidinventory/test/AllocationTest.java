package team.androidinventory.test;

import java.util.ArrayList;
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

public class AllocationTest {
	static WorkerDaoImpl wo = new WorkerDaoImpl();
	static WorkerDaoImpl wwo = wo;
	public static void main(String[] args){
		
		JSONObject jo = new JSONObject();
		JSONArray allocateArray = new JSONArray();
		try{
			for(int j = 0;j<1;j++){
				JSONObject stockitem = new JSONObject();
				stockitem.put("commodityID",(12345678)+"");
				stockitem.put("inHouseID",1001);
				stockitem.put("outHouseID",1004);
				stockitem.put("totalNumber",3);
				stockitem.put("providerID",20000001);
				allocateArray.put(stockitem);
			}
			jo.put("stockitems", allocateArray);
		}catch(Exception e){
			System.out.println("json创建失败");
		}
		
			JSONObject jsonObject;
			try {				
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
						System.out.println(commodityExist);
					}
					int providerID = commodityAllocationItem.getInt("providerID");
					providerExist = utils.isProviderExist(providerID);
					if(providerExist .contains("不存在")){
						pe=true;
						System.out.println(providerExist);
					}			
					int inHouseID = commodityAllocationItem.getInt("inHouseID");
					if(houseExist .contains("不存在")){
						he=true;
						System.out.println(houseExist+"\n");
					}else
						houseIDs.add(inHouseID);		
					int outHouseID = commodityAllocationItem.getInt("outHouseID");
					houseExist = utils.isHouseExist(outHouseID);
					if(houseExist .contains("不存在")){
						he=true;
						System.out.println(houseExist+"\n");
					}else
						houseIDs.add(outHouseID);	
				}
				System.out.println("存在性判断结束");
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
								System.out.println(si.getCommodityID());
								if(si.getNumber()>0){
									alsil.add(sidi.getStoreItemList(si));
									System.out.println(1);
								}else{
									System.out.println("错误所在位置：StoreItemDaoImpl.getStoreItemList(StoreItemList)");
								}
							}else{
								System.out.println(4);
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
									System.out.println(2);
								}else{
									System.out.println("错误所在位置：StoreItemDaoImpl.getStoreItemList(StoreItemList)");
								}
							}else{
								System.out.println(3);
							}
						}
						
					}
					System.out.println(alhv.size());
					for(int i=0;i<alhv.size();i++){
						System.out.println(alhv.get(i).getHouseID()+" "+alhv.get(i).getCommodityType());
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
							System.out.println("存在仓库容积不足以调入");
							break;
						}
						if(alhv.get(i).getCurrentUsefulNum()>alhv.get(i).getMaxNum()){
							inNumDiff=true;
							System.out.println("存在仓库容积调拨异常");
							break;
						}
					}
					
					if(!inNumDiff&&!outNumDiff){
						WorkerDaoImpl wo = new WorkerDaoImpl();
						int commodityAllocationID = wo.getCommodityAllocationID();
						int workerID = 10000002;
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
					}else{
						System.out.println("调拨失败");	
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 

	}
