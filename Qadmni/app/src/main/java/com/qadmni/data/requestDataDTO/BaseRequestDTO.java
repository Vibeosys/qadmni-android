package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by shrinivas on 12-01-2017.
 */
public class BaseRequestDTO extends BaseDTO {
    private String user;
    private String data;
    private String langCode;

    public BaseRequestDTO() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }


}
