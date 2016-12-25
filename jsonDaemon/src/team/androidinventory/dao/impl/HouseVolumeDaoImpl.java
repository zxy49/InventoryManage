package team.androidinventory.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.JSONArray;

import team.androidinventory.model.HouseVolume;

public class HouseVolumeDaoImpl {
	public Connection conn1;
	DBUtil dbu = new DBUtil();
	Connection conn = dbu.getConnForMySql();

	public void closeConn() {
		//dbu.CloseResources(conn);
	}
	

	public String isCommodityTypeExists(String commodityType){
		////conn = dbu.getConnForMySql();
		String sql = "select * from varietyEnum where type='"+commodityType+"'";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				dbu.CloseResources(r, ps);
				//dbu.CloseResources(conn);
				return "exist";
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("错误所在位置：HouseVolumeDaoImpl.isCommodityTypeExists(String)");
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return "商品类型"+commodityType+"不存在";
	}
	
	public int getCurrentUsefulNum(int houseID,String commodityType){
		////conn = dbu.getConnForMySql();
		String sql = "select * from houseVolume where commodityType='"+commodityType+"'"+" and houseID="+houseID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				int usefulNum = r.getInt(4);
				dbu.CloseResources(r, ps);
				//dbu.CloseResources(conn);
				return usefulNum;
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("错误所在位置：HouseVolumeDaoImpl.getCurrentUsefulNum(int,String)");
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return 0;//不可用或者该仓库没有这种容量
	}
	
	public ArrayList<HouseVolume> getHouseVolume(int houseID){
		////conn = dbu.getConnForMySql();
		ArrayList<HouseVolume> alhv = new ArrayList<HouseVolume>();
		String sql = "select * from housevolume where houseID="+houseID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if(r.next()) {
				do{
					HouseVolume hv = new HouseVolume();
					hv.setHouseID(r.getInt(1));
					hv.setCommodityType(r.getString(2));
					hv.setMaxNum(r.getInt(3));
					hv.setCurrentUsefulNum(r.getInt(4));
					alhv.add(hv);
				}while(r.next());
				dbu.CloseResources(r, ps);
				//dbu.CloseResources(conn);
				return alhv;
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("错误所在位置：HouseVolumeDaoImpl.getHouseVolume(int)");
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return alhv;
	}
	

	
	
	public String getCommodityType(String commodityID){
		////conn = dbu.getConnForMySql();
		String sql = "select type from commodity where commodityID='"+commodityID+"'";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				String result= r.getString(1);
				dbu.CloseResources(r, ps);
				//dbu.CloseResources(conn);
				return result;
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("错误所在位置：HouseVolumeDaoImpl.getCommodityType(String)");
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return commodityID+"的商品类型因未知原因获取失败，请联系管理员";
	}
	
	
	public void updateHouseVolume(HouseVolume hv){
		int houseID = hv.getHouseID();
	    String commodityType = hv.getCommodityType();
	    int currentUsefulNum = hv.getCurrentUsefulNum();
	    ////conn = dbu.getConnForMySql();
		String sql = "select currentUsefulNum from housevolume where commodityType='"+commodityType+"'"+" and houseID="+houseID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				if(r.getInt(1)!=currentUsefulNum){
					sql = "update housevolume set currentUsefulNum="+currentUsefulNum+" where commodityType='"+commodityType+"'"+" and houseID="+houseID;
					ps = conn.prepareStatement(sql);
					int re = ps.executeUpdate();
				    if(re>0){
				    	System.out.println(hv.getHouseID()+"的"+hv.getCommodityType()+"更新成功");
				    }
				}else{
					//无动作
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("错误所在位置：HouseVolumeDaoImpl.updateHouseVolume(HouseVolume)");
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
	}
}
