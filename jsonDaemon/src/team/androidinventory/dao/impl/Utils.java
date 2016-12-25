package team.androidinventory.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Utils {
public Connection conn;
	DBUtil dbu = new DBUtil();
	Connection conn = dbu.getConnForMySql();

	public void closeConn() {
		//dbu.CloseResources(conn);
	}
	
	public String isCommodityExist(String commodityID){
		//conn = dbu.getConnForMySql();
		String sql = "select * from commodity where commodityID = '" + commodityID+"'";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				dbu.CloseResources(r, ps);
				return "exist";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return "商品"+commodityID+"不存在;";
	}
	
	public String isHouseExist(String name){
		//conn = dbu.getConnForMySql();
		String sql = "select * from house where name = '" + name+"'";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				dbu.CloseResources(r, ps);
				return "exist";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return "仓库"+name+"不存在;";
	}
	
	public String isProviderExist(String name){
		//conn = dbu.getConnForMySql();
		String sql = "select * from provider where name = '" + name+"'";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				dbu.CloseResources(r, ps);
				return "exist";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return "供应商"+name+"不存在;";
	}
	
	public String isCustomerExist(String name){
		//conn = dbu.getConnForMySql();
		String sql = "select * from customer where name = '" + name+"'";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				dbu.CloseResources(r, ps);
				return "exist";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return "客户"+name+"不存在;";
	}
	public String isHouseExist(int houseID) {
		// TODO Auto-generated method stub
		//1,success
		//0,failed
		//conn = dbu.getConnForMySql();
		String sql = "select * from house where houseID = " + houseID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				dbu.CloseResources(r, ps);
				return "exist";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return "仓库"+houseID+"不存在;";
	}
	public String isProviderExist(int providerID) {
		// TODO Auto-generated method stub
		//conn = dbu.getConnForMySql();
		String sql = "select * from provider where providerID = " + providerID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				dbu.CloseResources(r, ps);
				return "exist";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return "供应商"+providerID+"不存在;";
	}
	public String isCustomerExist(int customerID) {
		// TODO Auto-generated method stub
		//conn = dbu.getConnForMySql();
		String sql = "select * from customer where customerID = " + customerID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				dbu.CloseResources(r, ps);
				return "exist";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return "客户"+customerID+"不存在;";
	}	
	public int getHouseVolume(int houseID){//unit m²
			// TODO Auto-generated method stub
			//common int >0,success
			//0,failed
			String sql = "select length,width,height from house where houseID = " + houseID;
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
				ResultSet r = ps.executeQuery();
				if (r.next()) {
					int length = r.getInt(1);
					int width = r.getInt(2);
					int height = r.getInt(3);
					dbu.CloseResources(r, ps);
					return length*width*height;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			dbu.CloseResources(ps);
			return 0;
	}
	
	public String getHouseName(int houseID){
		String sql = "select name from house where houseID = " + houseID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				String name = r.getString(1);
				dbu.CloseResources(r, ps);
				//dbu.CloseResources(conn);
				return name;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return null;
	}
	
	public String getCommodityName(String commodityID){
		String sql = "select name from commodity where commodityID = " + commodityID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				String name = r.getString(1);
				dbu.CloseResources(r, ps);
				//dbu.CloseResources(conn);
				return name;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		//dbu.CloseResources(conn);
		return null;
	}
	
	public ArrayList<Integer> RemoveSame(ArrayList<Integer> list)
    {
        //上面写的那句是多余的，这个是最终的  
        for (int i = 0; i < list.size(); i++)
        {
            for (int j = i + 1; j < list.size(); j++)
            {
                if (list.get(i)-list.get(j)==0)
                {
                    list.remove(j);
                    j--;
                }
            }
        }
        return list;
    }
}
