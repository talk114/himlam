package vn.nganluong.naba.repository;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.entities.PgErrorDefinition;

@Repository
public interface PgErrorDefinitionRepository extends JpaRepository<PgErrorDefinition, Integer> {

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	@Query("SELECT u FROM PgErrorDefinition u WHERE u.code = :code and u.status = 1")
	PgErrorDefinition findByCodeAndStatus(@Param("code") String code);

}