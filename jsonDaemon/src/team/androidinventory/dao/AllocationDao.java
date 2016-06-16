package team.androidinventory.dao;

import org.json.JSONObject;

public interface AllocationDao {
	public boolean allocationLogic(String allocationItemLists);
	public boolean allocationExistenceJudge(String allocationItemLists);
    public JSONObject parseAllocationJson();
    public int getCommodityAllocationID();
	public String commodityAllocation(int allocationID,int workerID);//验证allocationID之后再决定是否调拨
	public String commodityAllocationItem(int commodityAllocationID, String commodityID, int inHouseID, int outHouseID,
			int totalNumber,int providerID);
	public String allocation(int allocationID,int workerID,String allocationItemLists);
	
	
}
