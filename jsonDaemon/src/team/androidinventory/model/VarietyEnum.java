package team.androidinventory.model;

public class VarietyEnum {
    private int varietyID;
	private String type;
	private String typeName;
	private String volume;
	private String state;
	
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
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
	
	public String getVolume(){
		return this.volume;
	}
	
	public void setVolume(String volume){
		this.volume = volume;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
