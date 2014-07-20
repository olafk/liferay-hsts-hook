package de.olafkock.liferay.hsts;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class HSTSFilter implements Filter {

	private static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filter) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		// only set the header once. response.containsHeader gave false negatives, 
		// working around this with an extra request attribute
		if(servletRequest.isSecure() && servletRequest.getAttribute(STRICT_TRANSPORT_SECURITY)==null) {
			String header = getHeader(servletRequest);
			if(header != null) {
				try {
					response.addHeader(STRICT_TRANSPORT_SECURITY, header);
					servletRequest.setAttribute(STRICT_TRANSPORT_SECURITY, header);
				} catch (Exception ignore) {
					_log.error("ignored exception in HSTS Filter. No header added. (" + 
							ignore.getClass().getName() + " " + ignore.getMessage() + ")");
				}
			} else {
				if(_log.isDebugEnabled())
					_log.debug("request is insecure or HSTS not configured");
			}
		}
		filter.doFilter(servletRequest, servletResponse);
	}

	private String getHeader(ServletRequest request) {
		long timeout = 0;
		String result = null;
		Long companyId = (Long) request.getAttribute("COMPANY_ID");
		if(companyId != null) {
			Object user = request.getAttribute("USER");
			try {
				if(user == null) {
					timeout = getAnonymousTimeout(companyId);
				} else {
					timeout = getUserTimeout(companyId);
				}
				if(timeout > 0L) {
					result = "max-age=" + timeout;
					boolean includeSubdomain = PrefsPropsUtil.getBoolean(companyId, "hsts.include.subdomain", false);
					if(includeSubdomain) {
						result += "; includeSubDomains";
					}
				}				
				if(_log.isTraceEnabled())
					_log.trace(STRICT_TRANSPORT_SECURITY + " " + result + " in company " + companyId);
			} catch (SystemException e) {
				_log.error(e);
			}
		}
		return  result;
	}

	private long getUserTimeout(Long companyId) throws SystemException {
		// investigate if it's worth to cache these values
		long timeout = PrefsPropsUtil.getLong(companyId, "hsts.user", 0L);
		return timeout;
	}

	private long getAnonymousTimeout(Long companyId) throws SystemException {
		// investigate if it's worth to cache these values
		long timeout = PrefsPropsUtil.getLong(companyId, "hsts.all", 0L);
		return timeout;
	}

	private static Log _log = LogFactoryUtil.getLog(HSTSFilter.class);
}
