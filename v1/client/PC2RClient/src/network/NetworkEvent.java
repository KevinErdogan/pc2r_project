package network;

import java.util.LinkedList;
import java.util.List;

public class NetworkEvent {
	public static enum NetType{
		DENIED, PLAYERLEFT, NEWPLAYER, NEWSESSION, WINNER, TICK, NEWOBJ, WELCOMEGAME, WELCOMEWAIT
	}
	
	private NetType type;
	private List<Object> parameters;
	
	public NetworkEvent(NetType t, Object ... params) {
		type=t;
		parameters = new LinkedList<Object>();
		for(Object o : params) {
			parameters.add(o);
		}
	}
	
	public NetType getNetType() {
		return type;
	}
	
	public List<Object> getParameters(){
		return parameters;
	}
	
}
