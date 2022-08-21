package vn.nganluong.naba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.entities.VirtualAccount;

@Repository
public interface VirtualAccountRepository extends JpaRepository<VirtualAccount, Integer> {

}