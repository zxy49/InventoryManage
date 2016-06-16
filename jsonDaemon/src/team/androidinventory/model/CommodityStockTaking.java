package team.androidinventory.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="commodityStockTaking")
public class CommodityStockTaking implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int commodityStockTakingID;
	private Date time;
	private String commodityID;
	private int exhouseID;
	private int actualhouseID;
	private int count;
	private int providerID;
	
	public CommodityStockTaking(){
		super();
	}

	public int getCommodityStockTakingID() {
		return commodityStockTakingID;
	}

	public void setCommodityStockTakingID(int commodityStockTakingID) {
		this.commodityStockTakingID = commodityStockTakingID;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getCommodityID() {
		return commodityID;
	}

	public void setCommodityID(String commodityID) {
		this.commodityID = commodityID;
	}

	public int getExhouseID() {
		return exhouseID;
	}

	public void setExhouseID(int exhouseID) {
		this.exhouseID = exhouseID;
	}

	public int getActualhouseID() {
		return actualhouseID;
	}

	public void setActualhouseID(int actualhouseID) {
		this.actualhouseID = actualhouseID;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getProviderID() {
		return providerID;
	}
	public void setProviderID(int providerID) {
		this.providerID = providerID;
	}
	
}
