package com.example.mayoweb.commons.aop;

import com.example.mayoweb.commons.exception.ApplicationException;
import com.example.mayoweb.commons.exception.payload.ErrorStatus;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Aspect
@Component
@RequiredArgsConstructor
public class FirestoreTransactionAspect {

    private final Firestore firestore;

    @Around("@annotation(com.example.mayoweb.commons.annotation.FirestoreTransactional) || @within(com.example.mayoweb.commons.annotation.FirestoreTransactional)")
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
            return firestore.runTransaction(transaction -> {
                try {
                    return joinPoint.proceed();
                } catch (Throwable e) {
                    throw new ApplicationException(ErrorStatus.toErrorStatus("firebase transaction 오류", 500, LocalDateTime.now()));
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new ApplicationException(ErrorStatus.toErrorStatus("firebase transaction 오류", 500, LocalDateTime.now()));
        }
    }
}
