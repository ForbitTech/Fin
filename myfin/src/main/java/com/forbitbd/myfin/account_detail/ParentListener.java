package com.forbitbd.myfin.account_detail;

import com.forbitbd.myfin.models.TransactionResponse;

import java.util.List;

public interface ParentListener {
    List<TransactionResponse> getTransaction();
}
