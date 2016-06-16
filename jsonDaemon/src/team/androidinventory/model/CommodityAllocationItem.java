package team.androidinventory.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="commodityAllocationItem")
public class CommodityAllocationItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int commodityAllocationID;
	private int commodityAllocationItemID;
	private String commodityID;
	private int inHouseID;
	private int outHouseID;
	private Date time;
	private int totalNumber;
	private int providerID;
	
	public CommodityAllocationItem(){
		super();
	}

	public int getCommodityAllocationID() {
		return commodityAllocationID;
	}

	public void setCommodityAllocationID(int commodityAllocationID) {
		this.commodityAllocationID = commodityAllocationID;
	}
	
	public int getCommodityAllocationItemID() {
		return commodityAllocationItemID;
	}

	public void setCommodityAllocationItemID(int commodityAllocationItemID) {
		this.commodityAllocationItemID = commodityAllocationItemID;
	}

	public String getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(String commodityID) {
		this.commodityID = commodityID;
	}

	public int getInHouseID() {
		return inHouseID;
	}

	public void setInHouseID(int inHouseID) {
		this.inHouseID = inHouseID;
	}

	public int getOutHouseID() {
		return outHouseID;
	}

	public void setOutHouseID(int outHouseID) {
		this.outHouseID = outHouseID;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public int getProviderID() {
		return providerID;
	}
	public void setProviderID(int providerID) {
		this.providerID = providerID;
	}
	
}
