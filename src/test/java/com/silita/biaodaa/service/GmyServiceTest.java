package com.silita.biaodaa.service;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by 91567 on 2018/4/12.
 */
@WebAppConfiguration
public class GmyServiceTest extends ConfigTest {

    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Before()
    public void setup()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }


    @Test
    public void testController()throws Exception{
        String requestBody = "{\"type\":0,\"showType\":0}";
        String responseString = mockMvc.perform(get("/foundation/listBannerImage").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController2()throws Exception{
        String requestBody = "{\"userId\":\"dba718748d3e4a2096c1c948b77a77bd\",\"isSystem\":0,\"pageNo\":1,\"pageSize\":20}";
        String responseString = mockMvc.perform(post("/userCenter/listMessageByUserId").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController3()throws Exception{
        String requestBody = "{\"type\":1}";
        String responseString = mockMvc.perform(post("/foundation/listHotHotWords").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController4()throws Exception{
        String requestBody = "{\"invitationIp\":\"127.0.0.1\",\"invitationPhone\":\"18774987061\",\"type\":1}";
        String responseString = mockMvc.perform(post("/authorize/getVerificationCode").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController6()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "gmytest");
        jsonObject.put("userpass", "gmytest");
        jsonObject.put("userphone", "18774987061");
        jsonObject.put("invitationCode", "900007");
        jsonObject.put("version", "10100");
        jsonObject.put("loginchannel", "1002");
        String requestBody = jsonObject.toJSONString();
        String responseString = mockMvc.perform(post("/authorize/userRegister").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController7()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userphone", "18774987061");
        jsonObject.put("userpass", "gmytest");
        jsonObject.put("version", "10100");
        jsonObject.put("loginchannel", "1002");
        String requestBody = jsonObject.toJSONString();
        String responseString = mockMvc.perform(post("/authorize/userLogin").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController8()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "18774987061");
        jsonObject.put("userpass", "gmytest");
        jsonObject.put("userphone", "18774987061");
        jsonObject.put("invitationCode", "900007");
        jsonObject.put("wxopenid", "ixixiixix");
        jsonObject.put("wxUnionid", "xixixixix");
        jsonObject.put("version", "10100");
        jsonObject.put("loginchannel", "1002");
//        jsonObject.put("qqopenid", "ixixiixix");
        jsonObject.put("type", "1");
        jsonObject.put("nikename", "test");
        String requestBody = jsonObject.toJSONString();
        String responseString = mockMvc.perform(post("/authorize/thirdPartyBindingOrRegister").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController9()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", "a91f2c0ef897407f98086acfc5f538d2");
//        jsonObject.put("imgurl", "test.img");
//        jsonObject.put("nickname", "update");
        jsonObject.put("gender", "1");
        jsonObject.put("mailbox", "999999@qq.com");
//        jsonObject.put("companyname", "测试");
        String requestBody = jsonObject.toJSONString();
        String responseString = mockMvc.perform(post("/userCenter/updateUserTemp").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController10()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userpass", "123321");
        jsonObject.put("userid", "67edf8831ddf49008cf3161e1b92b743");
        jsonObject.put("userphone", "18774987061");
        jsonObject.put("invitationCode", "900007");
        jsonObject.put("version", "10100");
        jsonObject.put("loginchannel", "1002");

        String requestBody = jsonObject.toJSONString();
        String responseString = mockMvc.perform(post("/userCenter/updatePassWord").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController11()throws Exception{
        MockMultipartFile firstFile = new MockMultipartFile("files", "filename.txt", "text/plain", "some xml".getBytes());

        String responseString = mockMvc.perform(fileUpload("/userCenter/updateHeadPortrait").file(firstFile)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController12()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", "a91f2c0ef897407f98086acfc5f538d2");
        jsonObject.put("type", "2");
        jsonObject.put("noticeid", "666666");

        String requestBody = jsonObject.toJSONString();
        String responseString = mockMvc.perform(post("/userCenter/collectionNotice").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController13()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", "a91f2c0ef897407f98086acfc5f538d2");
        jsonObject.put("noticeid", "123456");

        String requestBody = jsonObject.toJSONString();
        String responseString = mockMvc.perform(post("/userCenter/cancelCollectionNotice").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController14()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", "8e3adebbe29d4e75afc5dedbd4b69752");
        jsonObject.put("type", "2");
        jsonObject.put("pageNo", 1);
        jsonObject.put("pageSize", 20);

        String requestBody = jsonObject.toJSONString();
        String responseString = mockMvc.perform(post("/userCenter/listCollectionNotice").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController15()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", "a91f2c0ef897407f98086acfc5f538d2");
        jsonObject.put("companyid", 6666);
        jsonObject.put("tablename", "new_huNan");

        String requestBody = jsonObject.toJSONString();
        String responseString = mockMvc.perform(post("/userCenter/collectionCompany").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController16()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", "a91f2c0ef897407f98086acfc5f538d2");
        jsonObject.put("companyid", 6666);
        jsonObject.put("tablename", "new_huNan");

        String requestBody = jsonObject.toJSONString();
        String responseString = mockMvc.perform(post("/userCenter/cancelCollectionCompany").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController18()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", "a91f2c0ef897407f98086acfc5f538d2");
        jsonObject.put("tablename", "new_huNan");
        jsonObject.put("pageNo", 1);
        jsonObject.put("pageSize", 20);

        String requestBody = jsonObject.toJSONString();
        String responseString = mockMvc.perform(post("/userCenter/listCollectionCompany").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController19()throws Exception{
        String requestBody = "{\"userId\":\"dba718748d3e4a2096c1c948b77a77bd\",\"isSystem\":0}";
        String responseString = mockMvc.perform(post("/userCenter/allReadedRecordToRead").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void testController20()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgId", 18466);
        jsonObject.put("userId", "dba718748d3e4a2096c1c948b77a77bd");

        String requestBody = jsonObject.toJSONString();
        String responseString = mockMvc.perform(post("/userCenter/getMessagePushById").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.getBytes())
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----返回的json = " + responseString);
    }

}
