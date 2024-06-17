package genspark.pj.SecureAuthenticationSystem.Aspect;

import genspark.pj.SecureAuthenticationSystem.Entity.Blog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@EnableAspectJAutoProxy
@Slf4j
public class LoggingAspect {
    // When calling the method it will log which method is being called and the param it has
    @Before("execution(* genspark.pj.SecureAuthenticationSystem.Services.BlogService.*(..))")
    public void logBeforeService(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String argsString = Arrays.toString(args);
        log.info("Executing method: " + methodName + ", Arguments: " + argsString);
    }

    // After it finished, it will log the result of the method
    @AfterReturning(pointcut = "execution(* genspark.pj.SecureAuthenticationSystem.Services.BlogService.*(..))", returning = "result")
    public void logAfterService(JoinPoint joinPoint, Object result){
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String argsString = IntStream.range(0, args.length)
                .mapToObj(i -> {
                    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                    Parameter parameter = method.getParameters()[i];
                    return parameter.getName() + "=" + args[i];
                })
                .collect(Collectors.joining(", "));
        log.info("Method executed: " + methodName + ", Arguments: " + argsString +", Return: " + result);
        if (isEmpty(result)){
            log.info("No Blog with the criteria: " + argsString);
        }
    }
    // Helper method use to see if the method executed correctly
    private boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).trim().isEmpty();
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj.getClass().isArray()) {
            return Arrays.asList((Object[]) obj).isEmpty();
        }
        if (obj instanceof Blog){
            return  ((Blog) obj).getTitle().isEmpty();
        }
        return false;
    }
}
