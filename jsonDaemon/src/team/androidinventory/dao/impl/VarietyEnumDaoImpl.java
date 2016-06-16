package team.androidinventory.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VarietyEnumDaoImpl {
	DBUtil dbu = new DBUtil();
	Connection conn = dbu.getConnForMySql();

	public void closeConn() {
		dbu.CloseResources(conn);
	}
	public String getTypeName(String commodityType){
		String sql = "select typeName from varietyenum where type='"+commodityType+"'";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				String result= r.getString(1);
				dbu.CloseResources(r, ps);
				return result;
			}
		}catch(Exception e){
//			e.printStackTrace();
			System.out.println("错误所在位置：VarietyEnumDaoImpl.getTypeName(String)");
		}
		dbu.CloseResources(ps);
		return commodityType+"类型名称获取失败";
	}
}
