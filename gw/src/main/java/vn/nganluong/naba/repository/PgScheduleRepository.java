package vn.nganluong.naba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.entities.PgMail;
import vn.nganluong.naba.entities.PgSchedule;

@Repository
public interface PgScheduleRepository extends JpaRepository<PgSchedule, Integer> {

    //@Query("SELECT u FROM PgSchedule u WHERE u.channelId = :channelId AND u.pgFunctionId = :pgFunctionId and u.status = 1 limit 1")
    PgSchedule findFirstByChannelIdAndPgFunctionIdAndStatus(int channelId, Integer pgFunctionId, Boolean status);

}