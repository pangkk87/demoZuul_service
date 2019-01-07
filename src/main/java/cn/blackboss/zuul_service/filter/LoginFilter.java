package cn.blackboss.zuul_service.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.RequestContent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 自定义filter
 * 1 继承zuulFilter
 * 2 添加Component注解，让spring来管理
 */
@Component
public class LoginFilter extends ZuulFilter{

    /**
     * 过滤器类型，前置过滤器
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * 过滤器顺序，值越小越先执行
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 过滤器是否生效
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext requestContent = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContent.getRequest();

        System.out.println(request.getRequestURI());//==/apigateway/order/api/v1/order/save
//        System.out.println(request.getRequestURL());//==http://localhost:9000/apigateway/order/api/v1/order/save

        /**
         * 字符串放前面可以避免空指针  编码习惯
         * 根据业务需求来控制是否拦截
         * 此处为： 拦截请求为/apigateway/order/api/v1/order/save地址的
         *
         *
         * acl 访问控制列表  存放在公共权限资源里去
         * 将需要过滤的url放进redis里面  定期去拉去数据放在本地缓存（就是一个集合）进行匹配
         */
        if("/apigateway/order/api/v1/order/save".equalsIgnoreCase(request.getRequestURI())){
            return true;
        }

        return false;
    }

    @Override
    public Object run() throws ZuulException {

        //可以使用jwt
        RequestContext requestContent = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContent.getRequest();

        //有些情况下在header中获取不到就在parameter中获取
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }

        /**
         * 还是为空，视为非法操作，此处做鉴权功能
         * 可以将登陆后的用户绑定一个加密的token，此处做解密，避免攻击
         */

        if (StringUtils.isBlank(token)){
            //设置不再往下走
            requestContent.setSendZuulResponse(false);
            //设置响应信息，状态吗
            requestContent.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
        }else {

        }



        return null;
    }
}
