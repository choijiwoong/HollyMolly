package com.holly.molly.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTraceAop {//실행속도 측정을 위한 모듈

    @Around("execution(* com.holly.molly..*(..))")//molly.service..로 변경시 해당 부분만 AOP가 적용되게 수정이 가능
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{
        long start=System.currentTimeMillis();
        System.out.println("START: "+joinPoint.toString());
        try{
            return joinPoint.proceed();//프록시라는 가짜 서비스를 실행하여 시간을 체킹 뒤에 실제 작업
        } finally{
            long finish=System.currentTimeMillis();
            long timeMs=finish-start;
            System.out.println("END: "+joinPoint.toString()+" "+timeMs+"ms");
        }
    }
}
