package com.gregorriegler.seamer.aspectj;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.core.ProxyMethod;
import com.gregorriegler.seamer.core.SeamRecorder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SeamerAspect {

    private SeamRecorder seam;

    @Pointcut("@annotation(seam) && execution(* *(..))")
    public void callAt(com.gregorriegler.seamer.core.annotation.Seam seam) {
    }

    @SuppressWarnings("unchecked")
    @Around("callAt(seam)")
    public Object around(ProceedingJoinPoint pjp, com.gregorriegler.seamer.core.annotation.Seam seam) throws Throwable {
        initializeInterceptor(pjp, seam);

        Object result = pjp.proceed();
        this.seam.recordInvocation(pjp.getArgs(), result);

        return result;
    }

    private void initializeInterceptor(ProceedingJoinPoint pjp, com.gregorriegler.seamer.core.annotation.Seam seam) {
        if (this.seam != null) return;

        this.seam = Seamer.intercept(
            seam.value(),
            pjp.getTarget().getClass(),
            ProxyMethod.of(pjp.getTarget(), pjp.getSignature().getName())
        );
    }
}