package com.test.testcase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.base.BaseCase;
import com.test.pojo.CaseInfo;
import com.test.utils.Constants;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Description: 充值
 * @ClassName: Recharge
 * @Author: Ming         // 创建者
 * @Date: 2020/12/27 20:30   // 时间
 * @Version: 1.0     // 版本
 */
public class Recharge extends BaseCase {
    List<CaseInfo> caseInfoList;
    @BeforeClass
    public void setUp() {
        caseInfoList = getCaseData(3);
        //参数化替换
        caseInfoList=paramsReplace(caseInfoList);
    }
    @Test(dataProvider="getRechargeData")
    public void testRecharge(CaseInfo caseInfo)  {
        Map headerMap = jsonToMap(caseInfo.getRequestHeader());
        Response res = given().
                //当json返回小数时，用BigDecimal存储
                config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL))).
                headers(headerMap).
                body(caseInfo.getInputParams()).
                when().
                post(Constants.BASE_URL + caseInfo.getUrl()).
                then().log().all().extract().response();

        assertExpected(caseInfo,res);
    }
    @DataProvider
    public Object[] getRechargeData() {
        return caseInfoList.toArray();
    }
}
