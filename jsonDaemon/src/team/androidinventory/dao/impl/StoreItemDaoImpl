package team.androidinventory.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import team.androidinventory.model.HouseVolume;
import team.androidinventory.model.StoreItemList;

public class StoreItemDaoImpl {
	DBUtil dbu = new DBUtil();
	Connection conn = dbu.getConnForMySql();

	public void closeConn() {
		//dbu.CloseResources(conn);
	}
	
	public void unitToM3(int volume , String unit){
		switch(unit){
		case "m3":
			
			break;
		}
	}
//	public ArrayList<StoreItemList> getStoreItemList(){
//		
//	}
	
	public void updateStoreItemList(StoreItemList sil){
		int houseID = sil.getHouseID();
	    String commodityID = sil.getCommodityID();
	    int providerID = sil.getProviderID();
	    int number = sil.getNumber();
	    //conn = dbu.getConnForMySql();
		String sql = "select number from storeitemlist where commodityID='"+commodityID+"'"+" and houseID="+houseID+" and providerID="+providerID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				if(r.getInt(1)!=number){
					sql = "update storeitemlist set number="+number+" where commodityID='"+commodityID+"'"+" and houseID="+houseID+" and providerID="+providerID;
					ps = conn.prepareStatement(sql);
				    int re = ps.executeUpdate();
				    if(re>0)
					    System.out.println("更新成功");
				}else{
					//无动作
					System.out.println(houseID+"的"+commodityID+"数量无需更新");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("错误所在位置：StoreItemDaoImpl.updateStoreItemList(StoreItemList)");
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
	}

	public StoreItemList getStoreItemList(StoreItemList sil) {
		// TODO Auto-generated method stub
	    String commodityID = sil.getCommodityID();
	    int houseID = sil.getHouseID();
	    int providerID = sil.getProviderID();
	    //conn = dbu.getConnForMySql();
		String sql = "select number from storeitemlist where commodityID='"+commodityID+"'"+" and houseID="+houseID+" and providerID="+providerID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				int number = r.getInt(1);
//				System.out.println("更新成功");
				sil.setNumber(number);
				return sil;
				}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("错误所在位置：StoreItemDaoImpl.getStoreItemList(StoreItemList)");
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		sil.setNumber(0);
		return sil;
	}
}
