package seamer.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import seamer.Seamer;
import seamer.core.ProxySignature;
import seamer.core.Seam;

@Aspect
public class SeamerAspect {

    private Seam seam;

    @Pointcut("@annotation(seam) && execution(* *(..))")
    public void callAt(seamer.core.annotation.Seam seam) {
    }
 
    @Around("callAt(seam)")
    public Object around(ProceedingJoinPoint pjp, seamer.core.annotation.Seam seam) throws Throwable {
        initializeSeamer(pjp, seam);

        Object result = pjp.proceed();
        record(pjp, result);

        return result;
    }

    @SuppressWarnings("unchecked")
    private void record(ProceedingJoinPoint pjp, Object result) {
        seam.record(pjp.getArgs(), result);
    }

    private void initializeSeamer(ProceedingJoinPoint pjp, seamer.core.annotation.Seam seam) {
        if (this.seam != null) return;

        this.seam = Seamer.intercept(
            ProxySignature.of(pjp.getTarget(), pjp.getSignature().getName()),
            pjp.getTarget().getClass(),
            seam.value()
        );
    }
}