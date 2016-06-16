package team.androidinventory.dao;

import java.sql.Connection;
import java.sql.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import team.androidinventory.dao.impl.DBUtil;

public interface OutputDao {
	public String outputJudge(JSONArray sellArray);
    public JSONObject parseOutputJson(String sellItemLists);
	public int getOutputID();//验证冲突
	public int insertIntoOutputList(int outputID,int workerID);
	public int insertIntoSellList(int outputID, String commodityID, int houseID, int customerID, Date time,
			int totalNumber,int providerID);
	public String warseOut(String sellItemLists);
}