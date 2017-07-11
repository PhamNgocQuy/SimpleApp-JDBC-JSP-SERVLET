package com.haku.utils;

import java.sql.Connection;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.haku.bean.UserAccount;

public class MyUtils {

	public static final String ATT_NAME_CONNECTION = "ATTRIBUTE_FOR_CONNECTION";

	private static final String ATT_NAME_USER_NAME = "ATTRIBUTE_FOR_STORE_USER_NAME_IN_COOKIE";

	/*
	 * Lưu trữ Connection vào một thuộc tính của request Thông tin lưu trữ chỉ tồn
	 * tại trong thời gian yêu cầu (request) cho tới khi dữ liệu được trả về trình
	 * duyệt người dùng
	 */
	public static void storeConnection(ServletRequest request, Connection conn) {
		request.setAttribute(ATT_NAME_CONNECTION, conn);
	}

	/*
	 * Lay doi tuong Connection da duoc luc tru
	 */
	public static Connection getStoreConnection(ServletRequest request) {
		return (Connection) request.getAttribute(ATT_NAME_CONNECTION);
	}

	/*
	 * Lưu trữ thông tin người dùng đã login vào Session
	 */
	public static void storeLoginedUser(HttpSession session, UserAccount loginedUser) {
		// Trên JSP có thể truy cập ${loginedUser}
		session.setAttribute("loginedUser", loginedUser);
	}

	/*
	 * Lấy thông tin người dùng đã login trong session.
	 */
	public static UserAccount getLoginedUser(HttpSession session) {
		UserAccount loginedUser = (UserAccount) session.getAttribute("loginedUser");
		return loginedUser;
	}

	/*
	 * Lưu thông tin người dùng vào Cookie.
	 */
	public static void storeUserCookie(HttpServletResponse response, UserAccount user) {
		System.out.println("Store user cookie");
		Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, user.getUserName());
		// 1 ngày (Đã đổi ra giây)
		cookieUserName.setMaxAge(24 * 60 * 60);
		response.addCookie(cookieUserName);
	}

	public static String getUserNameInCookie(HttpServletRequest request) {
		Cookie[] cookie = request.getCookies();
		if (cookie != null) {
			for (Cookie cookie2 : cookie) {
				if (ATT_NAME_USER_NAME.equals(cookie2.getName())) {
					return cookie2.getValue();
				}
			}
			
		}
		return null;

	}

	public static void deleteUserCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie(ATT_NAME_USER_NAME, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);

	}

}
