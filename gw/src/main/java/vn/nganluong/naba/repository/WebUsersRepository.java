package vn.nganluong.naba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.nganluong.naba.entities.WebUsers;

public interface WebUsersRepository extends JpaRepository<WebUsers, Integer> {

	public WebUsers findByUserNameAndStatus(String userName, Boolean status);
}