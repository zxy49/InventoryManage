package team.androidinventory.dao;

import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

import team.androidinventory.dao.impl.DBUtil;

public interface InputDao {
	public String inputJudge(JSONArray stockArray);
    public JSONObject parseInputJson(String storeItemLists);
	public int getInputID();//验证冲突
	public int insertIntoInputList(int inputID,int workerID);
	public int insertIntoStockList(int inputID, String commodityID, int houseID, int providerID, int time,
			int totalNumber);
	public String warseIn(String storeItemLists);
}
