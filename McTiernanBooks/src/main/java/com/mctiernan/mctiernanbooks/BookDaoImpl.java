package com.mctiernan.mctiernanbooks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Repository
public class BookDaoImpl implements BookDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}
	
	@Override
	public Book createBook(Book book) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String SQL = "insert into book (isbn, title, author, description, cost, category) "
						 + "values (?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(SQL, book.getIsbn(), book.getTitle(), book.getAuthor(), book.getDescription(),
								book.getCost(), book.getCategory());
			SQL = "insert into book_format (isbn, format) " + "values (?, ?)";
			for(int i = 0; i < book.getFormat().size(); i++) {
				jdbcTemplate.update(SQL, book.getIsbn(), book.getFormat().get(i));
			}
			logger.info("Created Book Isbn = " + book.getIsbn());
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error in creating book record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}

		return book;
	}

	@Override
	public List<Book> getAllBooks() {
		String SQL = "select * from book";
		List<Book> books = jdbcTemplate.query(SQL, new BookMapper());
		return books;
	}

	@Override
	public Book getBookByIsbn(String isbn) {
		Book book = null;
		try {
			String SQL = "select * from book where isbn = ?";
			book = jdbcTemplate.queryForObject(SQL, new Object[] { isbn }, new BookMapper());
			SQL = "select * from book_format where isbn = ?";
			List<String> list = jdbcTemplate.query(SQL, new String[] { isbn }, new BookFormatMapper());
			book.setFormat(list);
			logger.info("Retrieved Book Isbn = " + book.getIsbn());
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
		return book;
	}

	@Override
	public Book deleteBookByIsbn(String isbn) {
		String SQL = "select * from book where isbn = ?";
		Book book = jdbcTemplate.queryForObject(SQL, new Object[] { isbn }, new BookMapper());
		
		SQL = "delete from book_format where isbn = ?";
		jdbcTemplate.update(SQL, book.getIsbn());
		
		SQL = "delete from book where isbn = ?";
		jdbcTemplate.update(SQL, book.getIsbn());
		
		return book;
	}

	@Override
	public List<String> getFormatTypes() {
		String SQL = "select * from format";
		List<String> list = jdbcTemplate.query(SQL, new FormatMapper());
		return list;
	}

	@Override
	public List<String> getCategoryTypes() {
		String SQL = "select * from category";
		List<String> list = jdbcTemplate.query(SQL, new CategoryMapper());
		return list;
	}
	
	class BookMapper implements RowMapper<Book> {
		public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
			Book book = new Book();
			book.setIsbn(rs.getString("isbn"));
			book.setTitle(rs.getString("title"));
			book.setAuthor(rs.getString("author"));
			book.setDescription(rs.getString("description"));
			book.setCost(rs.getDouble("cost"));
			book.setCategory(rs.getString("category"));
			return book;
		}
	}
	
	class CategoryMapper implements RowMapper<String> {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("name");
		}
	}
	
	class FormatMapper implements RowMapper<String> {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("name");
		}
	}

	class BookFormatMapper implements RowMapper<String> {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("format");
		}
	}

}