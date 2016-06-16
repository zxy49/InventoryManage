package team.androidinventory.model;

public class HouseVolume {

	private int houseID;
	private String commodityType;
	private int maxNum;
	private int currentUsefulNum;
	
	public int getHouseID(){
		return this.houseID;
	}
	
	public void setHouseID(int houseID){
		this.houseID = houseID;
	}
	
	public String getCommodityType(){
		return this.commodityType;
	}
	
	public void setCommodityType(String commodityType){
		this.commodityType = commodityType;
	}
	
	public int getMaxNum(){
		return this.maxNum;
	}
	
	public void setMaxNum(int maxNum){
		this.maxNum = maxNum;
	}
	
	public int getCurrentUsefulNum(){
		return this.currentUsefulNum;
	}
	
	public void setCurrentUsefulNum(int currentUsefulNum){
		this.currentUsefulNum = currentUsefulNum;
	}
}
