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

import com.haku.bean.Product;
import com.haku.utils.DBUtils;
import com.haku.utils.MyUtils;

/**
 * Servlet implementation class DoCreateProductServlet
 */
@WebServlet("/doCreateProduct")
public class DoCreateProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DoCreateProductServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connection conn = MyUtils.getStoreConnection(request);

		String code = (String) request.getParameter("code");
		String name = (String) request.getParameter("name");
		String priceStr = (String) request.getParameter("price");

		float price = 0;
		try {
			price = Float.parseFloat(priceStr);
		} catch (Exception e) {
		}
		
		Product product = new Product(code, name, price);
		 
	      String errorString = null;
	 
	      // Mã sản phẩm phải là chuỗi chữ [a-zA-Z_0-9]
	      // Có ít nhất một ký tự.
	      String regex = "\\w+";
	 
	      if (code == null || !code.matches(regex)) {
	          errorString = "Product Code invalid!";
	      }
	      
	      /*
	       * 
	       */
	      if (errorString == null) {
	          try {
	              DBUtils.insertProduct(conn, product);
	          } catch (SQLException e) {
	              e.printStackTrace();
	              errorString = e.getMessage();
	          }
	      }
	      
	      /*
	       *  Luu thong tin trc khi sang view
	       */
	      request.setAttribute("errorString", errorString);
	      request.setAttribute("product", product);
	 
	      /*
	       * Co loi : forward => trang edit
	       */
	      if (errorString != null) {
	          RequestDispatcher dispatcher = request.getServletContext()
	                  .getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
	          dispatcher.forward(request, response);
	      }
	      /*
	       * Khong co loi => ve trang san pham
	       */
	      else {
	          response.sendRedirect(request.getContextPath() + "/productList");
	      }
	 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
