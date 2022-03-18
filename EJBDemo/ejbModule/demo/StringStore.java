package demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Remove;
import javax.ejb.Stateful;

/**
 * Session Bean implementation class StringStore
 */
@Stateful
public class StringStore implements StringStoreRemote {
	
	private List<String> strings = new ArrayList<>();

    /**
     * Default constructor. 
     */
    public StringStore() {
    	System.out.println("StringStore constructor called.");
    }
    
    @PostConstruct
    public void init() {
    	System.out.println("StringStore init called.");    	
    }
    
    @PreDestroy
    public void destroy() {
    	System.out.println("StringStore destroy called.");    	
    }
    
    @Override
	@Remove
    public void release() {
    	System.out.println("StringStore instance released.");    	
    }
    
    @Override
	public void add(String s) {
    	strings.add(s);
    }
    
    @Override
	public List<String> getAll(){
    	return Collections.unmodifiableList(strings);
    }
}
