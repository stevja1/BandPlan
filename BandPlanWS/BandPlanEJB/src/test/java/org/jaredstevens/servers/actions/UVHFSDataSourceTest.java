package org.jaredstevens.servers.actions;

import static org.junit.Assert.*;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jaredstevens.servers.db.entities.Repeater;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UVHFSDataSourceTest {
	private EntityManager em;
	private static String repeaterUrl = "http://utahvhfs.org/rptrraw.txt";
	
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
	public void testReadURLData() {
		List<String> lines;
		lines = UVHFSDataSource.readURLData(UVHFSDataSourceTest.repeaterUrl);
		if(lines == null || lines.size() <= 0)
			fail("Didn't get any data from server!");
	}
	
	@Test
	public void testParseData() {
		List<String> lines;
		lines = UVHFSDataSource.readURLData(UVHFSDataSourceTest.repeaterUrl);
		if(lines == null || lines.size() <= 0)
			fail("Didn't get any data from server!");
		else {
			List<Repeater> parseDataResult = null;
			parseDataResult = UVHFSDataSource.parseData(lines);
			if(parseDataResult == null)
				fail("Couldn't parse the data for some reason");
		}
	}
	
	@Test
	public void testRefreshUVHFSRepeaters() {
		boolean refreshResult = false;
		UVHFSDataSource actions = new UVHFSDataSource();
		actions.setEm(em);
		em.getTransaction().begin();
		refreshResult = actions.refreshUVHFSRepeaters();
		if(refreshResult)
			em.getTransaction().commit();
		else {
			em.getTransaction().rollback();
			fail("Couldn't save data to database");
		}
	}
}
