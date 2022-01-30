//package account.security;
//
//import org.springframework.security.access.intercept.InterceptorStatusToken;
//import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
//
//import javax.servlet.FilterConfig;
//
//public class CustomFilterSecurityInterceptor extends FilterSecurityInterceptor {
//
//    @Override
//    public void init(FilterConfig arg0) {
//        super.init(arg0);
//    }
//
//    @Override
//    protected Object afterInvocation(InterceptorStatusToken token, Object returnedObject) {
//
//
//        System.out.println("----------CustomFilterSecurityInterceptor-------------");
//        System.out.println(token.getSecurityContext().getAuthentication());
//        System.out.println(token.getSecurityContext().getAuthentication().getPrincipal());
//        System.out.println(token.getSecurityContext().getAuthentication().getCredentials());
//        System.out.println("----------CustomFilterSecurityInterceptor-------------");
//
//        return super.afterInvocation(token, returnedObject);
//    }
//}
