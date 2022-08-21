package vn.nganluong.naba.channel.vib.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import vn.nganluong.naba.dto.ResponseJson;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class GetTransactionHistoryGWResponse extends ResponseJson implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = -1846926851668145554L;
    private int total_record;

    @JsonProperty(value = "list_transaction")
    private List<ListTransactionHistoryResponse> listTrans;


    public List<ListTransactionHistoryResponse> getListTrans() {
        return listTrans;
    }


    public void setListTrans(List<ListTransactionHistoryResponse> listTrans) {
        this.listTrans = listTrans;
    }

    public int getTotal_record() {
        return total_record;
    }

    public void setTotal_record(int total_record) {
        this.total_record = total_record;
    }
}
