package org.jaredstevens.servers.db.interfaces;

import java.util.List;

import javax.ejb.Remote;
import org.jaredstevens.servers.db.entities.Repeater;
import org.jaredstevens.servers.db.entities.Repeater.Status;

@Remote
public interface IRepeaterOps {
	public Repeater getRepeaterById(long repeaterId);
	public Repeater save(long repeaterId, Status status);
	public void remove(long repeaterId);
	public boolean loadData(List<Repeater> parsedData);
	public List<Repeater> findRepeaters(int latitude, int longitude, int radius, int pageNum, int pageCount);
	public boolean clearData();
	public Number getRowCount();
}
