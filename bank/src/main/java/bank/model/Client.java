package bank.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * The persistent class for the CLIENT database table.
 * 
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQuery(name="Client.findAll", query="SELECT c FROM Client c")
@NamedQuery(name="Client.findByIdIn", query="SELECT c FROM Client c WHERE c.clientid IN :ids")
@NamedEntityGraph(name = "Client.EGWithAccounts", attributeNodes = @NamedAttributeNode("accounts"))
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int clientid;

	@NotEmpty
	@ContainsZipCode
	private String address;

	@NotEmpty
	private String name;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="client"/*, fetch = FetchType.EAGER*/)
//	@Fetch(FetchMode.SUBSELECT)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<Account> accounts;

	public Client() {
	}

	
	public Client(String name, String address) {
		super();
		this.name = name;
		this.address = address;
	}

	public int getClientid() {
		return this.clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Account> getAccounts() {
		return this.accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public Account addAccount(Account account) {
		getAccounts().add(account);
		account.setClient(this);

		return account;
	}

	public Account removeAccount(Account account) {
		getAccounts().remove(account);
		account.setClient(null);

		return account;
	}

}