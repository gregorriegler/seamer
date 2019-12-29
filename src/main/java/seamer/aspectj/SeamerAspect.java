package seamer.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import seamer.SeamerFactory;
import seamer.core.ProxySeam;
import seamer.core.Seamer;
import seamer.core.annotation.Seam;

@Aspect
public class SeamerAspect {

    private Seamer seamer;

    @Pointcut("@annotation(seam) && execution(* *(..))")
    public void callAt(Seam seam) {
    }
 
    @Around("callAt(seam)")
    public Object around(ProceedingJoinPoint pjp, Seam seam) throws Throwable {
        initializeSeamer(pjp, seam);

        Object result = pjp.proceed();
        record(pjp, result);

        return result;
    }

    @SuppressWarnings("unchecked")
    private void record(ProceedingJoinPoint pjp, Object result) {
        seamer.record(pjp.getArgs(), result);
    }

    private void initializeSeamer(ProceedingJoinPoint pjp, Seam seam) {
        if (seamer != null) return;

        seamer = SeamerFactory.intercept(
            ProxySeam.of(pjp.getTarget(), pjp.getSignature().getName()),
            pjp.getTarget().getClass(),
            seam.value()
        );
    }
}