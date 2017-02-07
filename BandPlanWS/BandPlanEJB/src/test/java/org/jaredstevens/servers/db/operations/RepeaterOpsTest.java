package org.jaredstevens.servers.db.operations;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jaredstevens.servers.db.entities.Repeater;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class RepeaterOpsTest {
	private EntityManager em;

	@Before
	public void setUp() throws Exception {
		// Setup an EntityManager
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("BandPlanTestPU", System.getProperties());
		this.em = factory.createEntityManager();
	}

	@After
	public void tearDown() throws Exception {
		this.em.getEntityManagerFactory().close();
	}

	@Test
	@Ignore
	public void testGetRepeaterById() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRemove() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testLoadData() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindRepeaters() {
		int latitude = 40383495;
		int longitude = -111814304;
		int radius = 10;
		RepeaterOps conn = new RepeaterOps();
		conn.setEm(this.em);
		List<Repeater> results = conn.findRepeaters(latitude, longitude, radius, 1, 10);
		for(Repeater result : results) {
			System.out.println("Id: "+result.getId());
		}
	}

	@Test
	@Ignore
	public void testClearData() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetRowCount() {
		fail("Not yet implemented");
	}

}
