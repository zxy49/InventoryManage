package team.androidinventory.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import team.androidinventory.model.Worker;

public class Test {

	public static void main(String[] args){
//		WorkerDaoImpl wo = new WorkerDaoImpl();
//		Worker w = wo.login(123, "000");
//		System.out.println(wo.login(10000001, "111111").getName());
//		Utils ut = new Utils();
//		System.out.println(ut.currentHouseAvailableVolume(1001));
//		int inputID = wo.getInputID();
////		wo.waresIn(inputID, 10000001, jsonArray)
//		JSONArray ja = new JSONArray();
//		JSONObject stockitem = new JSONObject();
//		try {
//			stockitem.put("commodityID",1234567890);
//			stockitem.put("houseID",1001);
//			stockitem.put("totalNumber",300);
//			stockitem.put("providerID",20000001);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ja.put(stockitem);
//		System.out.println(wo.waresIn(inputID, 10000001, ja));
//		Connection conn = DBUtil.getConnForMySql();
//		String sql = "select * from house where houseID = " + 1001;
//		PreparedStatement ps = null;
//		try {
//			ps = conn.prepareStatement(sql);
//			ResultSet r = ps.executeQuery();
//			if (r.next()) {
//				System.out.println(r.getInt(1));
//			}else
//				System.out.println("gg");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		System.out.println(wo.commodityAllocation(10000002));
//        System.out.println(wo.commodityAllocationItem(300000001, "1234567890", 1001, 1002, 70));
// 		ArrayList<Integer> list = new ArrayList();
// 		int i =10;
// 		while(i>0){
// 			list.add(1);
// 			i--;
// 		}
// 		i=10;
// 		while(i>0){
// 			list.add(2);
// 			i--;
// 		}
// 		System.out.println(list.size());
// 		list = RemoveSame(list);
// 		System.out.println(list.size());
	}
	
// 	private static ArrayList<Integer> RemoveSame(ArrayList<Integer> list)
//     {
//         //上面写的那句是多余的，这个是最终的  
//         for (int i = 0; i < list.size(); i++)
//         {
//             for (int j = i + 1; j < list.size(); j++)
//             {
//                 if (list.get(i)==list.get(j))
//                 {
//                     list.remove(j);
//                     j--;
//                 }
//             }
//         }
//         return list;
//     }
}
