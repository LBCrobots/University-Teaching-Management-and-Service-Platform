package com.example.annotation;

import com.example.annotation.generation.CreationCreateByGeneration;
import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**实现根据登录Token验证的功能*/
/*用于指定值生成器的类型*/
@ValueGenerationType(
        generatedBy = CreationCreateByGeneration.class
)
/*指定注解的保留策略，表示注解在运行时可用。*/
@Retention(RetentionPolicy.RUNTIME)
/*指定注解可以应用于字段或方法。*/
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface EnableCreateBy {
}
