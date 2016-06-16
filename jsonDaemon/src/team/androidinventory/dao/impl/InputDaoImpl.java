package team.androidinventory.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import team.androidinventory.dao.InputDao;
import team.androidinventory.model.HouseVolume;
import team.androidinventory.model.StoreItemList;

public class InputDaoImpl implements InputDao {
	private DBUtil dbu = new DBUtil();
	private Connection conn = dbu.getConnForMySql();
	private ArrayList<HouseVolume> alhv = new ArrayList<HouseVolume>();
	private HouseVolumeDaoImpl hvdi = new HouseVolumeDaoImpl();
	private VarietyEnumDaoImpl vedi = new VarietyEnumDaoImpl();
	private StoreItemDaoImpl sidi = new StoreItemDaoImpl();
	private ArrayList<StoreItemList> alsil = new ArrayList<StoreItemList>();

	public void closeConn() {
		// dbu.CloseResources(conn);
	}

	@Override
	public String inputJudge(JSONArray stockArray) {
		// TODO Auto-generated method stub
		String result = "";
		Utils utils = new Utils();
		String commodityExist = "";
		boolean ce = false;
		String providerExist = "";
		boolean pe = false;
		String houseExist = "";
		boolean he = false;
		boolean te = false;
		ArrayList<Integer> houseIDs = new ArrayList<Integer>();
		try {
			for (int i = 0; i < stockArray.length(); i++) {// 存在性判断
				JSONObject stockItem = stockArray.getJSONObject(i);
				String commodityID = stockItem.getString("commodityID");
				commodityExist = utils.isCommodityExist(commodityID);
				if (commodityExist.contains("不存在")) {
					ce = true;
					result += commodityExist + "\n";
				}
				int houseID = stockItem.getInt("houseID");
				houseExist = utils.isHouseExist(houseID);
				if (houseExist.contains("不存在")) {
					he = true;
					result += houseExist + "\n";
				} else {
					houseIDs.add(houseID);
				}
				int providerID = stockItem.getInt("providerID");
				providerExist = utils.isProviderExist(providerID);
				if (providerExist.contains("不存在")) {
					pe = true;
					result += providerExist + "\n";
				}
				int time = stockItem.getInt("time");
				if (time <= 0) {
					te = true;
					result += commodityID + "存储期限错误 \n";
				}
			}

			// 仓库、商品、供应商ID-姓名对
			boolean alhvbl = false;// 仓库-类型对是否存在
			hvdi = new HouseVolumeDaoImpl();
			vedi = new VarietyEnumDaoImpl();
			sidi = new StoreItemDaoImpl();
			alsil = new ArrayList<StoreItemList>();
			alhv = new ArrayList<HouseVolume>();
			houseIDs = utils.RemoveSame(houseIDs);
//			System.out.println("houseIDs.size:" + houseIDs.size());
			if (houseIDs.size() > 0 && he == false) {// 添加houseVolume类型
				Set set = new HashSet(alhv);
				for (int i = 0; i < houseIDs.size(); i++) {
					alhv = hvdi.getHouseVolume(houseIDs.get(i));// 可优化为只提供当前要求的仓库-类型对
					if (alhv.size() > 0) {
						set.addAll(new HashSet(alhv));
					} else {
						alhvbl = true;
						result += utils.getHouseName(houseIDs.get(i)) + "不存在可存储空间 \n";
					}
				}
				alhv = new ArrayList<HouseVolume>(set);
			}
			if (alhv.size() <= 0) {
				alhvbl = true;
			}

			// 下面开始判断能否入库
			boolean numDiff = false;
			if (ce == false && pe == false && he == false && te == false && alhvbl == false) {
				for (int i = 0; i < stockArray.length(); i++) {
					if (numDiff) {
						break;
					}
					JSONObject stockitem = stockArray.getJSONObject(i);
					int houseID = stockitem.getInt("houseID");
					String houseName = utils.getHouseName(houseID);
					String commodityID = stockitem.getString("commodityID");
					String commodityName = utils.getCommodityName(commodityID);
					int totalNumber = stockitem.getInt("totalNumber");
					String commodityType = hvdi.getCommodityType(commodityID);
					String commodityTypeName = vedi.getTypeName(commodityType);
					int providerID = stockitem.getInt("providerID");
					boolean isSellItemRepeat = false;
					for (int j = 0; j < alsil.size(); j++) {// 添加storeItem类型-1
						if (commodityID.equals(alsil.get(j).getCommodityID())
								&& providerID == alsil.get(j).getProviderID() && houseID == alsil.get(j).getHouseID()) {
							isSellItemRepeat = true;
							alsil.get(j).setNumber(totalNumber + alsil.get(j).getNumber());
							break;
						}
					}
					if (!isSellItemRepeat) {// 添加storeItem类型-2
						StoreItemDaoImpl sidi = new StoreItemDaoImpl();
						StoreItemList si = new StoreItemList();
						si.setCommodityID(commodityID);
						si.setHouseID(houseID);
						si.setProviderID(providerID);
						si = sidi.getStoreItemList(si);
						si.setNumber(si.getNumber() + totalNumber);
						if (si.getNumber() >= 0) {
							alsil.add(si);
						} else {
							System.out.println("错误所在位置：StoreItemDaoImpl.getStoreItemList(StoreItemList)");
							break;
						}
					}
					for (int j = 0; j < alhv.size(); j++) {
						HouseVolume hv = alhv.get(j);
						if (commodityType.equals(hv.getCommodityType()) && houseID == hv.getHouseID()) {
							int maxNum = hv.getMaxNum();
							int currentUsefulNum = hv.getCurrentUsefulNum();
							int diff = currentUsefulNum - totalNumber;
							if (diff < 0) {
								result += "仓库" + houseName + "的" + commodityTypeName + "类型无法容纳" + commodityName + " \n";
								numDiff = true;
								break;
							} else {
								this.alhv.get(j).setCurrentUsefulNum(diff);
							}
							break;
						}
					}
				}
				for (int i = 0; i < alhv.size(); i++) {
					if (alhv.get(i).getCurrentUsefulNum() < 0) {
						numDiff = true;
						result += "错误类型1：入库失败,仓库容量不足";
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("错误所在位置：InputDaoImpl.inputJudge(JSONArray)");
		}
		return result;
	}

	@Override
	public JSONObject parseInputJson(String storeItemLists) {
		// TODO Auto-generated method stub
		JSONObject inputObject = null;
		try {
			inputObject = new JSONObject(storeItemLists);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("错误所在位置：InputDaoImpl.parseInputJson(String)");
		}
		return inputObject;
	}

	@Override
	public int getInputID() {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select max(inputID) from inputlist";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("错误所在位置：InputDaoImpl.getInputID()");
		}
		int inputID = 0;
		if (rs != null) {
			try {
				rs.next();
				inputID = rs.getInt(1) + 1;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("错误所在位置：InputDaoImpl.getInputID()");
			}
		} else {
			inputID = OperationUtil.inputStartNumber + 1;
		}
		dbu.CloseResources(rs, ps);
		// dbu.CloseResources(conn);
		return inputID;
	}

	@Override
	public int insertIntoInputList(int inputID, int workerID) {
		// TODO Auto-generated method stub
		// 返回类型定义
		// 0，插入stocklist失败
		// 1，插入stocklist成功
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
				// dbu.CloseResources(conn);
				return 1;
			}
		} catch (Exception e) {
			System.out.println("错误所在位置：InputDaoImpl.insertIntoInputList(int,int)");
		}
		dbu.CloseResources(ps);
		// dbu.CloseResources(conn);
		return 0;
	}

