package vn.nganluong.naba.service;

import vn.nganluong.naba.dto.VirtualAccountDto;
import vn.nganluong.naba.entities.VirtualAccount;

public interface VirtualAccountService {

    public boolean createVirtualAccount(VirtualAccountDto virtualAccountDto);

    public boolean deleteVirtualAccount(String virtualAccountNo);

    public boolean updateStatusVirtualAccount(String virtualAccountNo, boolean status);

    public VirtualAccount findVirtualAccount(String virtualAccountCode);

    public boolean isExistVirtualAccount(String virtualAccountCode);

    void updateVirtualAccount(VirtualAccount virtualAccount);
}