package com.test.testcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.base.BaseCase;
import com.test.data.GlobalEnvironment;
import com.test.pojo.CaseInfo;
import com.test.utils.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Description: 添加项目
 * @ClassName: AddLoan
 * @Author: Ming         // 创建者
 * @Date: 2020/12/27 22:35   // 时间
 * @Version: 1.0     // 版本
 */
public class AddLoan extends BaseCase {
    List<CaseInfo> caseInfoList;
    @BeforeClass
    public void setUp() {
        //从excel读取login需要的case
        caseInfoList = getCaseData(4);
        //参数化替换
        caseInfoList=paramsReplace(caseInfoList);
    }

    @Test(dataProvider = "getADDLoanData")
    public void testGetADDLoan(CaseInfo caseInfo)  {
        Map headerMap = jsonToMap(caseInfo.getRequestHeader());
        //获取响应结果
        Response response =
                given().
                        config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL))).
                        headers(headerMap).
                        body(caseInfo.getInputParams()).
                        when().
                        post(Constants.BASE_URL + caseInfo.getUrl()).
                        then().log().all().
                        extract().response();
        assertExpected(caseInfo,response);
        //获取项目id，存贮下来
        if (response.path("data.id")!=null){
            GlobalEnvironment.envData.put("loan_id",response.path("data.id"));
        }
    }

    @DataProvider
    public Object[] getADDLoanData() {
        return caseInfoList.toArray();
    }
}
