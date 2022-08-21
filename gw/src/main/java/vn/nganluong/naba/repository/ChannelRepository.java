package vn.nganluong.naba.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.entities.Channel;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Integer> {
//	@Query(value = "select b from Channel b where b.name like CONCAT('%',:keyName,'%')")
//	List<Channel> findAllByName(@Param("keyName") String name);

	Channel findByName(String name);

//	@Cacheable
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Channel findByCode(String code);

	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Channel findFirstByCodeAndStatus(String code, Boolean status);

	Optional<Channel> findById(Integer id);

	List<Channel> findAllByStatusOrderByIdAsc(Boolean status);

	List<Channel> findAllByStatusOrderByCodeAsc(Boolean status);

}