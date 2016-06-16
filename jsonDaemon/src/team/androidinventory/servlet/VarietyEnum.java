package team.androidinventory.servlet;

public class VarietyEnum {
    private int varietyID;
	private String type;
	private int volume;
	private String unit;
	
	public int getVarietyID(){
		return this.varietyID;
	}
	
	public void setVarietyID(int varietyID){
		this.varietyID = varietyID;
	}
	
	public String getType(){
		return this.type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public int getVolume(){
		return this.volume;
	}
	
	public void setVolume(int volume){
		this.volume = volume;
	}
	
	public String getUnit(){
		return this.unit;
	}
	
	public void setUnit(String unit){
		this.unit = unit;
	}
}
