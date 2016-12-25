package team.androidinventory.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="storeItemList")
public class StoreItemLists implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int storeItemID;
	private int houseID;
	private String commodityID;
	private int providerID;
	private int number;
	
	public StoreItemLists(){
		super();
	}

	public int getStoreItemID() {
		return storeItemID;
	}

	public void setStoreItemID(int storeItemID) {
		this.storeItemID = storeItemID;
	}

	public int getHouseID() {
		return houseID;
	}

	public void setHouseID(int houseID) {
		this.houseID = houseID;
	}

	public String getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(String commodityID) {
		this.commodityID = commodityID;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getProviderID() {
		return providerID;
	}
	public void setProviderID(int providerID) {
		this.providerID = providerID;
	}


}
