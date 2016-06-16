package team.androidinventory.test;

import java.util.ArrayList;

import team.androidinventory.dao.impl.Utils;
import team.androidinventory.dao.impl.VarietyEnumDaoImpl;

public class VarietyEnumTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		VarietyEnumDaoImpl vedi = new VarietyEnumDaoImpl();
//		System.out.println(vedi.getTypeName("A"));
//		System.out.println(vedi.getTypeName("A"));
//		System.out.println(vedi.getTypeName("A"));
//		System.out.println(vedi.getTypeName("A"));
//		System.out.println(vedi.getTypeName("A"));
		ArrayList<Integer> houseIDs = new ArrayList<Integer>();
		for(int i = 0;i<10;i++){
			houseIDs.add(1001);
		}
		Utils utils = new Utils();
		utils.RemoveSame(houseIDs);
		System.out.println(houseIDs.size());
	}

}
