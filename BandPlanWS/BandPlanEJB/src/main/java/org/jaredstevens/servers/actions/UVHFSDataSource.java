package org.jaredstevens.servers.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jaredstevens.servers.db.entities.Repeater;
import org.jaredstevens.servers.db.entities.Repeater.Availability;
import org.jaredstevens.servers.db.entities.Repeater.Status;
import org.jaredstevens.servers.db.operations.RepeaterOps;
import org.jaredstevens.servers.utilities.Utilities;

public class UVHFSDataSource {
	public static final int BAND      = 0;
	public static final int OUTPUT    = 1;
	public static final int INPUT     = 2;
	public static final int CALLSIGN  = 5;
	public static final int SPONSOR   = 6;
	public static final int OPEN      = 10;
	public static final int CLOSED    = 11;
	public static final int CTCSS_IN  = 16;
	public static final int CTCSS_OUT = 17;
	public static final int DCS_CODE  = 19;
	public static final int LAT       = 39;
	public static final int LONG      = 40;
	
	private EntityManager em;
	
	public boolean refreshUVHFSRepeaters() {
		boolean retVal = false;
		RepeaterOps db = new RepeaterOps();
		db.setEm(em);
		List<String> lines = UVHFSDataSource.readURLData("http://utahvhfs.org/rptrraw.txt");
		List<Repeater> repeaters = UVHFSDataSource.parseData(lines);
		if(db.clearData())
			retVal = db.loadData(repeaters);
		return retVal;
	}
	
	public static List<String> readURLData(String url) {
		ArrayList<String> lines = new ArrayList<String>();
		URL repeaterData;
		try {
			repeaterData = new URL(url);
	        URLConnection yc = repeaterData.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                                    yc.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null)
	        	lines.add(inputLine);
	        in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}

	public static List<Repeater> parseData(List<String> lines) {
		ArrayList<Repeater> retVal = null;
		Repeater repeater = null;
		String[] fields = null;
		int lineCount = 0;
		for(String line : lines) {
			lineCount++;
			
			// Skip the first line
			if(lineCount == 1)
				continue;
			
			fields = line.split(",");
			if(fields.length > 0) {
				if(retVal == null) retVal = new ArrayList<Repeater>();
				repeater = new Repeater();
				repeater.setStatus(Status.ACTIVE);
				repeater.setBand(fields[UVHFSDataSource.BAND]);
				repeater.setOutput(Utilities.parseFrequency(fields[UVHFSDataSource.OUTPUT]));
				if(fields[UVHFSDataSource.INPUT].length() <= 0)
					repeater.setInput(repeater.getOutput());
				else
					repeater.setInput(Utilities.parseFrequency(fields[UVHFSDataSource.INPUT]));
				repeater.setCallsign(fields[UVHFSDataSource.CALLSIGN]);
				repeater.setSponsor(fields[UVHFSDataSource.SPONSOR]);
				repeater.setLatitude(Utilities.parseLatLong(fields[UVHFSDataSource.LAT]));
				repeater.setLongitude(Utilities.parseLatLong(fields[UVHFSDataSource.LONG]));
				if(fields[UVHFSDataSource.OPEN].length() <= 0 || fields[UVHFSDataSource.OPEN].equals("Y"))
					repeater.setAvailability(Availability.OPEN);
				else repeater.setAvailability(Availability.CLOSED);
				repeater.setCtcssIn(Utilities.parseFrequency(fields[UVHFSDataSource.CTCSS_IN]));
				repeater.setCtcssOut(Utilities.parseFrequency(fields[UVHFSDataSource.CTCSS_OUT]));
				repeater.setDcsCode(fields[UVHFSDataSource.DCS_CODE]);
				
				retVal.add(repeater);
			}
		}
		return retVal;
	}

	/**
	 * @return the em
	 */
	public EntityManager getEm() {
		return em;
	}

	/**
	 * @param em the em to set
	 */
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
