package org.jaredstevens.servers.db.operations;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.jaredstevens.servers.db.entities.Repeater;
import org.jaredstevens.servers.db.entities.Repeater.Status;
import org.jaredstevens.servers.db.interfaces.IRepeaterOps;

/**
 * @author jstevens
 *
 */
@Stateless(name="RepeaterOps",mappedName="RepeaterOps")
@Remote
public class RepeaterOps implements IRepeaterOps{
	@PersistenceContext(unitName="BandPlanPU",type=PersistenceContextType.TRANSACTION)
	private EntityManager em;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Repeater getRepeaterById(long repeaterId) {
		Repeater retVal = null;
		if(repeaterId > 0)
		{
			retVal = (Repeater)this.getEm().find(Repeater.class, repeaterId);
		}
		return retVal;
	}
	
	/**
	 * Other methods:
	 * search - lat/long, radius, band, closed, tone
	 */

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Repeater save(long repeaterId, Status status) {
		Repeater retVal = null;
		Repeater repeaterEntry = new Repeater();
		if(repeaterId > 0) repeaterEntry.setId(repeaterId);
		repeaterEntry.setStatus(status);
		this.getEm().persist(repeaterEntry);
		if(repeaterEntry != null) retVal = repeaterEntry;
		return retVal;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(long repeaterId) {
		if(repeaterId > 0) {
			Repeater historyEntry = null;
			historyEntry = (Repeater)this.getEm().find(Repeater.class, repeaterId);
			if(historyEntry != null) this.getEm().remove(historyEntry);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean loadData(List<Repeater> parsedData) {
		boolean retVal = true;
		EntityManager em = null;
		if(this.getEm() != null)
			em = this.getEm();
		if(em != null) {
			for(Repeater repeater : parsedData) {
				em.persist(repeater);
				if(repeater.getId() <= 0) {
					retVal = false;
					break;
				}
			}
		}
		return retVal;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Repeater> findRepeaters(int latitude, int longitude, int radius, int pageNum, int pageCount) {
		List<Repeater> retVal = null;
		EntityManager em = null;
		if(this.getEm() != null)
			em = this.getEm();
		if(em != null) {
			String sql = "CALL geodist(?,?,?,?,?)";
			Query query = this.em.createNativeQuery(sql, Repeater.class);
			query.setParameter(1, (double)latitude/1000000);
			query.setParameter(2, (double)longitude/1000000);
			query.setParameter(3, radius);
			query.setParameter(4, pageNum);
			query.setParameter(5, pageCount);
			List<Repeater> results = query.getResultList();
			if(results != null) {
				retVal = results;
			}
		}
		return retVal;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean clearData() {
		boolean retVal = false;
		Number rowsBefore = this.getRowCount();
		if(rowsBefore.intValue() > 0) {
			Number rowsAfter;
			this.getEm().createNativeQuery("TRUNCATE TABLE Repeater").executeUpdate();
			rowsAfter = this.getRowCount();
			if(rowsAfter.intValue() == 0) retVal = true;
		} else retVal = true;
		return retVal;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Number getRowCount() {
		Number retVal = -1;
		Query sql = this.getEm().createQuery("SELECT count(r) FROM Repeater r");
		retVal = (Number)sql.getSingleResult();
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