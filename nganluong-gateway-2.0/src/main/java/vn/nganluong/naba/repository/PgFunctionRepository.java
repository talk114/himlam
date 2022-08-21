package vn.nganluong.naba.repository;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.entities.PgFunction;
import vn.nganluong.naba.entities.PgSftp;

@Repository
public interface PgFunctionRepository extends JpaRepository<PgFunction, Integer> {
//	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
//	@Query("SELECT u FROM PgFunction u WHERE u.code = :code and u.status = 1")
//	PgFunction findByCode(String code);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	PgFunction findFirstByCodeAndStatus(String code, boolean status);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	PgFunction findFirstByCodeAndChannelIdAndStatus(String code, Integer channelId, boolean status);

	PgFunction findPgFunctionByCode(String pgFuntion);
}