	@Override
	public int insertIntoStockList(int inputID, String commodityID, int houseID, int providerID, int time,
			int totalNumber) {
		// TODO Auto-generated method stub
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
				// dbu.CloseResources(conn);
				return 1;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("错误所在位置：InputDaoImpl.insertIntoStockList(...)");
		}
		dbu.CloseResources(ps);
		// dbu.CloseResources(conn);
		return 0;
	}

	@Override
	public String warseIn(String storeItemLists) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = this.parseInputJson(storeItemLists);
		try {
			int workerID = jsonObject.getInt("workerID");
			JSONArray stockArray = jsonObject.getJSONArray("stockitems");
			String existenceJudge = this.inputJudge(stockArray);
			if (existenceJudge.contains("错误类型")) {
				return existenceJudge;
			} else {
				int targetInputID = this.getInputID();
				System.out.println(targetInputID);
				this.insertIntoInputList(targetInputID, workerID);
				for (int i = 0; i < stockArray.length(); i++) {
					JSONObject stockitem = stockArray.getJSONObject(i);
					String commodityID = stockitem.getString("commodityID");
					int houseID = stockitem.getInt("houseID");
					int providerID = stockitem.getInt("providerID");
					int time = stockitem.getInt("time");
					int totalNumber = stockitem.getInt("totalNumber");
					this.insertIntoStockList(targetInputID, commodityID, houseID, providerID, time, totalNumber);
				}
				for (int i = 0; i < alhv.size(); i++) {
					hvdi.updateHouseVolume(alhv.get(i));
				}
				for (int i = 0; i < alsil.size(); i++) {
					sidi.updateStoreItemList(alsil.get(i));
				}
				System.out.println("入库成功");
			}
		} catch (Exception e) {
			System.out.println("错误所在位置：InputDaoImpl.warseIn(String)");
		}
		return "入库失败";
	}

	public void closeInputDao() {
		this.dbu.CloseResources(this.conn);
	}

}
