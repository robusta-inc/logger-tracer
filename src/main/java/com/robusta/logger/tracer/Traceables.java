package com.robusta.logger.tracer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A {@link Traceable} factory class which provides
 * implementations for wrapping common objects.
 *
 * <p>Using a traceable ensure that the toString()
 * operation is done only when tracing is enabled.</p>
 *
 * @author sudhir.ravindramohan
 * @since 1.0
 * @see HttpServletRequest
 * @see HttpSession
 */
public abstract class Traceables {

    /**
     * Get a {@link Traceable} implementation that wraps a
     * {@link HttpServletRequest request}. Names of significant parameters
     * expected in the request is accepted.
     *
     * @param request HttpServletRequest - to be traced
     * @param parameterNamesToBeTraced - String[] - an array of parameter name
     *                                 that are significant.
     * @return Traceable
     */
    public static Traceable<HttpServletRequest> requestParams(HttpServletRequest request, String... parameterNamesToBeTraced) {
        return new TraceableHttpServletRequest(request, parameterNamesToBeTraced);
    }

    /**
     * Get a {@link Traceable} implementation that wraps a
     * {@link HttpSession session}.
     * <p>Accepts the {@link HttpServletRequest} from which a session
     * will be obtained (with createNew = false).</p>
     * <p>Names of significant attributes expected in the session is
     * accepted.</p>
     *
     * <p>Can handle cases:
     * <ol>
     *     <li>HttpServletRequest is null</li>
     *     <li>HttpSession obtained from the request is null</li>
     *     <li>HttpSession does not contain expected attributes</li>
     * </ol></p>
     * @param request HttpServletRequest - to be traced
     * @param sessionAttributesToBeTraced - String[] - an array of
     *                                    significant attribute names.
     * @return Traceable
     */
    public static Traceable<HttpSession> sessionAttrs(HttpServletRequest request, String... sessionAttributesToBeTraced) {
        return new TraceableHttpSession(request, sessionAttributesToBeTraced);
    }

    public static Traceable<Cookie[]> cookieAttrs(HttpServletRequest request, String... cookieAttributesToBeTraced) {
        return new TraceableCookie(request, cookieAttributesToBeTraced);
    }
    
    static class TraceableHttpServletRequest extends Traceable<HttpServletRequest> {

        private final String[] parametersToBeTraced;

        TraceableHttpServletRequest(HttpServletRequest request, String... parametersToBeTraced) {
            super(HttpServletRequest.class, request);
            this.parametersToBeTraced = parametersToBeTraced;
        }

        @Override
        protected String asString() {
            if(traced == null) {
                return super.nullAsString();
            } else if(parametersToBeTraced == null || parametersToBeTraced.length == 0) {
                return super.objectAsString();
            } else {
                Map<String, String> parameters = new LinkedHashMap<String, String>(); // to maintain ordering.
                for (String aParameterName : parametersToBeTraced) {
                    parameters.put(aParameterName, traced.getParameter(aParameterName));
                }
                return String.format("%s.params: [[%s]]", HttpServletRequest.class.getSimpleName(), parameters);
            }
        }
    }

    private static class TraceableHttpSession extends Traceable<HttpSession> {
        private final String[] sessionAttributesToBeTraced;
        private boolean nullRequestPassedIn;

        public TraceableHttpSession(HttpServletRequest request, String... sessionAttributesToBeTraced) {
            this(request == null ? null : request.getSession(false), sessionAttributesToBeTraced);
            this.nullRequestPassedIn = request == null;
        }

        private TraceableHttpSession(HttpSession session, String... sessionAttributesToBeTraced) {
            super(HttpSession.class, session);
            this.sessionAttributesToBeTraced = sessionAttributesToBeTraced;
        }

        @Override
        protected String asString() {
            if(traced == null) {
                if(nullRequestPassedIn) {
                    return "[Unable to getSession - null HttpServletRequest]";
                }
                return super.nullAsString();
            } else if(sessionAttributesToBeTraced == null || sessionAttributesToBeTraced.length == 0) {
                return super.objectAsString();
            } else {
                Map<String, Object> parameters = new LinkedHashMap<String, Object>(); // to maintain ordering.
                for (String aParameterName : sessionAttributesToBeTraced) {
                    parameters.put(aParameterName, traced.getAttribute(aParameterName));
                }
                return String.format("%s.attrs: [[%s]]", HttpSession.class.getSimpleName(), parameters);
            }
        }
    }
    
    private static class TraceableCookie extends Traceable<Cookie[]> {
        private final String[] cookieAttributesToBeTraced;
        private boolean nullRequestPassedIn;

        public TraceableCookie(HttpServletRequest request, String... cookieAttributesToBeTraced) {
            this(request == null ? null : request.getCookies(), cookieAttributesToBeTraced);
            this.nullRequestPassedIn = request == null;
        }

        private TraceableCookie(Cookie[] cookies, String... cookieAttributesToBeTraced) {
            super(Cookie[].class, cookies);
            this.cookieAttributesToBeTraced = cookieAttributesToBeTraced;
        }

        @Override
        protected String asString() {
            if(traced == null) {
                if(nullRequestPassedIn) {
                    return "[Unable to retrieve Cookie[] - null HttpServletRequest]";
                }
                return super.nullAsString();
            } else if(cookieAttributesToBeTraced == null || cookieAttributesToBeTraced.length == 0) {
                return super.objectAsString();
            } else {
                Map<String, Object> parameters = new LinkedHashMap<String, Object>(); // to maintain ordering.
                for (String aParameterName : cookieAttributesToBeTraced) {
					for (Cookie cookie : traced) {
						String cookieName = cookie.getName();
						if (cookieName.equals(aParameterName))
							parameters.put(aParameterName, cookie.getValue());
					}
				}
                return String.format("%s.attrs: [[%s]]", Cookie[].class.getSimpleName(), parameters);
            }
        }
    }    
}
