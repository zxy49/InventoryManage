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

import team.androidinventory.dao.OutputDao;
import team.androidinventory.model.HouseVolume;
import team.androidinventory.model.StoreItemList;

public class OutputDaoImpl implements OutputDao {
	private DBUtil dbu = new DBUtil();
	private Connection conn = dbu.getConnForMySql();
	private ArrayList<HouseVolume> alhv = new ArrayList<HouseVolume>();
	private HouseVolumeDaoImpl hvdi = new HouseVolumeDaoImpl();
	private VarietyEnumDaoImpl vedi = new VarietyEnumDaoImpl();
	private StoreItemDaoImpl sidi = new StoreItemDaoImpl();
	private ArrayList<StoreItemList> alsil = new ArrayList<StoreItemList>();
	
	

	@Override
	public String outputJudge(JSONArray sellArray) {
		String result = "";
		Utils utils = new Utils();
		String commodityExist = "";
		boolean ce = false;
		String providerExist = "";
		boolean pe = false;
		String houseExist = "";
		String customerExist = "";
		boolean CE = false;
		boolean he = false;
		boolean te = false;
		ArrayList<Integer> houseIDs = new ArrayList<Integer>();
		try {
			for (int i = 0; i < sellArray.length(); i++) {// 存在性判断
				JSONObject sellItem = sellArray.getJSONObject(i);
				String commodityID = sellItem.getString("commodityID");
				commodityExist = utils.isCommodityExist(commodityID);
				if (commodityExist.contains("不存在")) {
					ce = true;
					result += commodityExist + "\n";
				}
				int houseID = sellItem.getInt("houseID");
				houseExist = utils.isHouseExist(houseID);
				if (houseExist.contains("不存在")) {
					he = true;
					result += houseExist + "\n";
				} else {
					houseIDs.add(houseID);
				}
				int providerID = sellItem.getInt("providerID");
				providerExist = utils.isProviderExist(providerID);
				if (providerExist.contains("不存在")) {
					pe = true;
					result += providerExist + "\n";
				}
				int customerID = sellItem.getInt("customerID");
				customerExist = utils.isCustomerExist(customerID);
				if (customerExist.contains("不存在")) {
					CE = true;
					result += customerExist + "\n";
				}
			}
			boolean alhvbl = false;// 仓库-类型对是否存在
			hvdi = new HouseVolumeDaoImpl();
			vedi = new VarietyEnumDaoImpl();
			sidi = new StoreItemDaoImpl();
			alsil = new ArrayList<StoreItemList>();
			alhv = new ArrayList<HouseVolume>();
			houseIDs = utils.RemoveSame(houseIDs);
			if (houseIDs.size() > 0 && he == false) {// 添加houseVolume类型
				Set set = new HashSet(alhv);
				for (int i = 0; i < houseIDs.size(); i++) {
					alhv = hvdi.getHouseVolume(houseIDs.get(i));// 可优化为只提供当前要求的仓库-类型对
					if (alhv.size() > 0 && alhv.get(i).getCurrentUsefulNum() <= alhv.get(i).getMaxNum()) {
						set.addAll(new HashSet(alhv));
					} else {
						alhvbl = true;
						result += utils.getHouseName(houseIDs.get(i)) + "存在库存状况异常，可用库存位超出已有库存位 \n";
					}
				}
				alhv = new ArrayList<HouseVolume>(set);
			}
			if (alhv.size() <= 0) {
				alhvbl = true;
			}
			// 判断能否出库
			boolean numDiff = false;
			if (ce == false && pe == false && he == false && CE == false && alhvbl == false) {
				for (int i = 0; i < sellArray.length(); i++) {
					JSONObject stockitem = sellArray.getJSONObject(i);
					String commodityID = stockitem.getString("commodityID");
					int providerID = stockitem.getInt("providerID");
					int houseID = stockitem.getInt("houseID");
					int totalNumber = stockitem.getInt("totalNumber");
					// 暂时不考虑容量判断，只考虑出库库存量判断
					boolean isSellItemRepeat = false;
					for (int j = 0; j < alsil.size(); j++) {
						if (commodityID.equals(alsil.get(j).getCommodityID())
								&& providerID == alsil.get(j).getProviderID() && houseID == alsil.get(j).getHouseID()) {
							isSellItemRepeat = true;
							break;
						}
					}
					if (!isSellItemRepeat) {
						StoreItemDaoImpl sidi = new StoreItemDaoImpl();
						StoreItemList si = new StoreItemList();
						si.setCommodityID(commodityID);
						si.setHouseID(houseID);
						si.setProviderID(providerID);
						si.setNumber(si.getNumber() - totalNumber);
						alsil.add(si);
					}
				}

				// 库存项判断
				for (int j = 0; j < alsil.size(); j++) {
					StoreItemList si = alsil.get(j);
					int number = si.getNumber();
					if (number >= 0) {
					} else {
						int providerID = si.getProviderID();
						int houseID = si.getHouseID();
						String commodityID = si.getCommodityID();
						int totalNumber = si.getNumber();
						String houseName = utils.getHouseName(houseID);
						String commodityName = utils.getCommodityName(commodityID);
						result += "仓库" + houseName + "由" + providerID + "供应的" + commodityName + "数量小于" + totalNumber
								+ ",不足以出库 \n";
						numDiff = true;
						break;
					}
				}
				// houseVolume判断
				for (int i = 0; i < sellArray.length(); i++) {
					if (numDiff) {
						break;
					}
					JSONObject stockitem = sellArray.getJSONObject(i);
					String commodityID = stockitem.getString("commodityID");
					int providerID = stockitem.getInt("providerID");
					int houseID = stockitem.getInt("houseID");
					int totalNumber = stockitem.getInt("totalNumber");
					String commodityType = hvdi.getCommodityType(commodityID);
					

					for (int j = 0; j < alhv.size(); j++) {
						HouseVolume hv = alhv.get(j);
						if (commodityType.equals(hv.getCommodityType()) && houseID == hv.getHouseID()) {
							int maxNum = hv.getMaxNum();
							int currentUsefulNum = hv.getCurrentUsefulNum();
							int diff = currentUsefulNum + totalNumber;
							if (diff > maxNum) {
								String commodityTypeName = vedi.getTypeName(commodityType);
								String houseName = utils.getHouseName(houseID);
								String commodityName = utils.getCommodityName(commodityID);
								result += "仓库" + houseName + "的" + commodityTypeName + "类型在商品"+commodityName+"出库后将超出最大容量，请检查仓库容量设置 \n";
								numDiff = true;
								break;
							} else {
								alhv.get(j).setCurrentUsefulNum(diff);
							}
							break;
						}
					}
				}
				for (int i = 0; i < alsil.size(); i++) {
					if (alsil.get(i).getNumber() < 0) {
						numDiff = true;
						result += "错误类型1：出库失败,存在商品数量不足";
						break;
					}
				}
				for (int i = 0; i < alhv.size(); i++) {
					if (alhv.get(i).getCurrentUsefulNum()>alhv.get(i).getMaxNum()) {
						numDiff = true;
						result += "错误类型2：出库失败,仓库容量发生异常";
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("错误所在位置：OutputDaoImpl.outputJudge(JSONArray)");
		}
		return result;
	}

	@Override
	public JSONObject parseOutputJson(String sellItemLists) {
		JSONObject outputObject = null;
		try {
			outputObject = new JSONObject(sellItemLists);
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("错误所在位置：OutputDaoDaoImpl.parseOutputJson(String)");
		}
		return outputObject;
	}

	@Override
	public int getOutputID() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select max(inputID) from outputlist";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("错误所在位置：InputDaoDaoImpl.getInputID()");
		}
		int outputID = 0;
		if (rs != null) {
			try {
				rs.next();
				outputID = rs.getInt(1) + 1;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("错误所在位置：InputDaoDaoImpl.getInputID()");
			}
		} else {
			outputID = OperationUtil.outputStartNumber + 1;
		}
		dbu.CloseResources(rs, ps);
		return outputID;
	}

	@Override
	public int insertIntoOutputList(int outputID, int workerID) {
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

	@Override
	public int insertIntoSellList(int outputID, String commodityID, int houseID, int customerID, Date time,
			int totalNumber, int providerID) {
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
	public String warseOut(String sellItemLists) {
		JSONObject jsonObject = this.parseOutputJson(sellItemLists);
		try {
			int workerID = jsonObject.getInt("workerID");
			JSONArray stockArray = jsonObject.getJSONArray("sellItems");
			String existenceJudge = this.outputJudge(stockArray);
			if (existenceJudge.contains("错误类型")) {
				return existenceJudge;
			} else {
				int targetOutputID = this.getOutputID();
//				System.out.println(targetOutputID);
				this.insertIntoOutputList(targetOutputID, workerID);
				for (int i = 0; i < stockArray.length(); i++) {
					JSONObject stockitem = stockArray.getJSONObject(i);
					String commodityID = stockitem.getString("commodityID");
					int houseID = stockitem.getInt("houseID");
					int customerID = stockitem.getInt("customerID");
					int providerID = stockitem.getInt("providerID");
					Date time = new java.sql.Date(System.currentTimeMillis());
					int totalNumber = stockitem.getInt("totalNumber");
					this.insertIntoSellList(targetOutputID, commodityID, houseID, customerID, time, totalNumber, providerID);
				}
				for (int i = 0; i < alhv.size(); i++) {
					hvdi.updateHouseVolume(alhv.get(i));
				}
				for (int i = 0; i < alsil.size(); i++) {
					sidi.updateStoreItemList(alsil.get(i));
				}
				System.out.println("出库成功");
			}
		} catch (Exception e) {
			System.out.println("错误所在位置：OutputDaoImpl.warseIn(String)");
		}
		return "出库失败";
	}

}
