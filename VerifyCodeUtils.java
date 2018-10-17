package com.yqh.shop.utils;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yqh.component.utils.CookieUtils;
import com.yqh.component.utils.DateUtils;
import com.yqh.shop.common.Constant;

public class VerifyCodeUtils {
	
    public static String getVerifyCode(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        //  HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //  HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Map<String, Object> map =  com.yqh.component.utils.VerifyCodeUtils.verifyCode();
        String randomCode = (String) map.get("randomCode");
        BufferedImage buffImg = (BufferedImage) map.get("buffImg");
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
        return randomCode;
    }

    public static String getVerifyCodeForOil(HttpServletRequest request, HttpServletResponse response, String userId)
            throws ServletException, java.io.IOException {
        //  HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //  HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Map<String, Object> map = com.yqh.component.utils.VerifyCodeUtils.verifyCode();
        String randomCode = (String) map.get("randomCode");
        BufferedImage buffImg = (BufferedImage) map.get("buffImg");
        // 将四位数字字母的验证码保存到cookies中。
        CookieUtils.setCookie(request, response, "randomCode", randomCode, null, 60);
        // 将四位数字字母的验证码保存到redis中。
        RedisUtils.getInstance().setex(Constant.VERIFYCODE+"_"+userId, randomCode, 60);
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
        return randomCode;
    }

    public static String getVerifyCodeForLogin(HttpServletRequest request, HttpServletResponse response,String key)
            throws ServletException, java.io.IOException {
        //  HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //  HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Map<String, Object> map = com.yqh.component.utils.VerifyCodeUtils.verifyCode();
        String randomCode = (String) map.get("randomCode");
        BufferedImage buffImg = (BufferedImage) map.get("buffImg");
        // 将四位数字字母的验证码保存到cookies中。
//        String key = DateUtils.formatDateToString(new Date(), com.yqh.component.utils.DateUtils.DATE_FORMAT_YMDHMS);
        CookieUtils.setCookie(request, response, "randomCode", randomCode, null, 60);
//        CookieUtils.setCookie(request, response, "key", key, null, 60);
        // 将四位数字字母的验证码保存到redis中。
        key = key.substring(0,key.indexOf("?"));
        RedisUtils.getInstance().setex(Constant.VERIFYCODE+"_"+key, randomCode, 60);
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
        return randomCode;
    }
}
