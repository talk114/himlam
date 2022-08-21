package vn.nganluong.naba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.nganluong.naba.entities.PaymentAccount;

import javax.persistence.QueryHint;

@Repository
public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Integer> {


    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("SELECT u FROM PaymentAccount u WHERE u.userId = :user_id and u.channelId = :channel_id and u.status = 1")
    PaymentAccount findTopByUserIdAndChannelId(@Param("user_id") Integer user_id, @Param("channel_id") Integer channelId);

    PaymentAccount findFirstByMerchantId(String code);
}