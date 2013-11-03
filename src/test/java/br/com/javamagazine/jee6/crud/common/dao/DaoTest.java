package br.com.javamagazine.jee6.crud.common.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * DAO utilitario usado por classes de testes
 * @author cicero
 *
 */
@Stateless
public class DaoTest {
	
	@PersistenceContext
	private EntityManager em;

	/**
	 * Remove todos os registros de uma entidade
	 * @param entityName
	 */
	public void deleteAll(String entityName) {
		List<Object> registros = em.createQuery("select o from " + entityName + " o").getResultList();
		for (Object obj : registros) {
			em.remove(obj);
		}
	}

}
