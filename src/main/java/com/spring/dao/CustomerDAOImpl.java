package com.spring.dao;

import java.util.List;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.entity.Customer;
@Repository
public class CustomerDAOImpl implements CustomerDAO {
	
	//need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Customer> getCustomers() {
		Session currentsession = sessionFactory.getCurrentSession();
		Query<Customer> theQuery = currentsession.createQuery("from Customer",Customer.class);
		List<Customer> customers = theQuery.getResultList();
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		Session currentsession = sessionFactory.getCurrentSession();
		
		currentsession.saveOrUpdate(theCustomer);
	}

	@Override
	public Customer getCustomers(int theId) {
		Session currentsession = sessionFactory.getCurrentSession();
		
		Customer theCustomer = currentsession.get(Customer.class, theId);
		
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {
		Session currentsession = sessionFactory.getCurrentSession();
		
		Customer theCustomer = currentsession.get(Customer.class, theId);
		
		currentsession.delete(theCustomer);
	}
	
	@Override
	public List<Customer> searchCustomers(String theSearchName) {

		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Customer> theQuery = null;
		
		if (theSearchName != null && theSearchName.trim().length() > 0) {

			// search for firstName or lastName ... case insensitive
			theQuery =currentSession.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
			theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");

		}
		else {
			// theSearchName is empty ... so just get all customers
			theQuery =currentSession.createQuery("from Customer", Customer.class);			
		}
		
		List<Customer> customers = theQuery.getResultList();
					
		return customers;
		
	}
}
