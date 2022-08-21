package vn.nganluong.naba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.entities.PgMail;

@Repository
public interface PgMailRepository extends JpaRepository<PgMail, Integer> {

//	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") }) 
	PgMail findFirstByStatusOrderByIdAsc(Boolean status);
	

}