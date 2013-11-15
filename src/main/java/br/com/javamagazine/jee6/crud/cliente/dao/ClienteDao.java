package br.com.javamagazine.jee6.crud.cliente.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.javamagazine.jee6.crud.cliente.entity.Cliente;
import br.com.javamagazine.jee6.crud.cliente.entity.filter.ClienteFilter;

@Stateless
public class ClienteDao {
	
	@PersistenceContext
	private EntityManager em;
	
	public Cliente save(Cliente cliente) {
		if (cliente.getId() == null) {
			em.persist(cliente);
		} else {
			em.merge(cliente);
		}
		return cliente;
	}
	
	public void remove(Long id) {
		em.createQuery("delete from Cliente c where c.id = :id").setParameter("id", id).executeUpdate();
	}
	
	public Cliente findById(Long id) {
		return em.find(Cliente.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> findByFilter(ClienteFilter filter) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select c from Cliente c");
		if (filter != null && filter.getNome() != null) {
			jpql.append(" where c.nome like :nome");
			
		}
		jpql.append(" order by c.nome");
		Query query = em.createQuery(jpql.toString());
		if (filter != null && filter.getNome() != null) {
			query.setParameter("nome", "%" + filter.getNome() + "%");
		}
		return query.getResultList();
	}
	
	public boolean existe(Cliente cliente) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select count(c) from Cliente c where c.email = :email");
		if (cliente.getId() != null) {
			jpql.append(" and c.id != :id");
		}
		Query query = em.createQuery(jpql.toString());
		if (cliente.getId() != null) {
			query.setParameter("id", cliente.getId());
		}
		query.setParameter("email", cliente.getEmail());
		return ((Long) query.getSingleResult()).intValue() > 0;
	}
	
}
