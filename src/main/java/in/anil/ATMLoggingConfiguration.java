package in.anil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Configuration
@ConditionalOnProperty(prefix = "default", name = "endPointLog", havingValue = "true", matchIfMissing = false)
public class ATMLoggingConfiguration {

    @Bean
    public Filter loggingFilter() {
        return new CustomRequestFilter();
    }

    public class CustomRequestFilter extends OncePerRequestFilter {
        private final boolean includeContent = logger.isDebugEnabled();
        private final int maxPayloadLength = 99999;

        protected void doFilterInternal(final HttpServletRequest pRequest, final HttpServletResponse pResponse, final FilterChain filterChain) throws ServletException, IOException {
            final boolean doLog = logger.isInfoEnabled() && !isAsyncDispatch(pRequest);
            HttpServletRequest request = pRequest;
            HttpServletResponse response = pResponse;

            if (doLog && includeContent) {
                if (!(request instanceof ContentCachingRequestWrapper)) {
                    request = new ContentCachingRequestWrapper(request);
                }
                if (!(response instanceof ContentCachingResponseWrapper)) {
                    response = new ContentCachingResponseWrapper(response);
                }
            }

            if (doLog) {
                beforeRequest(request);
            }

            long start = System.currentTimeMillis();
            boolean ok = false;
            try {
                filterChain.doFilter(request, response);
                ok = true;
            } finally {
                if (doLog) {
                    afterRequest(request, response, (System.currentTimeMillis() - start), ok);
                }

                ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
                if (wrapper != null) {
                    wrapper.copyBodyToResponse();
                }
            }
        }

        private void beforeRequest(final HttpServletRequest request) {
            try {
                StringBuilder buf = new StringBuilder();
                buf.append("beg. ");
                buf.append("[").append(StringUtils.rightPad(callId(request), 12)).append("] ");
                buf.append(createMessage(request));
                buf.append(";");
                logger.info(buf.toString());
            } catch (Exception e) {
                logger.warn(e.getMessage());
                throw e;
            }
        }

        private void afterRequest(final HttpServletRequest request, final HttpServletResponse response, final long ms, final boolean ok) {
            try {
                StringBuilder buf = new StringBuilder();
                buf.append("end. ");
                buf.append("[").append(StringUtils.rightPad(callId(request), 12)).append("] ");
                buf.append(createMessage(request, response));
                buf.append("; ok= ").append(ok).append(", ").append(ms).append(" ms.");
                logger.info(buf.toString());
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }

        protected String createMessage(final HttpServletRequest request) {
            StringBuilder msg = new StringBuilder();

            msg.append("uri= ").append(request.getRequestURI());
            String queryString = request.getQueryString();
            if (StringUtils.isNotBlank(queryString)) {
                msg.append('?').append(queryString);
            }

            String client = request.getRemoteAddr();
            if (StringUtils.isNotBlank(client)) {
                msg.append("; client=").append(client);
            }

            HttpSession session = request.getSession(false);
            if (session != null) {
                msg.append("; session=").append(session.getId());
            }

            String user = request.getRemoteUser();
            if (user != null) {
                msg.append("; user= ").append(user);
            }

            if (includeContent) {
                ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
                if (wrapper != null) {
                    byte[] buf = wrapper.getContentAsByteArray();

                    if (buf.length > 0) {
                        int length = Math.min(buf.length, maxPayloadLength);
                        String payload;
                        try {
                            payload = new String(buf, 0, length, wrapper.getCharacterEncoding());
                        } catch (UnsupportedEncodingException ex) {
                            payload = "[unknown]";
                            logger.warn(ex.getLocalizedMessage(), ex);
                        }
                        msg.append("; payload= ").append(payload);
                    }
                }
            }

            return msg.toString();
        }

        private String createMessage(final HttpServletRequest request, final HttpServletResponse response) {
            StringBuilder msg = new StringBuilder();

            msg.append("uri= ").append(request.getRequestURI());

            msg.append("; status= ").append(response.getStatus());

            if (includeContent) {
                ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
                if (wrapper != null) {
                    String contentType = wrapper.getContentType();
                    byte[] buf = wrapper.getContentAsByteArray();
                    if (buf.length > 0) {
                        int length = Math.min(buf.length, maxPayloadLength);
                        String payload;
                        if (StringUtils.startsWith(contentType, "application/json")) {
                            try {
                                payload = new String(buf, 0, length, wrapper.getCharacterEncoding());
                            } catch (UnsupportedEncodingException ex) {
                                payload = "[unknown]";
                                logger.warn(ex.getLocalizedMessage(), ex);
                            }
                        } else {
                            payload = "<" + buf.length + ">";
                        }
                        msg.append("; payload= ").append(payload);
                    }
                }
            }

            return msg.toString();
        }

        private String callId(final HttpServletRequest request) {
            return String.valueOf(request.hashCode());
        }
    }
}
