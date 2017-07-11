package com.haku.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.haku.bean.UserAccount;
import com.haku.conn.MysqlConnUtils;
import com.haku.utils.DBUtils;
import com.haku.utils.MyUtils;

/**
 * Servlet implementation class DologinServlet
 */
@WebServlet("/doLogin")
public class DologinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DologinServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("userName");
		String password = request.getParameter("password");
		String rememberMe = request.getParameter("rememberMe");
		boolean remember = "Y".equals(rememberMe);

		UserAccount user = null;
		boolean hasError = false;
		String errorString = null;

		if (username == null || password == null || password.split(" ").length == 0 || username.split("").length == 0) {
			hasError = true;
			errorString = " Error at username and password";
		} else {

			try {
				Connection conn;
				conn = MysqlConnUtils.getMysqlConnection();
				/*
				 * Tim user trong database, neu khong co => user ko ton tai
				 */
				System.out.println("Check user");
				user = DBUtils.findUser(conn,password,username);
				if (user == null) {
					hasError = true;
					errorString = "uername or password invalid";
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				hasError = true;
				errorString = e.getMessage();
			}

		}
		/*
		 * hasError = true => forward ve LoginView.jsp
		 */
		if (hasError) {
			user = new UserAccount();
			user.setUserName(username);
			user.setPassword(password);

			request.setAttribute("errorString", errorString);
			request.setAttribute("user", user);

			RequestDispatcher dispatcher = this.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/loginView.jsp");
			dispatcher.forward(request, response);
		}

		else {
			HttpSession session = request.getSession();
			MyUtils.storeLoginedUser(session, user);
			if (remember) {
				MyUtils.storeUserCookie(response, user);
			} else {
				MyUtils.deleteUserCookie(response);
			}
			response.sendRedirect(request.getContextPath() + "/userInfo");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
