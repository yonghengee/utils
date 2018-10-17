package com.yqh.shop.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * Cookie 工具类
 */
public final class CookieUtils {

    /**
     * 获取指定的cookie
     * @param request
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie targetCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    targetCookie = cookie;
                    break;
                }
            }
        }
        return targetCookie;
    }

    /**
     * 得到Cookie的值, 不编码
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request,
                                        String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * 得到Cookie的值,
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request,
                                        String cookieName, boolean isDecoder) {
        Cookie cookieList[] = request.getCookies();
        if (cookieList == null || cookieName == null)
            return null;
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValue = URLDecoder.decode(cookieList[i].getValue(),
                                "utf-8");
                    } else {
                        retValue = cookieList[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            //logger.error("Cookie Decode Error.", e);
        }
        return retValue;
    }

    /**
     * 得到Cookie的值,
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request,
                                        String cookieName, String encodeString) {
        Cookie cookieList[] = request.getCookies();
        if (cookieList == null || cookieName == null)
            return null;
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {

                    retValue = URLDecoder.decode(cookieList[i].getValue(),
                            encodeString);

                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            //logger.error("Cookie Decode Error.", e);
        }
        return retValue;
    }

    /**
     * 设置Cookie的值 不设置生效时间默认浏览器关闭即失效,也不编码
     */
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response, String cookieName, String cookieValue, String domainName) {
        setCookie(request, response, cookieName, cookieValue, domainName, -1);
    }

    /**
     * 设置Cookie的值 在指定时间内生效,但不编码
     */
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response, String cookieName,
                                 String cookieValue, String domainName, int cookieMaxage) {
        setCookie(request, response, cookieName, cookieValue, domainName, cookieMaxage,
                false);
    }

    /**
     * 设置Cookie的值 不设置生效时间,但编码
     */
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response, String cookieName,
                                 String cookieValue, String domainName, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, domainName, -1, isEncode);
    }

    /**
     * 设置Cookie的值 在指定时间内生效, 编码参数
     */
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response, String cookieName,
                                 String cookieValue, String domainName, int cookieMaxage, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, domainName, cookieMaxage,
                isEncode);
    }

    /**
     * 设置Cookie的值 在指定时间内生效, 编码参数(指定编码)
     */
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response, String cookieName,
                                 String cookieValue, String domainName, int cookieMaxage, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, domainName, cookieMaxage,
                encodeString);
    }

    /**
     * 删除Cookie带cookie域名
     */
    public static void deleteCookie(HttpServletRequest request,
                                    HttpServletResponse response, String cookieName, String domainName) {
        doSetCookie(request, response, cookieName, "", domainName, 0 , false);
    }

    /**
     * 设置Cookie的值，并使其在指定时间内生效
     *
     * @param cookieMaxage cookie生效的最大秒数
     */
    private static final void doSetCookie(HttpServletRequest request,
                                          HttpServletResponse response, String cookieName,
                                          String cookieValue, String domainName, int cookieMaxage, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            cookie.setMaxAge(cookieMaxage);
            if(null != domainName){
            	 cookie.setDomain(domainName);
            }
//            if (null != request)// 设置域名的cookie
//                cookie.setDomain(getDomainName(request));
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            //logger.error("Cookie Encode Error.", e);
        }
    }

    /**
     * 设置Cookie的值，并使其在指定时间内生效
     *
     * @param cookieMaxage cookie生效的最大秒数
     */
    private static final void doSetCookie(HttpServletRequest request,
                                          HttpServletResponse response, String cookieName,
                                          String cookieValue, String domainName,
                                          int cookieMaxage, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage > 0)
                cookie.setMaxAge(cookieMaxage);
            if (cookieName == null) {// 设置域名的cookie
                cookie.setDomain(getDomainName(request));
            } else {
                cookie.setDomain(domainName);
            }
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            //logger.error("Cookie Encode Error.", e);
        }
    }

    /**
     * 得到cookie的域名
     */
    private static final String getDomainName(HttpServletRequest request) {
        String domainName = null;

        String serverName = request.getRequestURL().toString();
        if (serverName == null || serverName.equals("")) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                // www.xxx.com.cn
                domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                // xxx.com or xxx.cn
                domainName = "." + domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        if (domainName != null && domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
        return domainName;
    }

}