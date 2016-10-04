package com.gcitsolutions.libraryapp;

import java.net.UnknownHostException;

import org.apache.commons.dbcp.BasicDataSource;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gcitsolutions.libraryapp.Data.AuthorDAO;
import com.gcitsolutions.libraryapp.Data.BookCopiesDAO;
import com.gcitsolutions.libraryapp.Data.BookDAO;
import com.gcitsolutions.libraryapp.Data.BookLoanDAO;
import com.gcitsolutions.libraryapp.Data.BorrowerDAO;
import com.gcitsolutions.libraryapp.Data.GenreDAO;
import com.gcitsolutions.libraryapp.Data.LibraryBranchDAO;
import com.gcitsolutions.libraryapp.Data.PublisherDAO;
import com.gcitsolutions.libraryapp.Entity.Message;
import com.gcitsolutions.libraryapp.Service.Administrator;
import com.gcitsolutions.libraryapp.Service.BorrowerService;
import com.gcitsolutions.libraryapp.Service.Librarian;
import com.mongodb.MongoClient;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true) 
@EnableTransactionManagement
public class SpringMVCLibraryAppConfig {
	private final String driver="com.mysql.jdbc.Driver";
	private final String url="jdbc:mysql://localhost:3306/library";
	private final String userName="root";
	private final String password="root";
	
	@Bean
	public BasicDataSource dataSource(){
		BasicDataSource dataSource=new BasicDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(userName);
		dataSource.setPassword(password);
		return dataSource;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate(){
		JdbcTemplate template=new JdbcTemplate();
		template.setDataSource(dataSource());
		return template;
	}
	
	@Bean
	public PlatformTransactionManager txnManager(){
		DataSourceTransactionManager txnManager=new DataSourceTransactionManager();
		txnManager.setDataSource(dataSource());
		return txnManager;
	}
	
	@Bean
	public Administrator adminService(){
		return new Administrator();
	}
	
	@Bean
	public Librarian librarianService(){
		return Librarian.getInstance();
	}
	
	@Bean
	public BorrowerService borrowerService(){
		return BorrowerService.getInstance();
	}
	
	
	@Bean
	public AuthorDAO adao(){
		return new AuthorDAO();
	}
	
	@Bean
	public BookDAO bdao(){
		return new BookDAO();
	}
	
	@Bean
	public BookCopiesDAO bcopdao(){
		return new BookCopiesDAO();
	}
	
	
	@Bean
	public PublisherDAO pubdao(){
		return new PublisherDAO();
	}
	
	@Bean
	public BookLoanDAO bldao(){
		return new BookLoanDAO();
	}
	
	@Bean
	public LibraryBranchDAO libdao(){
		return new LibraryBranchDAO();
	}
	
	@Bean
	public GenreDAO genredao(){
		return new GenreDAO();
	}
	
	@Bean
	public BorrowerDAO bordao(){
		return new BorrowerDAO();
	}
	@Bean
	public Message message(){
		return new Message();
	}
	
	@Bean
	public ObjectMapper objMapper(){
		return new ObjectMapper();
	}
	
	@Bean
	public SimpleMongoDbFactory dbFactory() throws UnknownHostException{
		SimpleMongoDbFactory factory=new SimpleMongoDbFactory(new MongoClient(),"local");
		return factory;
	}

	@Bean
	public MongoTemplate mongoTemplate() throws UnknownHostException{
		MongoTemplate mongoTemplate=new MongoTemplate(dbFactory());
		return mongoTemplate;
	}
	
}
