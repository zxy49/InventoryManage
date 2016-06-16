package team.androidinventory.dao.impl;

import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import team.androidinventory.dao.WorkerDao;
import team.androidinventory.model.CommodityAllocation;
import team.androidinventory.model.CommodityAllocationItem;
import team.androidinventory.model.House;
import team.androidinventory.model.HouseVolume;
import team.androidinventory.model.SellList;
import team.androidinventory.model.StockList;
import team.androidinventory.model.StoreItemList;
import team.androidinventory.model.VarietyEnum;
import team.androidinventory.model.Worker;

public class WorkerDaoImpl implements WorkerDao {
	DBUtil dbu = new DBUtil();
	Connection conn = dbu.getConnForMySql();

	public void closeConn() {
		dbu.CloseResources(conn);
	}

	@Override
	public Worker login(int id, String password) {
		// TODO Auto-generated method stub

		String sql = "select * from worker where workerID = '" + id + "' and password = '" + password + "'"+" and state='在职'";
//		String sql = "select * from worker where workerID = '" + id + "' and password = '" + password + "'";

		PreparedStatement ps = dbu.getPreparedStatemnt(conn, sql);
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Worker worker = new Worker();
		try {
			if (rs.next()) {
				worker.setWorkerID(rs.getInt(1));
				worker.setTypeNumber(rs.getInt(2));
				worker.setName(rs.getString(3));
				worker.setAddress(rs.getString(4));
				// worker.setPassword(rs.getString(5));
				worker.setPosition(rs.getString(6));
				worker.setTel(rs.getString(7));
			} else {
				worker = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dbu.CloseResources(rs, ps);
		return worker;
	}

	@Override
	public int getInputID() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select max(inputID) from inputlist";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int inputID = 0;
		if (rs != null) {
			try {
				rs.next();
				inputID = rs.getInt(1) + 1;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			inputID = 201600001;
		}
		dbu.CloseResources(rs, ps);
		return inputID;
	}

	@Override
	public int getOutputID() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select max(outputID) from outputlist";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int outputID = 0;
		if (rs != null) {
			try {
				rs.next();
				outputID = rs.getInt(1) + 1;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			outputID = 200000001;
		}
		dbu.CloseResources(rs, ps);
		return outputID;
	}

	@Override
	// public String waresIn(String provider, Date date, String[] cid, int[]
	// unit, String[] house, int[] amount) {
	// TODO Auto-generated method stub
	//要拆分开，入库只入库inputlist、stocklist，storeitem单独更新
	public String waresIn(int inputID, int workerID, JSONArray jsonArray) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = insertIntoInputlist(inputID, workerID);
		if (result == 1) {
			try {
				JSONArray stockarray = jsonArray;
				for (int i = 0; i < stockarray.length(); i++) {
					JSONObject stockitem = stockarray.getJSONObject(i);
					String commodityID = stockitem.getString("commodityID");
					int houseID = stockitem.getInt("houseID");
					int providerID = stockitem.getInt("providerID");
					int time = stockitem.getInt("time");
					int totalNumber = stockitem.getInt("totalNumber");
					int iisl = insertIntoStockList(inputID, commodityID, houseID, providerID, time, totalNumber);
					if (iisl == 1) {
//						int uc = updateCommodity(commodityID, totalNumber, "add");
//						if (uc == 1) {
							int us = updateStoreitemlist(houseID, commodityID, totalNumber, "add",providerID);
							if (us == 1) {
								//入库成功
							} else {
								return "入库失败";
							}
//						} else {
//							return "修改商品表失败";
//						}
					} else {
						return "新增进货表失败";
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return "新增入库单失败";
		}
		dbu.CloseResources(rs, ps);
		return "入库成功";
	}

//	public int updateCommodity(String commodityID, int number, String type) {
//		// 返回类型定义
//		// 0，更新失败
//		// 1，更新成功
//		// 2,数量不足无法更新
//		String sql = "select * from commodity where commodityID=" + commodityID;
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sql);
//			ResultSet r = ps.executeQuery();
//			if (r.next()) {
//				int num = r.getInt(3);
//				if (type.equals("add")) {
//					num += number;
//					sql = "update commodity set totalNumber=" + num + " where commodityID=" + commodityID;
//				} else {
//					num -= number;
//					if (num >= 0)
//						sql = "update commodity set totalNumber=" + num + " where commodityID=" + commodityID;
//					else
//						return 2;
//				}
//				ps = conn.prepareStatement(sql);
//				int result = ps.executeUpdate();
//				if (result > 0) {
//					return 1;
//				}
//			} else {
//				if (type.equals("add")) {
//					sql = "insert into commodity(commodityID,totalNumber) values(?,?)";
//					ps = conn.prepareStatement(sql);
//					ps.setString(1, commodityID);
//					ps.setInt(2, number);
//					int result = ps.executeUpdate();
//					if (result > 0) {
//						return 1;
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}

	public int updateStoreitemlist(int houseID, String commodityID, int number, String type,int providerID) {
		// 返回类型定义
		// 0，更新失败
		// 1，更新成功
		// 2,数量不足无法减少

		String sql = "select * from storeitemlist where houseID = " + houseID + " and commodityID = " + commodityID+" and providerID="+providerID;;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				int num = r.getInt(5);
				if (type.equals("add")) {
					num += number;
					sql = "update storeitemlist set number=" + num + " where houseID=" + houseID + " and commodityID="
							+ commodityID+" and providerID="+providerID;
				} else {
					num -= number;
					if (num >= 0)
						sql = "update storeitemlist set number=" + num + " where houseID=" + houseID
								+ " and commodityID=" + commodityID+" and providerID="+providerID;
					else
					{
						dbu.CloseResources(r, ps);
						return 2;}
				}
				ps = conn.prepareStatement(sql);
				int result = ps.executeUpdate();
				if (result > 0) {
					dbu.CloseResources(r, ps);
					return 1;
				}
			} else {
				sql = "insert into storeitemlist(houseID,commodityID,number,providerID) values(?,?,?,?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, houseID);
				ps.setString(2, commodityID);
				ps.setInt(3, number);
				ps.setInt(4, providerID);
				int result = ps.executeUpdate();
				if (result > 0) {
					dbu.CloseResources(r, ps);
					return 1;
				}
			}
			dbu.CloseResources(r, ps);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		return 0;
	}

	public int insertIntoStockList(int inputID, String commodityID, int houseID, int providerID, int time,
			int totalNumber) {
		// 返回类型定义
		// 0，插入stocklist失败
		// 1，插入stocklist成功
		String sql = "insert into stocklist(inputID,commodityID,houseID,providerID,time,totalNumber) values(?,?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, inputID);
			ps.setString(2, commodityID);
			ps.setInt(3, houseID);
			ps.setInt(4, providerID);
			ps.setInt(5, time);
			ps.setInt(6, totalNumber);
			int result = ps.executeUpdate();
			if (result > 0) {
				dbu.CloseResources(ps);
				return 1;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		return 0;
	}

	public int insertIntoInputlist(int inputID, int workerID) {
		// 返回类型定义
		// 0，插入inputlist失败
		// 1，插入inputlist成功
		String sql = "insert into inputlist(inputID,workerID,inputTime) values(?,?,?)";
		PreparedStatement ps = dbu.getPreparedStatemnt(conn, sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, inputID);
			ps.setInt(2, workerID);
			Date currentDate = new java.sql.Date(System.currentTimeMillis());
			ps.setDate(3, currentDate);
			int result = ps.executeUpdate();
			if (result > 0) {
				dbu.CloseResources(ps);
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		return 0;
	}

	@Override
	public String waresOut(int outputID,int workerID,JSONArray jsonArray) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = insertIntoOutputlist(outputID, workerID);
		if (result == 1) {
			try {
				JSONArray stockarray = jsonArray;
				boolean bo = false;
				for (int i = 0; i < stockarray.length(); i++) {
					JSONObject stockitem = stockarray.getJSONObject(i);
					String commodityID = stockitem.getString("commodityID");
					int houseID = stockitem.getInt("houseID");
					int customerID = stockitem.getInt("customerID");
					int providerID = stockitem.getInt("providerID");
					Date time = new java.sql.Date(System.currentTimeMillis());
					int totalNumber = stockitem.getInt("totalNumber");
					String sql = "select number from storeItemList where commodityID = " + commodityID+" and providerID="+providerID;
					ps = null;
					try {
						ps = conn.prepareStatement(sql);
						ResultSet r = ps.executeQuery();
						if (r.next()) {
							int num = r.getInt(1);
							if(!(num>=totalNumber)){
								bo=true;
							}
						}else{
							bo=true;
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if(!bo){
				for (int i = 0; i < stockarray.length(); i++) {
					JSONObject stockitem = stockarray.getJSONObject(i);
					String commodityID = stockitem.getString("commodityID");
					int houseID = stockitem.getInt("houseID");
					int customerID = stockitem.getInt("customerID");
					int providerID = stockitem.getInt("providerID");
					Date time = new java.sql.Date(System.currentTimeMillis());
					int totalNumber = stockitem.getInt("totalNumber");
					int iisl = insertIntoSellList(outputID, commodityID, houseID, customerID, time, totalNumber,providerID);
					if (iisl == 1) {
//						int uc = updateCommodity(commodityID, totalNumber, "minus",providerID);
							int us = updateStoreitemlist(houseID, commodityID, totalNumber, "minus",providerID);
							if (us == 1) {
								//出库成功
							} else {
								dbu.CloseResources(rs, ps);
								return "出库失败，商品数量不足";
							}
					} else {
						dbu.CloseResources(rs, ps);
						return "新增出货表失败";
					}
				}}else{
					dbu.CloseResources(rs, ps);
					return "商品数量不满足出库要求，出库失败";}
				dbu.CloseResources(rs, ps);
				return "出库成功";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			dbu.CloseResources(rs, ps);
			return "新增出库单失败";
		}
		dbu.CloseResources(rs, ps);
		return "出库失败";
	}
	
	public int insertIntoOutputlist(int outputID, int workerID) {
		// 返回类型定义
		// 0，插入inputlist失败
		// 1，插入inputlist成功
		String sql = "insert into outputlist(outputID,workerID,outputTime) values(?,?,?)";
		PreparedStatement ps = dbu.getPreparedStatemnt(conn, sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, outputID);
			ps.setInt(2, workerID);
			Date currentDate = new java.sql.Date(System.currentTimeMillis());
			ps.setDate(3, currentDate);
			int result = ps.executeUpdate();
			if (result > 0) {
				dbu.CloseResources(ps);
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		return 0;
	}
	
	public int insertIntoSellList(int outputID, String commodityID, int houseID, int customerID, Date time,
			int totalNumber,int providerID) {
		// 返回类型定义
		// 0，插入stocklist失败
		// 1，插入stocklist成功
		String sql = "insert into selllist(outputID,commodityID,houseID,customerID,time,totalNumber,providerID) values(?,?,?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, outputID);
			ps.setString(2, commodityID);
			ps.setInt(3, houseID);
			ps.setInt(4, customerID);
			ps.setDate(5, time);
			ps.setInt(6, totalNumber);
			ps.setInt(7, providerID);
			int result = ps.executeUpdate();
			if (result > 0) {
				dbu.CloseResources(ps);
				return 1;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		return 0;
	}
	@Override
	public boolean set(int workerID, String name, String address, String tel) {
		// TODO Auto-generated method stub
		String sql = "update worker set name=" + name + " and address="+address+" and tel="+tel+" where workerID="
				+ workerID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			int r = ps.executeUpdate();
			if(r>0){
				dbu.CloseResources(ps);
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dbu.CloseResources(ps);
		return false;
	}
	
	@Override
	public boolean changePassword(int workerID, String oldpassword,String newpassword) {
		// TODO Auto-generated method stub
		Worker worker=login(workerID, oldpassword);
		if(null==worker)
			return false;
		else {
			String sql = "update worker set password=" + newpassword+" where workerID="
					+ workerID;
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
				int r = ps.executeUpdate();
				if(r>0){
					return true;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		return false;
	}

	@Override
	public ArrayList<StoreItemList> commodityQuery(String commodityID) {
		// TODO Auto-generated method stub
		String sql = "select * from storeItemList where commodityID = " + commodityID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			ArrayList<StoreItemList> storeitemlists = new ArrayList<StoreItemList>();
			while (r.next()) {
				StoreItemList storeitem = new StoreItemList();
				storeitem.setStoreItemID(r.getInt(1));
				storeitem.setHouseID(r.getInt(2));
				storeitem.setCommodityID(r.getString(3));
				storeitem.setProviderID(r.getInt(4));
				storeitem.setNumber(r.getInt(5));
				storeitemlists.add(storeitem);
			}
			dbu.CloseResources(r, ps);
			return storeitemlists;
		}catch(Exception e){
			dbu.CloseResources(ps);
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public ArrayList<StoreItemList> houseQuery(int houseID) {
		// TODO Auto-generated method stub
		String sql = "select * from storeItemList where houseID = " + houseID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			ArrayList<StoreItemList> storeitemlists = new ArrayList<StoreItemList>();
			while (r.next()) {
				StoreItemList storeitem = new StoreItemList();
				storeitem.setStoreItemID(r.getInt(1));
				storeitem.setHouseID(r.getInt(2));
				storeitem.setCommodityID(r.getString(3));
				storeitem.setProviderID(r.getInt(4));
				storeitem.setNumber(r.getInt(5));
				storeitemlists.add(storeitem);
			}
			dbu.CloseResources(r, ps);
			return storeitemlists;
		}catch(Exception e){
			dbu.CloseResources(ps);
			e.printStackTrace();
		}
		return null;
	}



	public String addCommodity(String commodityID,String name,String type ){
		String sql = "select * from commodity where commodityID = " + commodityID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (!r.next()) {
				sql = "insert into commodity(commodityID,name,type,state) values(?,?,?,?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1,commodityID);
				ps.setString(2, name);
				ps.setString(3, type);
				ps.setString(4, "可用");
				int result = ps.executeUpdate();
				if (result > 0) {
					dbu.CloseResources(r, ps);
					return "success,商品添加成功";
				}
			}else{
				dbu.CloseResources(r, ps);
				return "failed,商品已存在";
				}
		}catch(Exception e){
			dbu.CloseResources(ps);
			e.printStackTrace();
		}
		return "failed,商品添加失败";
	}


//	@Override
//	public String addHouse(String housename,double length,double width,double ) {
//		// TODO Auto-generated method stub
//		String sql = "select * from house where name = '" + housename+"'";
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sql);
//			ResultSet r = ps.executeQuery();
//			if (!r.next()) {
//				sql ="select max(houseID) from house";
//				ps = conn.prepareStatement(sql);
//				r = ps.executeQuery();
//				int ID;
//				if(r.next()){
//					ID = r.getInt(1)+1;
//				}else
//					ID = 2001;
//				sql = "insert into house(houseID,name,state,length,width,) values(?,?,?,?,?,?)";
//				ps = conn.prepareStatement(sql);
//				ps.setInt(1,ID);
//				ps.setString(2, housename);
//				ps.setString(3, "inuse");
//				ps.setDouble(4, length);
//				ps.setDouble(5, width);
//				ps.setDouble(6, );
//				int result = ps.executeUpdate();
//				if (result > 0) {
//					return "success,仓库创建成功";
//				}
//			}else
//				return "failed,仓库名已存在";
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return "failed,仓库创建失败";
//	}
	
	@Override
	public String addHouse(String housename,ArrayList<HouseVolume> list)  {
		// TODO Auto-generated method stub
		String sql = "select * from house where name = '" + housename+"'";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (!r.next()) {
				sql ="select max(houseID) from house";
				ps = conn.prepareStatement(sql);
				r = ps.executeQuery();
				int ID;
				if(r.next()){
					ID = r.getInt(1)+1;
				}else
					ID = 2001;
				sql = "insert into house(houseID,name,state) values(?,?,?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1,ID);
				ps.setString(2, housename);
				ps.setString(3, "inuse");
				
				int result1 = ps.executeUpdate();
				
				sql = "insert into houseVolume(houseID,commodityType,maxNum,currentUsefulNum) values(?,?,?,0)";
				ps = conn.prepareStatement(sql);

				for(HouseVolume volume:list){
					ps.setInt(1, ID);
					ps.setString(2, volume.getCommodityType());
					ps.setInt(3, volume.getMaxNum());
				}
				int result2 = ps.executeUpdate();

				if (result1 > 0 && result2>0) {
					return "success,仓库创建成功";
				}
			}else
				return "failed,仓库名已存在";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "failed,仓库创建失败";
	}

	@Override
	public String deleteHouse(int houseID) {
		// TODO Auto-generated method stub
		boolean inUse=false;
		String sql="select * from house where houseID="+houseID+" and state='inuse'";
		PreparedStatement ps=null;

		try {
			ps=conn.prepareStatement(sql);
			ResultSet set=ps.executeQuery();
			if(set.next())
				inUse=true;
			
			sql="select * from storeitemlist where houseID="+houseID;
			ps=conn.prepareStatement(sql);

			set=ps.executeQuery();
			if(set.next())
				inUse=true;
			System.out.println("/////////"+inUse);
			if(inUse)
				return "仓库正在使用中，无法删除！";
			else {
				sql="delete from house where houseID="+houseID;
				ps=conn.prepareStatement(sql);
				int result=ps.executeUpdate();
				if(result>0)
					return "删除仓库成功！";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return "删除仓库失败！";
	}

	@Override
	public String commodityModify(String commodityID, String name, String type,double price ) {
		// TODO Auto-generated method stub
		
		String sql = "select * from commodity where commodityID = " + commodityID;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			if (r.next()) {
				sql = "update commodity set name=" + name +" and price="+price+" where commodityID="
							+ commodityID;
				ps = conn.prepareStatement(sql);
				int result = ps.executeUpdate();
				if (result > 0) {
					return "商品更新成功";
				}
			} else {
				this.addCommodity(commodityID, name, type);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "商品设置失败";
	}

	@Override
	public int getCommodityAllocationID() {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select max(commodityAllocationID) from commodityAllocation";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int commodityAllocationID = 0;
		if (rs != null) {
			try {
				rs.next();
				if(rs.getInt(1)==0)
					commodityAllocationID = 300000001;
				else
					commodityAllocationID = rs.getInt(1) + 1;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			commodityAllocationID = 300000001;
		}
		return commodityAllocationID;
	}
	
	public int getCommodityAllocationItemID(){
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select max(commodityAllocationItemID) from commodityAllocationItem";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int commodityAllocationItemID = 0;
		if (rs != null) {
			try {
				rs.next();
				if(rs.getInt(1)==0){
					commodityAllocationItemID = 400000001;
				}else
				commodityAllocationItemID = rs.getInt(1) + 1;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			commodityAllocationItemID = 400000001;
		}
		return commodityAllocationItemID;
	}
	
//	@Override
//	public String commodityAllocationItem(int commodityAllocationID, String commodityID, int inHouseID, int outHouseID,
//			int totalNumber) {
//		
//	}

	@Override
	public String commodityAllocationItem(int commodityAllocationID, String commodityID, int inHouseID, int outHouseID,
			int totalNumber,int providerID) {
		// TODO Auto-generated method stub
		CommodityAllocationItem cai = new CommodityAllocationItem();
		cai.setCommodityAllocationID(commodityAllocationID);
		int id = getCommodityAllocationItemID();
		cai.setCommodityAllocationItemID(id);
		cai.setCommodityID(commodityID);
		cai.setInHouseID(inHouseID);
		cai.setOutHouseID(outHouseID);
		Date time = new java.sql.Date(System.currentTimeMillis());
		cai.setTime(time);
		cai.setTotalNumber(totalNumber);
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from storeItemList where houseID="+inHouseID+" and commodityID="+commodityID+" and providerID="+providerID;		
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				int inHouseCount = rs.getInt(5);
				if(inHouseCount>=totalNumber){
					sql = "insert into commodityAllocationItem(commodityAllocationID,commodityAllocationItemID,commodityID,inHouseID,outHouseID,totalNumber,providerID,time) values(?,?,?,?,?,?,?,?)";
					try {
						ps = conn.prepareStatement(sql);
						ps.setInt(1, commodityAllocationID);
						ps.setInt(2, id);
						ps.setString(3, commodityID);
						ps.setInt(4, inHouseID);
						ps.setInt(5, outHouseID);
						ps.setInt(6, totalNumber);
						ps.setInt(7, providerID);
						ps.setDate(8,new java.sql.Date(System.currentTimeMillis()));
						int r = ps.executeUpdate();
						if(r>0){
							int num = inHouseCount-totalNumber;
							sql = "update storeItemList set number = "+num +" where houseID="+inHouseID+" and commodityID="+commodityID+" and providerID="+providerID;
							ps = conn.prepareStatement(sql);
							r = ps.executeUpdate();
							if(r>0){
								
								sql = "select * from storeItemList where houseID="+outHouseID+" and commodityID="+commodityID+" and providerID="+providerID;;
								ps = conn.prepareStatement(sql);
								rs = ps.executeQuery();
								if(rs.next()){
									int outHouseCount = rs.getInt(5)+totalNumber;
									sql = "update storeItemList set number = "+outHouseCount +" where houseID="+outHouseID+" and commodityID="+commodityID+" and providerID="+providerID;;
									ps = conn.prepareStatement(sql);
									r = ps.executeUpdate();
									if(r>0)
										return "success";
								}else{
									sql = "insert into storeItemList(houseID,commodityID,number,providerID) values(?,?,?,?)";
									ps = conn.prepareStatement(sql);
									ps.setInt(1, outHouseID);
									ps.setString(2, commodityID);
									ps.setInt(3, totalNumber);
									ps.setInt(4, providerID);
									r = ps.executeUpdate();
									if(r>0)
										return "success";
								}
							}
						}else
							return "insert failed";
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else
				return "failed,totalNumber>actual number";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "failed";
	}

	@Override
	public String commodityAllocation(int workerID) {
		// TODO Auto-generated method stub
		CommodityAllocation ca = new CommodityAllocation();
		int id = getCommodityAllocationID();
		ca.setCommodityAllocationID(getCommodityAllocationID());
		Date time = new java.sql.Date(System.currentTimeMillis());
		ca.setTime(time);
		ca.setWorkerID(workerID);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "insert into commodityAllocation(commodityAllocationID,workerID,time) values(?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setInt(2, workerID);
			ps.setDate(3, time);
			int r = ps.executeUpdate();
			if(r>0){
				return "success";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "failed";
	}
	
	@Override
	public ArrayList<House> getHouseList(){
		ArrayList<House> houseList=new ArrayList<House>();
		String sql = "select * from house where state='inuse' ";
		PreparedStatement ps=null;
		
		try {
			ps=conn.prepareStatement(sql);
			ResultSet set=ps.executeQuery();
			while(set.next()){
//				if(set.getString(3).equals("在职")){
					House house=new House();
					house.setHouseID(set.getInt(1));
					house.setName(set.getString(2));
					house.setState(set.getString(3));
//					house.setLength(set.getDouble(5));
//					house.setWidth(set.getDouble(6));
//					house.set(set.getDouble(7));
					if(set.getInt(4)==0)
						house.setIsAvailable(false);
					else {
						house.setIsAvailable(true);
					}
					houseList.add(house);
//				}			
			}
			
			return houseList;
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return null;
	}
	
	@Override
	public String addWorker(Worker worker){
		String sql="select max(workerID) from worker";
		PreparedStatement ps=null;
		try {
			ps=conn.prepareStatement(sql);
			ResultSet resultSet=ps.executeQuery();
			int ID;
			if(resultSet.next())
				ID=resultSet.getInt(1)+1;
			else {
				ID=2001;
			}
			sql="insert into worker(workerID,typeNumber,name,address,password,tel,state,position) values(?,0,?,?,?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, ID);
			ps.setString(2, worker.getName());
			ps.setString(3, worker.getAddress());
			ps.setString(4, worker.getPassword());
			ps.setString(5, worker.getTel());
			ps.setString(6, worker.getState());
			ps.setString(7, worker.getPosition());
			
			int result=ps.executeUpdate();
			if(result>0)
				return "success,用户创建成功";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "failed,用户创建失败！";
	}
	
	@Override
	public String deleteWorker(int id){
		String sql="update worker set state = '离职' where workerID="+id;
		PreparedStatement ps=null;
		try {
			ps=conn.prepareStatement(sql);
			int result=ps.executeUpdate();
			if(result>0)
				return "删除用户成功！";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "删除用户失败！";
	}
	
	@Override
	public ArrayList<Worker> getWorkerList(){
		ArrayList<Worker> workers=new ArrayList<Worker>();
		String sql="select * from worker";
		PreparedStatement ps=null;
		try {
			ps=conn.prepareStatement(sql);
			ResultSet set=ps.executeQuery();
			while(set.next()){
				if(set.getString(8).equals("在职")){
					Worker worker=new Worker();
					worker.setWorkerID(set.getInt(1));
					worker.setTypeNumber(set.getInt(2));
					worker.setName(set.getString(3));
					worker.setAddress(set.getString(4));
					worker.setPassword(set.getString(5));
					worker.setPosition(set.getString(6));
					worker.setTel(set.getString(7));				
					workers.add(worker);
				}			
			}
			return workers;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String confirmPosition(StoreItemList item){
		int houseId=item.getHouseID();
		int number=item.getNumber();
		String commodityId=item.getCommodityID();
		int providerID = item.getProviderID();
		Date time = new java.sql.Date(System.currentTimeMillis());

		String sql="select * from storeitemlist where houseID="+houseId+" and commodityID='"
		+commodityId+"'"+" and providerID="+providerID;
		System.out.println("hhhhhhhhhhh"+sql);
		PreparedStatement ps=null;
		try {
			ps=conn.prepareStatement(sql);
			ResultSet set=ps.executeQuery();
			boolean isOK=set.next();
			
			System.out.println("---------"+isOK);
			if(isOK){
				
				sql="insert into commoditystocktaking(time,commodityID,exhouseID,actualhouseID,count,providerID) values(?,?,?,?,?,?)";
				
				ps=conn.prepareStatement(sql);
				ps.setDate(1, time);
				ps.setString(2, commodityId);
				ps.setInt(3, houseId);
				ps.setInt(4, houseId);
				ps.setInt(5, number);
				ps.setInt(6, providerID);
				int result=ps.executeUpdate();
				

				if(result>0){
					
					return "商品"+commodityId+"已盘点！";
				}else {
					return "商品"+commodityId+"盘点失败！";
				}
				
				
			}else {
				
				return "库存中不存在该商品！";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "商品"+commodityId+"盘点失败！";
	}
	
    public String alterPosition(StoreItemList item,int newHouse){
    	int houseId=item.getHouseID();
		int number=item.getNumber();
		String commodityId=item.getCommodityID();
		int providerID = item.getProviderID();
		Date time = new java.sql.Date(System.currentTimeMillis());

		String sql="select * from storeitemlist where houseID="+houseId+" and commodityID='"
		+commodityId+"'"+" and providerID="+providerID;
		PreparedStatement ps=null;
		try {
			ps=conn.prepareStatement(sql);
			ResultSet set=ps.executeQuery();
			boolean isOK=set.next();
			if(isOK){
				sql="insert into commoditystocktaking(time,commodityID,exhouseID,actualhouseID,count,providerID) values(?,?,?,?,?,?)";
				ps=conn.prepareStatement(sql);
				ps.setDate(1, time);
				ps.setString(2, commodityId);
				ps.setInt(3, houseId);
				ps.setInt(4, newHouse);
				ps.setInt(5, number);
				ps.setInt(6, providerID);
				int result=ps.executeUpdate();
				if(result>0){
					return "商品"+commodityId+"已盘点！";
				}else {
					return "商品"+commodityId+"盘点失败！";
				}
				
				
			}else {
				return "库存中不存在该商品！";
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "商品"+commodityId+"盘点失败！";
    }
    
    public ArrayList<StoreItemList> getStoreItems(String itemid){
    	String sql = "select * from storeItemList where commodityID = '" + itemid+"'"+" and number>0";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet r = ps.executeQuery();
			ArrayList<StoreItemList> storeitemlists = new ArrayList<StoreItemList>();
			while (r.next()) {
				StoreItemList storeitem = new StoreItemList();
				storeitem.setStoreItemID(r.getInt(1));
				storeitem.setHouseID(r.getInt(2));
				storeitem.setCommodityID(r.getString(3));
				storeitem.setProviderID(r.getInt(4));
				storeitem.setNumber(r.getInt(5));
				storeitemlists.add(storeitem);
			}
			return storeitemlists;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
    }

	@Override
	public ArrayList<VarietyEnum> getUnits() {
		ArrayList<VarietyEnum> unitList=new ArrayList<VarietyEnum>();
		String sql = "select * from varietyenum where state='"+"inuse"+"'";
		PreparedStatement ps=null;
		
		try {
			ps=conn.prepareStatement(sql);
			ResultSet set=ps.executeQuery();
			while(set.next()){
					VarietyEnum varietyEnum=new VarietyEnum();
					varietyEnum.setVarietyID(set.getInt(1));
					varietyEnum.setType(set.getString(2));
					varietyEnum.setVolume(set.getString(4));
					varietyEnum.setTypeName(set.getString(3));
					unitList.add(varietyEnum);
//				}			
			}
			
			return unitList;
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return null;
	}

	@Override
	public String addUnit(VarietyEnum varietyEnum) {
		String sql="select max(varietyID) from varietyenum";
		PreparedStatement ps=null;
		try {
			ps=conn.prepareStatement(sql);
			ResultSet resultSet=ps.executeQuery();
			int ID;
			if(resultSet.next())
				ID=resultSet.getInt(1)+1;
			else {
				ID=2001;
			}
			sql="insert into varietyenum(varietyID,type,volume,unit) values(?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, ID);
			ps.setString(2, varietyEnum.getType());
			ps.setString(4, varietyEnum.getVolume());
			ps.setString(3, varietyEnum.getType());
			int result=ps.executeUpdate();
			if(result>0)
				return "success,规格创建成功";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "failed,规格创建失败！";
	}

	@Override
	public String deleteUnit(String type) {
		// TODO Auto-generated method stub
				boolean inUse=false;
				String sql="select * from varietyenum where type='"+type+"' and state='inuse'";
				PreparedStatement ps=null;

				try {
					ps=conn.prepareStatement(sql);
					ResultSet set=ps.executeQuery();
					if(set.next())
						inUse=true;
					
					sql="select * from housevolume where commodityType='"+type+"'";
					ps=conn.prepareStatement(sql);

					set=ps.executeQuery();
					if(set.next())
						inUse=true;
					System.out.println("/////////"+inUse);
					if(inUse)
						return "规格正在使用中，无法删除！";
					else {
						sql="delete from varietyenum where type="+type;
						ps=conn.prepareStatement(sql);
						int result=ps.executeUpdate();
						if(result>0)
							return "删除规格成功！";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
						
				return "删除规格失败！";
	}



}
