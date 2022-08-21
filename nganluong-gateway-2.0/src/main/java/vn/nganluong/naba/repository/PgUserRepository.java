package vn.nganluong.naba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import vn.nganluong.naba.entities.PgFunction;
import vn.nganluong.naba.entities.PgUser;

import javax.persistence.QueryHint;

@Repository
public interface PgUserRepository extends JpaRepository<PgUser, Integer> {
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	@Query("SELECT u FROM PgUser u WHERE u.code = :code and u.status = 1")
	PgUser findByCode(String code);

}