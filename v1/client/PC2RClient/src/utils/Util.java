package utils;

import java.util.LinkedList;
import java.util.List;

public class Util {
	public static Point2D getValueInCoord(String coord){
		int yPos = coord.indexOf("Y");
		
		double x = Double.parseDouble(coord.substring(1, yPos-1));
		double y = Double.parseDouble(coord.substring(yPos+1, coord.length()-1));
		
		return new Point2D(x,y);
	}

	public static List<Pair<String, Point2D>> getValueInCoords(String coords) {//A TESTER
		List<Pair<String,Point2D>> list = new LinkedList<Pair<String, Point2D>>();
		StringBuilder s = new StringBuilder(coords);
		int sepPos, twoPointPos;
		System.out.println("READING");
		System.out.println(s);
		// get name and coord for n-1 players
		while((sepPos=s.indexOf("|"))!= -1) {
			twoPointPos = s.indexOf(":");
			String name = s.substring(0, twoPointPos);
			System.out.println("read "+name);
			Point2D coord = Util.getValueInCoord(s.substring(twoPointPos+1, sepPos-1));
			list.add(new Pair<String, Point2D>(name,coord));
			s.delete(0, sepPos+1);
		}
		
		// get name and coord for nth player
		twoPointPos = s.indexOf(":");
		String name = s.substring(0, twoPointPos);
		Point2D coord = Util.getValueInCoord(s.substring(twoPointPos+1, s.length()-1));
		list.add(new Pair<String, Point2D>(name,coord));
		
		return list;
	}
}
