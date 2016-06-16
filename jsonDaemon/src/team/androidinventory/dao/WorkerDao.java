package team.androidinventory.dao;

import java.sql.Date;
import java.util.ArrayList;

import org.json.JSONArray;

import team.androidinventory.model.House;
import team.androidinventory.model.HouseVolume;
import team.androidinventory.model.SellList;
import team.androidinventory.model.StockList;
import team.androidinventory.model.StoreItemList;
import team.androidinventory.model.VarietyEnum;
import team.androidinventory.model.Worker;



public interface WorkerDao {
	//登录操作
	public Worker login(int id, String password);
	//入库操作
    public String waresIn(int inputID,int workerID,JSONArray jsonArray);
	//获取入库单号
	public int getInputID();
	//获取出库单号
	public int getOutputID();
	//获取商品调拨单号
	public int getCommodityAllocationID();
	//出库操作
	public String waresOut(int outputID,int workerID,JSONArray jsonArray);
    //商品查询
    public ArrayList<StoreItemList> commodityQuery(String commodityID);
    //仓库查询
    public ArrayList<StoreItemList> houseQuery(int houseID);
    //删除仓库
    public String deleteHouse(int houseID);
    //商品管理
    public String commodityModify(String commodityID,String name, String type,double price);
    //账户设置
    public boolean set(int workerID, String name, String address, String tel);
    //修改密码
    public boolean changePassword(int workerID, String oldpassword,String newpassword);
    //商品调拨
    public String commodityAllocation(int workerID);
    public String commodityAllocationItem(int commodityAllocationID,String commodityID,int inHouseID,int outHouseID,int totalNumber,int providerID);
    //获取仓库列表
    public ArrayList<House> getHouseList();
    //增加用户
    public String addWorker(Worker worker);
    //删除用户
    public String deleteWorker(int id);
    //获取用户列表
    public ArrayList<Worker> getWorkerList();
    
    public String confirmPosition(StoreItemList item);
    public String alterPosition(StoreItemList item,int newHouse);
    public ArrayList<StoreItemList> getStoreItems(String itemid) ;
	ArrayList<VarietyEnum> getUnits();
	String addUnit(VarietyEnum varietyEnum);
	String deleteUnit(String type);
	String addHouse(String housename, ArrayList<HouseVolume> list);

}
