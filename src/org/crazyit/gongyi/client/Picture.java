package org.crazyit.gongyi.client;

public class Picture {
	private int picture[]=new int[]{R.drawable.img1,R.drawable.img2};
	
	public Picture(){}
	
	public int getPicture(int num) {
		return picture[num];
		
	}


}
