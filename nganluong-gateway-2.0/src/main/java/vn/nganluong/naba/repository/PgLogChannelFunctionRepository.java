package vn.nganluong.naba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.entities.PgLogChannelFunction;

@Repository
public interface PgLogChannelFunctionRepository extends JpaRepository<PgLogChannelFunction, Integer> {

//	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") }) 
	@Query("SELECT u FROM PgLogChannelFunction u WHERE u.channelFunction.id = :channel_function_id")
	PgLogChannelFunction findByChannelFunctionId(@Param("channel_function_id") Integer channelFunctionId);
	
}