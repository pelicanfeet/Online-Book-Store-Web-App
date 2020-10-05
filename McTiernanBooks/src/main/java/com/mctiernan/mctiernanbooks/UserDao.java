package com.mctiernan.mctiernanbooks;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
	public User createUser(User user);
	public List<User> getAllUsers();
	public User getUserByEmail(String email);
	public User deleteUserByEmail(String email);
	public List<String> getRoleTypes();
}