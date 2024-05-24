package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.CommonClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.QrcodeOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.QrcodeOCRResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * 1. @ClassName BarCodeTest
 * 2. @Description 测试条码
 * 3. @Author huo
 * 4. @Date 2024/5/21 20:37
 */
@SpringBootTest
public class BarCodeTest {

    @Test
    public void testJSON() throws JsonProcessingException {
        String jsonString = "{\"Response\":{\"CodeResults\":[{\"Position\":{\"LeftBottom\":{\"X\":0,\"Y\":92},\"LeftTop\":{\"X\":0,\"Y\":0},\"RightBottom\":{\"X\":329,\"Y\":92},\"RightTop\":{\"X\":329,\"Y\":0}},\"TypeName\":\"CODE_128\",\"Url\":\"11111111\"}],\"ImgSize\":{\"High\":115,\"Wide\":336},\"RequestId\":\"ec8ad89d-9ace-4297-8101-8429f0b65650\"}}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode codeResultsNode = rootNode.path("Response").path("CodeResults").get(0);
        String urlValue = codeResultsNode.path("Url").asText();
        System.out.println("Url对应的值为: " + urlValue);
    }
    @Test
    public void testBarCode(){
        try {
            Credential cred = new Credential("", "");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CommonClient client = new CommonClient("ocr", "2018-11-19", cred, "ap-beijing", clientProfile);
            String params = "{\"ImageBase64\":\"" + testBase64() + "\"}";

            String resp = client.call("QrcodeOCR", params);
            System.out.println(resp);
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }
    @Test
    public String testBase64 () {
        File file = new File("src/main/resources/barcode18.png");
        if (file.length() > 7 * 1024 * 1024) {
            System.out.println("图片大小超过7MB限制！");
        } else {
            try {
                byte[] imageBytes = Files.readAllBytes(Paths.get(file.toURI()));
                String base64ImageString = encodeImageToBase64(imageBytes);
                System.out.println(base64ImageString);
                return base64ImageString;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 在ImageToBase64类中添加此方法
    public static String encodeImageToBase64(byte[] imageBytes) {
        // 注意这里的 MyBase64.Encoder 是 java.util.Base64.Encoder 的简写形式
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
