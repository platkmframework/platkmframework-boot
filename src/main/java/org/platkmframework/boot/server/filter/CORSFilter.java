/*******************************************************************************
 *   Copyright(c) 2023 the original author or authors.
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *  
 *        https://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *******************************************************************************/
package org.platkmframework.boot.server.filter;

import java.io.IOException; 
 

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.platkmframework.content.ioc.ObjectContainer; 

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; 


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/  
public class CORSFilter implements Filter
{
	private static final Logger logger = LogManager.getLogger(CORSFilter.class);
	

	   /**
     * Default constructor.
     */
    public CORSFilter() { 
    }
 
    /**
     * @see Filter#destroy()
     */
    public void destroy() { 
    }
 
    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
 
        HttpServletRequest request = (HttpServletRequest) servletRequest;
       // System.out.println("CORSFilter HTTP Request: " + request.getMethod());
 
        // Authorize (allow) all domains to consume the content
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", _getControlAllowOrigin(request,ObjectContainer.instance().getPropertyValue("System_Access-Control-Allow-Origin")));
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods", ObjectContainer.instance().getPropertyValue("System_Access-Control-Allow-Methods"));
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", ObjectContainer.instance().getPropertyValue("System_Access-Control-Allow-Headers"));
        
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
 
        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
        if (request.getMethod().equals("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
       // request.getHeader("Content-Type")
 
        // pass the request along the filter chain
        chain.doFilter(request, servletResponse);
    }
 
    private String _getControlAllowOrigin(HttpServletRequest request, String propertyValue) {
		
		if(StringUtils.isEmpty(propertyValue)) return "";
		String origin = request.getHeader("Origin");
		logger.info(origin);
		if(StringUtils.isEmpty(origin)) return "";
		
		if(propertyValue.contains(origin)) 
			return origin; 
		else {
				logger.warn("No origin present " + origin);
				return "";  
			}
	}

	/**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException { 
    }

	 
}
