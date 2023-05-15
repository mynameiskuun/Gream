package com.project.gream.common.annotation;


/*
 * Target 설정 : 어노테이션이 적용되는 SCOPE를 설정한다
 * Retention : 메모리를 어느 시점까지 가져갈 것인가
 * */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginMember {
}
