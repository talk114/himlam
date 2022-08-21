package gateway.core.util.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author dungla
 */
@Component
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(Loggable)")
    public void executeLogging() {}

    @Around(value = "executeLogging()")
    public Object logMethod(ProceedingJoinPoint joinPoint) {
        LOGGER.info(this.argumentLog(joinPoint));

        Object returnValue = null;
        try {
            returnValue = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            LOGGER.error(throwable.getMessage());
        }
        LOGGER.info(this.returnMessage(joinPoint, returnValue));

        return returnValue == null ? new Object() : returnValue;
    }

    private String argumentLog(ProceedingJoinPoint joinPoint) {
        final StringBuilder message = this.headerMessageGenerate(joinPoint,false);

        Object[] args = joinPoint.getArgs();
        if (args !=null && args.length > 0){
            message.append(" args=[ | ");
            Arrays.asList(args).forEach( arg -> message.append(arg).append(" | "));
            message.append("]");
        }
        return message.toString();
    }

    private String returnMessage(ProceedingJoinPoint joinPoint, Object process) {
        final StringBuilder afterLog = this.headerMessageGenerate(joinPoint, true);
        if (process == null) {
            afterLog.append(", returning: ").append("null");
            return afterLog.toString();
        }

        if (process instanceof Collection) {
            afterLog.append(", returning: ").append(((Collection) process).size()).append(" instanceof(s");
        } else {
            afterLog.append(", returning: ").append(process);
        }
        return afterLog.toString();
    }

    private StringBuilder headerMessageGenerate(ProceedingJoinPoint joinPoint, boolean isReturn) {
        StringBuilder message = new StringBuilder();
        message.append("Class: ").append(joinPoint.getSignature().getDeclaringTypeName());
        message.append("Method: ").append(joinPoint.getSignature().getName());

        if(isReturn ) {
            message.append(" Return: ");
        }

        return message;
    }
}
