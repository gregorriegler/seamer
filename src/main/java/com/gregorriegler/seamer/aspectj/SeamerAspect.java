package com.gregorriegler.seamer.aspectj;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.core.ProxyInvokable;
import com.gregorriegler.seamer.core.Seam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SeamerAspect {

    private Seam seam;

    @Pointcut("@annotation(seam) && execution(* *(..))")
    public void callAt(com.gregorriegler.seamer.core.annotation.Seam seam) {
    }

    @SuppressWarnings("unchecked")
    @Around("callAt(seam)")
    public Object around(ProceedingJoinPoint pjp, com.gregorriegler.seamer.core.annotation.Seam seam) throws Throwable {
        initializeInterceptor(pjp, seam);

        Object result = pjp.proceed();
        this.seam.record(pjp.getArgs(), result);

        return result;
    }

    private void initializeInterceptor(ProceedingJoinPoint pjp, com.gregorriegler.seamer.core.annotation.Seam seam) {
        if (this.seam != null) return;

        this.seam = Seamer.createSeam(
            seam.basePath(),
            seam.value(),
            ProxyInvokable.of(pjp.getTarget(), pjp.getSignature().getName())
        );
    }
}