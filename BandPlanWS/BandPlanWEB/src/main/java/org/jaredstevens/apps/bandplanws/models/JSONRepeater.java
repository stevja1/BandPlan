package org.jaredstevens.apps.bandplanws.models;

import java.util.ArrayList;
import java.util.List;

import org.jaredstevens.servers.db.entities.Repeater;
import org.jaredstevens.servers.db.entities.Repeater.Availability;
import org.jaredstevens.servers.db.entities.Repeater.Status;

public class JSONRepeater {
    private long id;
    private Status status;
    private Availability availability;
    private String band;
    private float input;
    private float output;
    private float ctcssIn;
    private float ctcssOut;
    private String dcsCode;
    private String callsign;
    private String sponsor;
    private float latitude;
    private float longitude;
    private boolean closed;
    
    public static JSONRepeater repeaterFactory(Repeater inRepeater) {
    	JSONRepeater retVal = new JSONRepeater();
    	if(inRepeater == null) return retVal;
    	retVal.setId(inRepeater.getId());
    	if(inRepeater.getStatus() != null) retVal.setStatus(inRepeater.getStatus());
    	if(inRepeater.getAvailability() != null) retVal.setAvailability(inRepeater.getAvailability());
    	if(inRepeater.getBand() != null) retVal.setBand(inRepeater.getBand());
    	retVal.setInput(inRepeater.getInput());
    	retVal.setOutput(inRepeater.getOutput());
    	retVal.setCtcssIn(inRepeater.getCtcssIn());
    	retVal.setCtcssOut(inRepeater.getCtcssOut());
    	if(retVal.getDcsCode() != null) retVal.setDcsCode(inRepeater.getDcsCode());
    	if(retVal.getCallsign() != null) retVal.setCallsign(inRepeater.getCallsign());
    	if(retVal.getSponsor() != null) retVal.setSponsor(inRepeater.getSponsor());
    	retVal.setLatitude(inRepeater.getLatitude());
    	retVal.setLongitude(inRepeater.getLongitude());
    	retVal.setClosed(inRepeater.isClosed());
    	return retVal;
    }
    
    public static List<JSONRepeater> repeaterFactory(List<Repeater> inList) {
    	ArrayList<JSONRepeater> retVal = null;
    	if(inList != null && inList.size() > 0) {
        	for(Repeater repeater : inList) {
        		if(retVal != null) retVal.add(JSONRepeater.repeaterFactory(repeater));
        		else retVal = new ArrayList<JSONRepeater>();
        	}
    	}
    	return retVal;
    }
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Availability getAvailability() {
		return availability;
	}
	public void setAvailability(Availability availability) {
		this.availability = availability;
	}
	public String getBand() {
		return band;
	}
	public void setBand(String band) {
		this.band = band;
	}
	public float getInput() {
		return input;
	}
	public void setInput(float input) {
		this.input = input;
	}
	public float getOutput() {
		return output;
	}
	public void setOutput(float output) {
		this.output = output;
	}
	public float getCtcssIn() {
		return ctcssIn;
	}
	public void setCtcssIn(float ctcssIn) {
		this.ctcssIn = ctcssIn;
	}
	public float getCtcssOut() {
		return ctcssOut;
	}
	public void setCtcssOut(float ctcssOut) {
		this.ctcssOut = ctcssOut;
	}
	public String getDcsCode() {
		return dcsCode;
	}
	public void setDcsCode(String dcsCode) {
		this.dcsCode = dcsCode;
	}
	public String getCallsign() {
		return callsign;
	}
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public boolean isClosed() {
		return closed;
	}
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
}
