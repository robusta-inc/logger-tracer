package com.robusta.logger.tracer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class TraceablesTest {
    @Mock private HttpServletRequest request;
    @Mock private HttpSession session;
    @Mock private Cookie cookie;
    private Cookie[] cookies = new Cookie[1];

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        cookies[0] = cookie;
    }

    @Test
    public void testRequestParams_whenRequestIsNull_shouldOutput_nullRequestString() throws Exception {
        assertThat(Traceables.requestParams(null).asString(), is(equalTo("[null - HttpServletRequest]")));
    }

    @Test
     public void testRequestParams_whenNoParametersAreSpecifiedForTracing_delegatesToRequest() throws Exception {
        assertThat(Traceables.requestParams(request).asString(), is(notNullValue(String.class)));
    }

    @Test
    public void testRequestParams_withParameters_whichAreNotPresentOnTheRequest() throws Exception {
        assertThat(Traceables.requestParams(request, "invalid1", "invalid2").asString(), is(equalTo("HttpServletRequest.params: [[{invalid1=null, invalid2=null}]]")));
    }

    @Test
    public void testRequestParams_withParameters() throws Exception {
        when(request.getParameter("valid1")).thenReturn("validvalue1");
        assertThat(Traceables.requestParams(request, "valid1", "invalid2").asString(), is(equalTo("HttpServletRequest.params: [[{valid1=validvalue1, invalid2=null}]]")));
    }

    @Test
    public void testSessionAttributes() throws Exception {
        assertThat(Traceables.sessionAttrs(null).asString(), is(equalTo("[Unable to getSession - null HttpServletRequest]")));
    }

    @Test
    public void testSessionAttributes_whenSessionIsNull() throws Exception {
        when(request.getSession(false)).thenReturn(null);
        assertThat(Traceables.sessionAttrs(request).asString(), is(equalTo("[null - HttpSession]")));
    }

    @Test
    public void testSessionAttributes_whenSesessionHasSomeAttributesAndDoesNotHaveSome() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("valid1")).thenReturn("validvalue1");
        assertThat(Traceables.sessionAttrs(request, "valid1", "invalid1").asString(), is(equalTo("HttpSession.attrs: [[{valid1=validvalue1, invalid1=null}]]")));
    }
    
    @Test
    public void testCookieAttributes_whenCookieSomeAttributesAndDoesNotHaveSome() throws Exception {
        when(request.getCookies()).thenReturn(cookies);
        when(cookies[0].getName()).thenReturn("validkey1");
        when(cookies[0].getValue()).thenReturn("validvalue1");
        assertThat(Traceables.cookieAttrs(request, "validkey1", "invalidkey1").asString(), is(equalTo("Cookie[].attrs: [[{validkey1=validvalue1}]]")));
    }

    @Test
    public void testCookieAttributes_whenCookieIsNull() throws Exception {
        when(request.getCookies()).thenReturn(null);
        assertThat(Traceables.cookieAttrs(request, "validkey1").asString(), is(equalTo("[null - Cookie[]]")));
    }    
    
    @Test
    public void testCookieAttributes_whenRequestIsNull() throws Exception {
        assertThat(Traceables.cookieAttrs(null, "validkey1").asString(), is(equalTo("[Unable to retrieve Cookie[] - null HttpServletRequest]")));
    } 
}
