package vn.nganluong.naba.repository;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.entities.PgSftp;

@Repository
public interface PgSftpRepository extends JpaRepository<PgSftp, Integer> {

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	@Query("SELECT u FROM PgSftp u WHERE u.channelId = :channelId AND u.pgFunctionId = :pgFunctionId and u.status = 1")
	PgSftp findTopByChannelIdAndPgFunctionId(@Param("channelId") int channelId,
			@Param("pgFunctionId") Integer pgFunctionId);
}