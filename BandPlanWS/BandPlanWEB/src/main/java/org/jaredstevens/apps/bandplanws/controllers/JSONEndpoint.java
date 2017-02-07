package org.jaredstevens.apps.bandplanws.controllers;

import java.util.List;

import javax.ejb.EJB;

import org.jaredstevens.apps.bandplanws.models.JSONRepeater;
import org.jaredstevens.servers.db.entities.Repeater;
import org.jaredstevens.servers.db.interfaces.IRepeaterOps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JSONEndpoint {
	@EJB(mappedName="java:module/RepeaterOps")
	private IRepeaterOps repeaterService;

	/*
	@RequestMapping(value="/", method = RequestMethod.GET)
	public @ResponseBody String testRequest() {
		String retVal = "Hello world";
		return retVal;
	}

	@RequestMapping(value="/testRequest/{input}", method = RequestMethod.GET)
	public @ResponseBody String testRequest(@PathVariable String input) {
		String retVal = "Hello "+input;
		return retVal;
	}
	 */
	@RequestMapping(value="/findRepeaters/{latitude}/{longitude}/{radius}/{pageNum}/{pageCount}", method = RequestMethod.GET)
	public @ResponseBody List<JSONRepeater> findRepeaters(@PathVariable int latitude,@PathVariable int longitude,@PathVariable int radius,@PathVariable int pageNum,@PathVariable int pageCount) {
		List<JSONRepeater> retVal = null;
		List<Repeater> repeaterList = this.repeaterService.findRepeaters(latitude, longitude, radius, pageNum, pageCount);
		retVal = JSONRepeater.repeaterFactory(repeaterList);
		return retVal;
	}
}
