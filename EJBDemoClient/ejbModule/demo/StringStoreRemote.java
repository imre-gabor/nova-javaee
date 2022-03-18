package demo;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Remove;

@Remote
public interface StringStoreRemote {

	List<String> getAll();

	void add(String s);

	void release();

}
