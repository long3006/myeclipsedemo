package cn.appsys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DevUser;
import cn.appsys.tools.Constants;

public class SysInterceptor extends HandlerInterceptorAdapter {

		private Logger logger=Logger.getLogger(SysInterceptor.class);
		
		public boolean preHandle(HttpServletRequest request,
							HttpServletResponse response,
							Object handler)throws Exception{
			logger.debug("SysInterceptor preHandle!");
			System.out.println("test666");
			HttpSession session=request.getSession();
			BackendUser buser=(BackendUser)session.getAttribute(Constants.BAKCUSER_SESSION);
			DevUser duser=(DevUser)session.getAttribute(Constants.DEVUDER_SESSION);
			if(null != buser){ 
				return true;
			}else if(null != duser){
				return true;
			}else{
				response.sendRedirect(request.getContextPath()+"/403.jsp");
				return false;
			}
		}
}
