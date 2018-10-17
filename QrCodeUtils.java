package com.yqh.shop.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.yqh.component.utils.PropUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QrCodeUtils {

    private static Logger logger = LogManager.getLogger(QrCodeUtils.class.getName());

    public static void main(String[] args) throws Exception {
//

//        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ PropUtils.getValue("APPID").trim()+"&secret="+PropUtils.getValue("APPSECRET").trim();
/*        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + PropUtils.getValue("TEST_APPID").trim() + "&secret=" + PropUtils.getValue("TEST_APPSECRET").trim();
        String resultJson = HttpClientUtils.doPost(url);
        //解析获取的accessToken,Json格式
        JSONObject jsonObject = JSON.parseObject(resultJson);
        String accessToken = jsonObject.getString("access_token");
        System.out.println(accessToken);
        url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;

//       {"action_name":"QR_LIMIT_STR_SCENE", "action_info": {"scene":{"scene_str":"test"}}}

        JSONObject params = new JSONObject();
        JSONObject scene = new JSONObject();
        JSONObject scene_str = new JSONObject();

        scene_str.put("scene_str", "test");
        scene.put("scene", scene_str);

        params.put("action_name", "QR_LIMIT_STR_SCENE");
        params.put("action_info", scene);

        String result = HttpClientUtils.doPostJSON(url, params);

        System.out.println(result);
        JSONObject jsonObject2 = JSON.parseObject(result);
        if (jsonObject2.get("ticket") != null) {
            String ticket = jsonObject2.getString("ticket");
            String expire_seconds = jsonObject2.getString("expire_seconds");
            String reurl = jsonObject2.getString("url");
            System.out.println(ticket);
            System.out.println(expire_seconds);
            System.out.println(reurl);

//           String urll ="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
//           WeixinUtils.showqrcode(ticket);
            String filePath = "D://";

            String fileName = "1111111111111.png";
            encode(filePath, reurl, fileName);
        }*/
//        String filePath = "D://";
//        String url = "http://test-web.baichida.com/#/?recommendMobile=13538920684";
//        String fileName = "7114622d041043fd943ce1ec6fd16685.png";
//        encode(filePath,url,fileName);
//        decode();
        encode("E:\\log","https://www.baidu.com/","photo");
    }

    /**
     * 生成二维码
     *
     * @throws WriterException
     * @throws IOException
     */
    public static void encode(String outPath, String url, String fileName) throws WriterException, IOException {
        String filePath = outPath;
//        String filePath = "D://";
        fileName = fileName + ".png";
        String content = url;

        int width = 300; // 图像宽度
        int height = 300; // 图像高度
        String format = "png";// 图像类型
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
        Path path = FileSystems.getDefault().getPath(filePath, fileName);
//        logger.debug("path:"+path);
//        logger.debug("bitMatrix:"+bitMatrix);
        MatrixToImageWriter.writeToPath(bitMatrix, format, path);// 输出图像
        System.out.println("输出成功.");
    }


    /**
     * 解析二维码
     */
    public static void decode() {
        String filePath = "D://zxing.png";
        BufferedImage image;
        try {
            image = ImageIO.read(new File(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码
            System.out.println("图片中内容：  ");
            System.out.println("author： " + result.getText());
            System.out.println("图片中格式：  ");
            System.out.println("encode： " + result.getBarcodeFormat());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建永久二维码(字符串)
     * @param
     * @param
     * @return
     */
    public static void createForeverTicket(String sceneStr, String outPath, String fileName)throws WriterException, IOException  {
//        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
//                + PropUtils.getValue("TEST_APPID").trim() + "&secret=" + PropUtils.getValue("TEST_APPSECRET").trim();
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + PropUtils.getValue("APPID").trim() + "&secret=" + PropUtils.getValue("APPSECRET").trim();
        String resultJson = HttpClientUtils.doPost(url);
        //解析获取的accessToken,Json格式
        System.out.println("resultJson:"+resultJson);
        JSONObject jsonObject = JSON.parseObject(resultJson);
        String accessToken = jsonObject.getString("access_token");
        System.out.println("accessToken:"+accessToken);
        url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;

//       {"action_name":"QR_LIMIT_STR_SCENE", "action_info": {"scene":{"scene_str":"test"}}}

        JSONObject params = new JSONObject();
        JSONObject scene = new JSONObject();
        JSONObject scene_str = new JSONObject();

        scene_str.put("scene_str",sceneStr);
        scene.put("scene", scene_str);
//        JSONObject scene_id = new JSONObject();

//        scene_id.put("scene_id", sceneId);
//        scene.put("scene", scene_id);

        params.put("action_name", "QR_LIMIT_STR_SCENE");
        params.put("action_info", scene);
//        params.put("action_name", "QR_LIMIT_SCENE");
//        params.put("action_info", scene);

        String result = HttpClientUtils.doPostJSON(url, params);

        System.out.println(result);
        JSONObject jsonObject2 = JSON.parseObject(result);
        if (jsonObject2.get("ticket") != null) {
            String ticket = jsonObject2.getString("ticket");
            String reurl = jsonObject2.getString("url");
            System.out.println(ticket);
            System.out.println(reurl);

            encode(outPath, reurl, fileName);
        }
    }

}