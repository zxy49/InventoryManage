package team.androidinventory.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="house")
public class House implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int houseID;
	private String name;
	private String state;
	private boolean isAvailable;
	
	
	public House(){
		super();
	}

	public int getHouseID() {
		return houseID;
	}

	public void setHouseID(int houseID) {
		this.houseID = houseID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public boolean getIsAvailable(){
		return isAvailable;
	}
	
	public void setIsAvailable(boolean isAvailable){
		this.isAvailable=isAvailable;
	}

	

}
