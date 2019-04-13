package utils;

import java.util.LinkedList;
import java.util.List;

public class Util {
	public static Point2D getValueInCoord(String coord){
		int yPos = coord.indexOf("Y");
		
		double x = Double.parseDouble(coord.substring(1, yPos));
		double y = Double.parseDouble(coord.substring(yPos+1, coord.length()));
		
		return new Point2D(x,y);
	}

	public static List<Pair<String, Point2D>> getValueInCoords(String coords) {//A TESTER
		List<Pair<String,Point2D>> list = new LinkedList<Pair<String, Point2D>>();
		StringBuilder s = new StringBuilder(coords);
		int sepPos, twoPointPos;
		// get name and coord for n-1 players
		while((sepPos=s.indexOf("|"))!= -1) {
			twoPointPos = s.indexOf(":");
			String name = s.substring(0, twoPointPos);
			Point2D coord = Util.getValueInCoord(s.substring(twoPointPos+1, sepPos));
			list.add(new Pair<String, Point2D>(name,coord));
			s.delete(0, sepPos+1);
		}
		
		// get name and coord for nth player
		twoPointPos = s.indexOf(":");
		String name = s.substring(0, twoPointPos);
		Point2D coord = Util.getValueInCoord(s.substring(twoPointPos+1, s.length()));
		list.add(new Pair<String, Point2D>(name,coord));
		
		return list;
	}
	
	public static List<Pair<String, Integer>> getValueInScores(String scores) {
		List<Pair<String,Integer>> list = new LinkedList<Pair<String, Integer>>();
		StringBuilder s = new StringBuilder(scores);
		int sepPos, twoPointPos;
		// get name and coord for n-1 players
		while((sepPos=s.indexOf("|"))!= -1) {
			twoPointPos = s.indexOf(":");
			String name = s.substring(0, twoPointPos);
			Integer score = Integer.parseInt(s.substring(twoPointPos+1, sepPos));
			list.add(new Pair<String, Integer>(name, score));
			s.delete(0, sepPos+1);
		}
		
		// get name and coord for nth player
		twoPointPos = s.indexOf(":");
		String name = s.substring(0, twoPointPos);
		Integer score = Integer.parseInt(s.substring(twoPointPos+1, s.length()));
		list.add(new Pair<String, Integer>(name, score));
		
		return list;
	}

	public static List<Pair<String, Point2D>> getValueInVcoords(String vcoords) {
		List<Pair<String,Point2D>> list = new LinkedList<Pair<String, Point2D>>();
		StringBuilder s = new StringBuilder(vcoords);
		int sepPos, twoPointPos;
		// get name and coord for n-1 players
		while((sepPos=s.indexOf("|"))!= -1) {
			twoPointPos = s.indexOf(":");
			String name = s.substring(0, twoPointPos);
			int vX = s.indexOf("VX");
			Point2D pos =  Util.getValueInCoord(s.substring(twoPointPos+1, vX));
			Util.getVandAngle(s.substring(vX, sepPos), pos);
			list.add(new Pair<String, Point2D>(name, pos));
			s.delete(0, sepPos+1);
		}
		
		// get name and coord for nth player
		twoPointPos = s.indexOf(":");
		String name = s.substring(0, twoPointPos);
		int vX = s.indexOf("VX");
		Point2D pos =  Util.getValueInCoord(s.substring(twoPointPos+1, vX));
		Util.getVandAngle(s.substring(vX, s.length()), pos);
		list.add(new Pair<String, Point2D>(name, pos));
		
		return list;
	}
	
	
	public static void getVandAngle(String s, Point2D pos){
		//vx, vx, angle
		int vyPos = s.indexOf("VY");
		int tPos = s.indexOf("T");
		
		double vx = Double.parseDouble(s.substring(2, vyPos));
		double vy = Double.parseDouble(s.substring(vyPos+2, tPos));
		double t = Double.parseDouble(s.substring(tPos+1, s.length()));
		
		pos.vX=vx;
		pos.vY=vy;
		pos.angle=t;
	}
}
