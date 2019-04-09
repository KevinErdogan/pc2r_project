package network;

import java.util.List;

public interface NetworkObserver {
	public abstract void notify(List<NetworkEvent> events);
}
