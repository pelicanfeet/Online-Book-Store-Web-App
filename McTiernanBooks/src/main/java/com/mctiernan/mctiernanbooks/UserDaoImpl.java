package com.mctiernan.mctiernanbooks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Repository
public class UserDaoImpl implements UserDao {

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
	public User createUser(final User user) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			String SQL = "insert into account (email, password) " + " values (?, ?)";
			jdbcTemplate.update(SQL, user.getEmail(), user.getPassword());
			
			SQL = "insert into profile (email, firstName, lastName, homeAddress) " + " values (?, ?, ?, ?)";
			jdbcTemplate.update(SQL, user.getEmail(), user.getFirstName(), user.getLastName(), user.getHomeAddress());
			
			SQL = "insert into authority (email, role) " + " values (?, ?)";
			for(String role : user.getRoles()) {
				jdbcTemplate.update(SQL, user.getEmail(), "ROLE_" + role);
			}

			logger.info("Created User Name = " + user.getEmail());

			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error in creating user record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		String SQL = "select p.email, p.firstName, p.lastName, p.homeAddress " +
				     "from profile p";
		List<User> users = jdbcTemplate.query(SQL, new UserProfileMapper());
		return users;
	}

	@Override
	public User getUserByEmail(String email) {
		String SQL = "select a.email, a.password, p.firstName, p.lastName, p.homeAddress " +
					 "from account a, profile p where a.email=p.email and a.email=?";
		User user = jdbcTemplate.queryForObject(SQL, new Object[] { email }, new UserMapper());
		
		SQL = "select * from authority where email = ?";
		List<String> list = jdbcTemplate.query(SQL, new String[] { email }, new UserRoleMapper());
		user.setRoles(list);
		
		logger.info("Retrieved User Name = " + user.getEmail());
		
		return user;
	}

	@Override
	public User deleteUserByEmail(String email) {
		String SQL = "select a.email, a.password, p.firstName, p.lastName, p.homeAddress " + 
				     "from account a, profile p where a.email=p.email and a.email=?";
		User user = jdbcTemplate.queryForObject(SQL, new Object[] { email }, new UserMapper());
		
		SQL = "delete from profile where email = ?";
		jdbcTemplate.update(SQL, user.getEmail());
		
		SQL = "delete from authority where email = ?";
		jdbcTemplate.update(SQL, user.getEmail());
		
		SQL = "delete from account where email = ?";
		jdbcTemplate.update(SQL, user.getEmail());
		
		return user;
	}

	@Override
	public List<String> getRoleTypes() {
		String SQL = "select * from role";
		List<String> list = jdbcTemplate.query(SQL, new RoleMapper());
		return list;
	}

	class UserMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setEmail(rs.getString("email"));
			user.setFirstName(rs.getString("firstName"));
			user.setLastName(rs.getString("lastName"));
			user.setHomeAddress(rs.getString("homeAddress"));
			user.setPassword(rs.getString("password"));
			return user;
		}
	}
	
	class UserProfileMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setEmail(rs.getString("email"));
			user.setFirstName(rs.getString("firstName"));
			user.setLastName(rs.getString("lastName"));
			user.setHomeAddress(rs.getString("homeAddress"));
			return user;
		}
	}
	
	class RoleMapper implements RowMapper<String> {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("name");
		}
	}
	
	class UserRoleMapper implements RowMapper<String> {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("role");
		}
	}
}