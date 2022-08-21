package vn.nganluong.naba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.nganluong.naba.entities.PgEndpoint;

import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface PgEndPointRepository extends JpaRepository<PgEndpoint, Integer> {

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	@Query("SELECT u FROM PgEndpoint u WHERE u.id = :id and u.status = 1")
	Optional<PgEndpoint> findById(@Param("id") Integer id);

}