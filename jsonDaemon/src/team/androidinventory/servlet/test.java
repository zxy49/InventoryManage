package team.androidinventory.servlet;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jndi.cosnaming.IiopUrl.Address;

import team.androidinventory.model.House;

public class test {

	private static String address="http://172.19.114.232:8080/jsonDaemon/gethouses";
	
	public static void main(String[] args) {
		ArrayList<Integer> alist=new ArrayList<Integer>();
		ArrayList<Integer> blist=null;

		boolean a=alist.isEmpty();
		boolean b=alist==null;
//		boolean c=blist.isEmpty();
		boolean d=blist==null;
		System.out.println(a);
		System.out.println(b);
//		System.out.println(c);
		System.out.println(d);

		
//		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
//            @Override
//            public void onFinish(String response) {
//                System.out.println(response);
//                try {
//                    JSONObject object=new JSONObject(response);
//                    JSONArray array=object.getJSONArray("house_list");
//                    for(int i=0;i<array.length();i++){
//                        JSONObject item=array.getJSONObject(i);
//                        int id=item.getInt("house_id");
//                        String name=item.getString("house_name");
//                        String state=item.getString("house_state");
//                        House house=new House();
//                        house.setHouseID(id);
//                        house.setName(name);
//                        house.setState(state);
//                        System.out.println("-----------"+house.getState().toString());
//                        boolean b="inuse".equals(house.getState());
//                        System.out.println(b);
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//                
//            }
//        });
	}
}
