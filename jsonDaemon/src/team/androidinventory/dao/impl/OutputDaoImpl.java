package team.androidinventory.dao.impl;

import java.sql.Date;

import org.json.JSONObject;

import team.androidinventory.dao.OutputDao;

public class OutputDaoImpl implements OutputDao{

	@Override
	public boolean outputJudge(String sellItemLists) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JSONObject parseOutputJson(String sellItemLists) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public int getOutputID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertIntoOutputList(int outputID, int workerID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertIntoSellList(int outputID, String commodityID, int houseID, int customerID, Date time,
			int totalNumber, int providerID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String warseOut(int outputID, int workerID, String storeItemLists) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
