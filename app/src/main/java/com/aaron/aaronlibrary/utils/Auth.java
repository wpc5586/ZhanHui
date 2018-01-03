/*****************************************************************************
 * 东方国信手机经分项目[mobile_jf]
 *----------------------------------------------------------------------------
 * cn.com.bonc.jf.common.util.Auth.java
 *
 * @author andy
 * @date 2016年12月5日
 * @version 0.0.1
 * @since 0.0.1
 *----------------------------------------------------------------------------
 * (C) 北京东方国信科技股份有限公司
 *     Business-intelligence Of Oriental Nations Corporation Ltd. 2016
 *****************************************************************************/
package com.aaron.aaronlibrary.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 鉴权工具类
 * cn.com.bonc.jf.common.util.Auth.java
 * 
 * @author andy
 * @date 2016年12月5日
 *
 * @since 0.0.1
 */
public class Auth {

	private static String authSecretKey;
	private static String authExpireInterval;
	private static String authSeparator;
	
	static {
		authSecretKey = "bonc";
		authExpireInterval = "43200";
		authSeparator = "-";
	}
	
	/**
	 * 生成TOKEN
	 * 
	 * @param cityId
	 * @param userId
	 * @return String
	 * @author andy
	 */
	public static String createToken(String cityId, String userId){
		StringBuffer token = new StringBuffer();
		token.append(AES.encrypt(cityId, authSecretKey)+authSeparator);
		token.append(AES.encrypt(userId, authSecretKey)+authSeparator);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, Integer.valueOf(authExpireInterval));
		token.append(AES.encrypt(String.valueOf(calendar.getTimeInMillis()), authSecretKey));
		return token.toString();
	}
	
	/**
	 * 提取token信息
	 * 
	 * @param token
	 * @return List<String>
	 * @author andy
	 */
	public static List<String> getSubTokens(String token){
		List<String> subTokenList = new ArrayList<String>();
		String[] subTokens = token.split(authSeparator);
		for(String subToken : subTokens){
			subTokenList.add( AES.decrypt(subToken, authSecretKey));
		}
		return subTokenList;
	}
	
	/**
	 * 提取token信息
	 * 
	 * @param subTokenList
	 * @param index
	 * @return String
	 * @author andy
	 */
	private static String extractTokenInfo(List<String> subTokenList, Integer index){
		String tokenInfo = null;
		if(index < subTokenList.size()){
			tokenInfo = subTokenList.get(index);
		}
		return tokenInfo;
	}
	
	/**
	 * 获取城市id
	 * 
	 * @param subTokenList
	 * @return String
	 * @author andy
	 */
	public static String getCityId(List<String> subTokenList){
		return extractTokenInfo(subTokenList, 0);
	}
	
	/**
	 * 获取用户id
	 * 
	 * @param subTokenList
	 * @return String
	 * @author andy
	 */
	public static String getUserId(List<String> subTokenList){
		return extractTokenInfo(subTokenList, 1);
	}
	
	/**
	 * 获取时间戳
	 * 
	 * @param subTokenList
	 * @return Long
	 * @author andy
	 */
	public static Long getTimestamp(List<String> subTokenList){
		String timestamp = extractTokenInfo(subTokenList, 2);
		if(timestamp != null && timestamp.matches("\\d{13}")){
			return Long.valueOf(timestamp);
		} else {
			return null;
		}
	}
	
	/**
	 * 验证TOKEN有效性
	 * 
	 * @param subTokenList
	 * @return Boolean
	 * @author andy
	 */
	public static Boolean isValid(List<String> subTokenList){
		if(getTimestamp(subTokenList)!=null && getUserId(subTokenList)!=null && getCityId(subTokenList)!=null){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 验证TOKEN时效性
	 * 
	 * @param subTokenList
	 * @return Boolean
	 * @author andy
	 */
	public static Boolean isLive(List<String> subTokenList){
		Long timestamp = getTimestamp(subTokenList);
		if(timestamp!=null && timestamp> Calendar.getInstance().getTimeInMillis()){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 提取token信息
	 * 
	 * @param token
	 * @param index
	 * @return String
	 * @author andy
	 */
	private static String extractTokenInfo(String token, Integer index){
		String tokenInfo = null;
		String[] subTokens = token.split(authSeparator);
		if(index < subTokens.length){
			tokenInfo = AES.decrypt(subTokens[index], authSecretKey);
		}
		return tokenInfo;
	}
	
	/**
	 * 获取城市id
	 * 
	 * @param token
	 * @return String
	 * @author andy
	 */
	public static String getCityId(String token){
		return extractTokenInfo(token, 0);
	}
	
	/**
	 * 获取用户id
	 * 
	 * @param token
	 * @return String
	 * @author andy
	 */
	public static String getUserId(String token){
		return extractTokenInfo(token, 1);
	}
	
	/**
	 * 获取时间戳
	 * 
	 * @param token
	 * @return Long
	 * @author andy
	 */
	public static Long getTimestamp(String token){
		String timestamp = extractTokenInfo(token, 2);
		if(timestamp != null && timestamp.matches("\\d{13}")){
			return Long.valueOf(timestamp);
		} else {
			return null;
		}
	}
	
	/**
	 * 验证TOKEN有效性
	 * 
	 * @param token
	 * @return Boolean
	 * @author andy
	 */
	public static Boolean isValid(String token){
		if(getTimestamp(token)!=null && getUserId(token)!=null && getCityId(token)!=null){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 验证TOKEN时效性
	 * 
	 * @param token
	 * @return Boolean
	 * @author andy
	 */
	public static Boolean isLive(String token){
		Long timestamp = getTimestamp(token);
		if(timestamp!=null && timestamp> Calendar.getInstance().getTimeInMillis()){
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
        String cityId = Auth.getCityId("6241823a70dd82564b04ded7eab42667-b2a1fa66d0d1962e277679818b2fee72-eb9a677015cc936fda9dc97fdf6a1625");
        String userId = Auth.getUserId("6241823a70dd82564b04ded7eab42667-b2a1fa66d0d1962e277679818b2fee72-eb9a677015cc936fda9dc97fdf6a1625");
        System.out.println(cityId+"_"+userId);
    }
	
}
