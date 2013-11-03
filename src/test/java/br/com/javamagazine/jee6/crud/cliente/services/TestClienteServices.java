package br.com.javamagazine.jee6.crud.cliente.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.javamagazine.jee6.crud.cliente.dao.ClienteDao;
import br.com.javamagazine.jee6.crud.cliente.entity.Cliente;
import br.com.javamagazine.jee6.crud.cliente.entity.ClienteTelefone;
import br.com.javamagazine.jee6.crud.cliente.entity.filter.ClienteFilter;
import br.com.javamagazine.jee6.crud.cliente.exception.ClienteExistenteException;
import br.com.javamagazine.jee6.crud.cliente.exception.NenhumTelefoneInformadoException;
import br.com.javamagazine.jee6.crud.common.dao.DaoTest;

@RunWith(Arquillian.class)
public class TestClienteServices {
	
	private static final DateFormat FORMATO_DATA = new SimpleDateFormat("dd/MM/yyyy");
	
	@Inject
	private ClienteServices clienteServices;
	
	@Inject
	private DaoTest daoTest;
	
	@Deployment
	public static WebArchive createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "testCliente.war")
				.addClasses(Cliente.class, ClienteTelefone.class, ClienteFilter.class)
				.addClasses(ClienteExistenteException.class, NenhumTelefoneInformadoException.class)
				.addClasses(ClienteServices.class, ClienteServicesImpl.class, ClienteDao.class)
				.addClasses(DaoTest.class).addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource("test-ds.xml");
	}
	
	@Test
	public void setUpTest() {
		daoTest.deleteAll(Cliente.class.getSimpleName());
	}
	
	@Test
	public void testAdd() throws Exception {
		Long idCliente = clienteServices.save(getCliente()).getId();
		Cliente cliente = clienteServices.findById(idCliente);
		assertNotNull(cliente);
		assertEquals("Bill", cliente.getNome());
		assertEquals(getDate("10/10/1980"), cliente.getDataNascimento());
		assertEquals("bill@site.com", cliente.getEmail());
		assertEquals(2, cliente.getTelefones().size());
	}
	
	@Test
	public void testAddEmailDuplicado() throws Exception {
		try {
			clienteServices.save(getCliente());
		} catch (ClienteExistenteException e) {
			fail("Nao deveria ter dado falha");
		} 
		
		try {
			clienteServices.save(getCliente());
			fail("Deveria ter dado falha");
		} catch (ClienteExistenteException e) {
			
		}
		
	}
	
	@Test
	public void testAddSemTelefone() throws Exception {
		Cliente cliente = new Cliente("Bill", getDate("10/10/1980"), "bill@site.com");
		try {
			clienteServices.save(cliente);
			fail("Deveria ter dado falha");
		} catch (NenhumTelefoneInformadoException e) {
			
		}
		
		cliente.addTelefone(new ClienteTelefone("19-1111-1111", "Residencial"));
		
		try {
			clienteServices.save(cliente);
		} catch (NenhumTelefoneInformadoException e) {
			fail("Nao deveria ter dado falha");
		}
	}
	
	@Test
	public void testUpdate() throws Exception {
		Long idCliente = clienteServices.save(getCliente()).getId();
		Cliente cliente = clienteServices.findById(idCliente);
		
		// verifica os dados antes da alteracao
		assertEquals("Bill", cliente.getNome());
		assertEquals(getDate("10/10/1980"), cliente.getDataNascimento());
		assertEquals("bill@site.com", cliente.getEmail());
		assertEquals(2, cliente.getTelefones().size());
		
		// altera dos dados
		cliente.setDataNascimento(getDate("11/11/1981"));
		cliente.setEmail("bill@outrosite.com");
		cliente.addTelefone(new ClienteTelefone("19-8888-9999", "Comercial"));
		clienteServices.save(cliente);
		
		// verifica os dados apos a alteracao
		cliente = clienteServices.findById(idCliente);
		assertEquals("Bill", cliente.getNome());
		assertEquals(getDate("11/11/1981"), cliente.getDataNascimento());
		assertEquals("bill@outrosite.com", cliente.getEmail());
		assertEquals(3, cliente.getTelefones().size());
		
		// verifica se deixa alterar o cliente sem alterar o e-mail
		try {
			cliente.setDataNascimento(getDate("12/11/1980"));
			clienteServices.save(cliente);
		} catch (ClienteExistenteException e) {
			fail("Deveria ter deixado alterar o cliente");
		}
		
		// adiciona outro cliente e verifica que o email do cliente atual nao pode ser alterado para esse outro
		clienteServices.save(getCliente());
		cliente.setEmail("bill@site.com");
		
		try {
			clienteServices.save(cliente);
			fail("Nao deveria ter deixado alterar o email do cliente");
		} catch (ClienteExistenteException e) {
			
		}
				
	}
	
	@Test
	public void testRemove() throws Exception {
		Long idCliente = clienteServices.save(getCliente()).getId();
		assertNotNull(clienteServices.findById(idCliente));
		clienteServices.remove(idCliente);
		assertNull(clienteServices.findById(idCliente));
	}
	

	private Date getDate(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Cliente getCliente() {
		// TODO Auto-generated method stub
		return null;
	}


}
