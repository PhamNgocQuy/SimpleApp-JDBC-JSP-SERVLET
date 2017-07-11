package com.haku.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.haku.conn.ConnectionUtils;
import com.haku.utils.MyUtils;

@WebFilter(filterName = "jdbcFilter", urlPatterns = "/*")
public class JDBCFilter implements Filter {

	public JDBCFilter() {
	}

	/*
	 * Kiểm tra xem request hiện tại là 1 Servlet?
	 */
	private boolean needJDBC(HttpServletRequest request) {
		System.out.println("JDBC Filter");

		/*
		 * Servlet Url-pattern: /spath/* =>/spath
		 */

		String servletPath = request.getServletPath();
		System.out.println("servletPath: " + servletPath);
		/*
		 * => /abc/mnp
		 */

		String pathInfo = request.getPathInfo();
		System.out.println("pathInfo: " + pathInfo);

		String urlPattern = servletPath;
		if (pathInfo != null) {
			/*
			 * =>/spath/*
			 */
			urlPattern = servletPath + "/*";
		}
		/*
		 * Key: servletName. Value: ServletRegistration
		 */
		Map<String, ? extends ServletRegistration> servletRegistration = request.getServletContext()
				.getServletRegistrations();

		/*
		 * Tập hợp tất cả các Servlet trong WebApp của bạn.
		 */
		Collection<? extends ServletRegistration> values = servletRegistration.values();
		for (ServletRegistration sr : values) {
			Collection<String> mappings = sr.getMappings();
			if (mappings.contains(urlPattern)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		/*
		 * Chỉ mở kết nối đối với các request có đường dẫn đặc biệt cần connection.
		 * (Chẳng hạn đường dẫn tới các servlet, jsp, ..) Tránh tình trạng mở connection
		 * với các yêu cầu thông thường (chẳng hạn image, css, javascript,... )
		 */

		Connection connection = null;
		if (this.needJDBC(req)) {
			try {
				connection = ConnectionUtils.getConnection();
				connection.setAutoCommit(false);

				/*
				 * store attribute of request
				 */
				MyUtils.storeConnection(request, connection);

				/*
				 * cho phép request đi tiếp.
				 */
				chain.doFilter(request, response);

				connection.commit();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				ConnectionUtils.closeQuietly(connection);
			}
			/*
			 * Với các request thông thường (image,css,html,..) không cần mở connection, cho
			 * tiếp tục.
			 */
		} else {
			chain.doFilter(request, response);
		}
		
		

	}

}
