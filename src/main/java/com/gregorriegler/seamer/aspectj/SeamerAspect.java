package com.gregorriegler.seamer.aspectj;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.core.ProxySignature;
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
 
    @Around("callAt(seam)")
    public Object around(ProceedingJoinPoint pjp, com.gregorriegler.seamer.core.annotation.Seam seam) throws Throwable {
        initializeSeamer(pjp, seam);

        Object result = pjp.proceed();
        record(pjp, result);

        return result;
    }

    @SuppressWarnings("unchecked")
    private void record(ProceedingJoinPoint pjp, Object result) {
        seam.record(pjp.getArgs(), result);
    }

    private void initializeSeamer(ProceedingJoinPoint pjp, com.gregorriegler.seamer.core.annotation.Seam seam) {
        if (this.seam != null) return;

        this.seam = Seamer.intercept(
            seam.value(), ProxySignature.of(pjp.getTarget(), pjp.getSignature().getName()),
            pjp.getTarget().getClass()
        );
    }
}