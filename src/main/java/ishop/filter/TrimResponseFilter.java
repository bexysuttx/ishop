package ishop.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.FilterChain;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import javax.servlet.WriteListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

@WebFilter(filterName = "TrimResponseFilter")
public class TrimResponseFilter extends AbstractFilter {

	@Override
	public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse originalResponse = resp;
		TrimResponse response = new TrimResponse(originalResponse);
		chain.doFilter(req, response);
		PrintWriter pw = originalResponse.getWriter();
		String content = response.getTrimContent();
		pw.write(content);
		originalResponse.setContentLength(content.length());
		pw.flush();
		pw.close();
	}

	private static class TrimResponse extends HttpServletResponseWrapper {
		private StringWriter sw = new StringWriter();

		private TrimResponse(HttpServletResponse response) {
			super(response);
		}

		public String getTrimContent() {
			return trim(sw.toString());
		}

		private String trim(String string) {
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < string.length(); i++) {
				char ch = string.charAt(i);
				if (ch != '\t' && ch != '\r' && ch != '\n') {
					s.append(ch);
				}
			}
			return s.toString();
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			return new PrintWriter(sw);
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			return new ServletOutputStream() {

				@Override
				public void write(int b) throws IOException {
					sw.write(b);

				}

				@Override
				public void setWriteListener(WriteListener writeListener) {

				}

				@Override
				public boolean isReady() {
					return false;
				}
			};
		}

	}

}
