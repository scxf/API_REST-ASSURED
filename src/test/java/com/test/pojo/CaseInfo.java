package com.test.pojo;
import cn.afterturn.easypoi.excel.annotation.Excel;
import javax.validation.constraints.NotNull;

/**
 * easyPOI映射实体类，类中的属性需要和Excel表头保持一致
 */
public class CaseInfo {
    @Excel(name="序号(caseId)")
    @NotNull
    private int caseId;

    @Excel(name="接口模块(interface)")
    @NotNull
    private String interfaceName;

    @Excel(name = "用例标题(title)")
    @NotNull
    private String title;

    @Excel(name = "请求头(requestHeader)")
    @NotNull
    private String requestHeader;

    @Excel(name = "请求方式(method)")
    @NotNull
    private String method;

    @Excel(name="接口地址(url)")
    @NotNull
    private String url;

    @Excel(name="参数输入(inputParams)")
    @NotNull
    private String inputParams;

    @Excel(name="期望返回结果(expected)")
    @NotNull
    private String expected;

    @Excel(name ="数据库校验(checkSQL)")
    @NotNull
    private String checkSQL;

    public String getCheckSQL() {
        return checkSQL;
    }
    public void setCheckSQL(String checkSQL) {
        this.checkSQL = checkSQL;
    }
    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInputParams() {
        return inputParams;
    }

    public void setInputParams(String inputParams) {
        this.inputParams = inputParams;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    @Override
    public String toString() {
        return "CaseInfo{" +
                "caseId=" + caseId +
                ", interfaceName='" + interfaceName + '\'' +
                ", title='" + title + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", inputParams='" + inputParams + '\'' +
                ", expected='" + expected + '\'' +
                ", checkSQL='" + checkSQL + '\'' +
                '}';
    }
}
