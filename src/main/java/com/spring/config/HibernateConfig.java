package com.spring.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;


@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@ComponentScans(value = { @ComponentScan("com.spring.dao"),
	    @ComponentScan("com.spring.service") })
public class HibernateConfig {
	
	@Autowired
	private Environment env;
	
	//Define Database DataSource / connection pool
	
	@Bean(destroyMethod="close")
	public DataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		
		dataSource.setDriverClass(env.getProperty("mysql.driver"));
		dataSource.setJdbcUrl(env.getProperty("mysql.jdbcUrl"));
		dataSource.setUser(env.getProperty("mysql.username"));
		dataSource.setPassword(env.getProperty("mysql.password"));
		
		dataSource.setInitialPoolSize(Integer.valueOf(env.getProperty("hibernate.c3p0.initialPoolSize")));
		dataSource.setMinPoolSize(Integer.valueOf(env.getProperty("hibernate.c3p0.min_size")));
		dataSource.setMaxPoolSize(Integer.valueOf(env.getProperty("hibernate.c3p0.max_size")));
		dataSource.setMaxIdleTime(Integer.valueOf(env.getProperty("hibernate.c3p0.maxIdleTime")));
		
		return dataSource;
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory(DataSource dataSource) throws PropertyVetoException {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan(env.getProperty("hibernate.toScan"));
		sessionFactory.setHibernateProperties(hibernateProperties());
		
		return sessionFactory;
	}
	
	private Properties hibernateProperties() {
		Properties properties = new Properties();
		
		properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		
		return properties;
	}
	
	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) throws PropertyVetoException {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		
		transactionManager.setSessionFactory(sessionFactory);
		
		return transactionManager;
	}

}
