package vn.nganluong.naba.repository;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

@Repository
public interface ChannelFunctionRepository extends JpaRepository<ChannelFunction, Integer> {
//	@Query(value = "select b from Channel b where b.name like CONCAT('%',:keyName,'%')")
//	List<Channel> findAllByName(@Param("keyName") String name);

    //	Channel findByName(String name);
//
//	Channel findByCode(String code);
//
//	Channel findById(int id);
//	
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("SELECT u FROM ChannelFunction u WHERE u.channel.id = :channel_id and u.code = :code and u.status = 1")
    ChannelFunction findByCodeAndChannelId(@Param("code") String code, @Param("channel_id") Integer channelId);


    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    ChannelFunction findTopByNameAndStatus(String name, Boolean status);
}