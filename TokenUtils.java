package com.yqh.bbct.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyh in 2018/8/2 14:36
 **/
public class TokenUtils {
    /**
     * iss: jwt签发者

     sub: jwt所面向的用户

     aud: 接收jwt的一方

     exp: jwt的过期时间，这个过期时间必须要大于签发时间

     nbf: 定义在什么时间之前，该jwt都是不可用的.

     iat: jwt的签发时间

     jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
     */
    /**
     * APP登录Token的生成和解析
     *
     */

    /** token秘钥，请勿泄露，请勿随便修改 backups:JKKLJOoasdlfj */
    public static final String SECRET = "JKKLJOoasdlfj";
    /** token 过期时间: 10天 */
    public static final int calendarField = Calendar.DATE;
    public static final int calendarInterval = 10;

    /**
     * JWT生成Token.<br/>
     *
     * JWT构成: header, payload, signature
     *
     * @param userName
     *            登录成功后用户user_id, 参数user_id不可传空
     */
    public static String createToken(String userName) throws Exception {
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // build token
        // param backups {iss:Service, aud:APP}
        String token = JWT.create().withHeader(map) // header
                .withClaim("iss", "Service") // payload
                .withClaim("aud", "APP").withClaim("user_name", null == userName ? null : userName.toString())
                .withIssuedAt(iatDate) // sign time
                .withExpiresAt(expiresDate) // expire time
                .sign(Algorithm.HMAC256(SECRET)); // signature

        return token;
    }

    /**
     * 解密Token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            // e.printStackTrace();
            // token 校验失败, 抛出Token验证非法异常
        }
        return jwt.getClaims();
    }

    /**
     * 根据Token获取user_id
     *
     * @param token
     * @return user_id
     */
    public static String getAppUID(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim user_id_claim = claims.get("user_name");
        if (null == user_id_claim || StringUtils.isEmpty(user_id_claim.asString())) {
            // token 校验失败, 抛出Token验证非法异常
        }
        return user_id_claim.asString();
    }


    public static void main(String[] args)throws Exception {
//        String wyh = createToken("wyh");
//        System.out.println(wyh);

        String token ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBUFAiLCJ1c2VyX25hbWUiOiJ3eWgiLCJpc3MiOiJTZXJ2aWNlIiwiZXhwIjoxNTM1ODg3NDcyLCJpYXQiOjE1MzUwMjM0NzJ9.8hxmTSu56aHaxqgL6H0cti5n21qzl5p-4fUwQbRoygM";
        System.out.println(getAppUID(token));
    }
}