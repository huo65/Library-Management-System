package com.example.demo.controller;

import com.example.demo.commom.Result;
import com.example.demo.entity.Codeinfo;
import com.example.demo.entity.VO.ReturnInfoVO;
import com.example.demo.service.CodeinfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.common.CommonClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 1. @ClassName BarCodeController
 * 2. @Description 条形码信息识别
 * 3. @Author huo
 * 4. @Date 2024/5/24 8:43
 */
@RestController
@RequestMapping("/barCode")
public class BarCodeController {
    @Resource
    CodeinfoService codeinfoService;

    /**
     * 获取二维码信息,用于加书
     * @param base64
     * @return
     */
    @PostMapping("/getId")
    public Result<Codeinfo> scanBarCode(@RequestBody String base64){
        try {
            Credential cred = new Credential("", "");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CommonClient client = new CommonClient("ocr", "2018-11-19", cred, "ap-beijing", clientProfile);
            String params = "{\"ImageBase64\":\"" + base64 + "\"}";

            String resp = client.call("QrcodeOCR", params);
            System.out.println(resp);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(resp);
            JsonNode codeResultsNode = rootNode.path("Response").path("CodeResults").get(0);
            String urlValue = codeResultsNode.path("Url").asText();
            System.out.println("Url对应的值为: " + urlValue);
            Codeinfo codeinfo = codeinfoService.getById(urlValue);
            return Result.success(codeinfo);
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Result.error("-1","识别失败");
    }

    /**
     * 获取二维码信息，用于还书
     * @param base64
     * @return
     */
    @PostMapping("/getReturnInfo")
    public Result<ReturnInfoVO> returnInfo(@RequestBody String base64){
        try {
            Credential cred = new Credential("", "");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CommonClient client = new CommonClient("ocr", "2018-11-19", cred, "ap-beijing", clientProfile);
            String params = "{\"ImageBase64\":\"" + base64 + "\"}";

            String resp = client.call("QrcodeOCR", params);
            System.out.println(resp);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(resp);
            JsonNode codeResultsNode = rootNode.path("Response").path("CodeResults").get(0);
            String urlValue = codeResultsNode.path("Url").asText();
            System.out.println("Url对应的值为: " + urlValue);
            Codeinfo codeinfo = codeinfoService.getById(urlValue);
//            TODO 待完善
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Result.error("-1","识别失败");
    }


}
