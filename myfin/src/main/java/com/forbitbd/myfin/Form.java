package com.forbitbd.myfin;

public interface Form {

    void clearPreError();
    void showValidationError(String message,int fieldId);
}
