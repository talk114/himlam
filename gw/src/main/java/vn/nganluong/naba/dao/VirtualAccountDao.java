package vn.nganluong.naba.dao;

import vn.nganluong.naba.dto.VirtualAccountDto;
import vn.nganluong.naba.entities.VirtualAccount;

public interface VirtualAccountDao {

	public boolean createVirtualAccount(VirtualAccountDto virtualAccountDto);
	
	public VirtualAccount findVirtualAccount(String virtualAccountCode);

}