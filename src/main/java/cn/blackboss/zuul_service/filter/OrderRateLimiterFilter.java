package cn.blackboss.zuul_service.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 限流
 * 令牌模式，请求拿到令牌的放行
 */
public class OrderRateLimiterFilter extends ZuulFilter {

    //假设qps为1000  每秒产生1000个令牌
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(1000);

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -4;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContent = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContent.getRequest();
        //仅对下单做限流处理，如果有多个业务需要限流，多几个filter或者共用这一个
        if("/apigateway/order/api/v1/order/save".equalsIgnoreCase(request.getRequestURI())){
            return true;
        }

        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContent = RequestContext.getCurrentContext();
        //非阻塞拿令牌，返回true则拿到
        if(!RATE_LIMITER.tryAcquire()){
            requestContent.setSendZuulResponse(false);
            requestContent.setResponseStatusCode(HttpStatus.SC_REQUEST_URI_TOO_LONG);
        }
        return null;
    }
}
