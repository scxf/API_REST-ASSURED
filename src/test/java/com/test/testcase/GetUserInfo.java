package com.test.testcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.base.BaseCase;
import com.test.pojo.CaseInfo;
import com.test.utils.Constants;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;
public class GetUserInfo extends BaseCase {
     List<CaseInfo> caseInfoList;
    @BeforeClass
    public void setUp() {
        //从excel读取login需要的case
        caseInfoList = getCaseData(2);
        //参数化替换
        caseInfoList=paramsReplace(caseInfoList);
    }

    @Test(dataProvider = "getUserInfoData")
    public void testGetUserInfo(CaseInfo caseInfo){
        Map headerMap = jsonToMap(caseInfo.getRequestHeader());
        //获取响应结果
        Response response =
                given().
                        headers(headerMap).
                        when().
                        get(Constants.BASE_URL + caseInfo.getUrl()).
                        then().log().all().
                        extract().response();
      assertExpected(caseInfo,response);
    }
    @DataProvider
    public Object[] getUserInfoData() {
        return caseInfoList.toArray();
    }

}

