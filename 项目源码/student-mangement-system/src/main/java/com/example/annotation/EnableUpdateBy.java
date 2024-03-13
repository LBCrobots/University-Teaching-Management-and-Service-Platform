package com.example.annotation;

import com.example.annotation.generation.CreationUpdateByGeneration;
import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**用于更新时获取更新者的信息*/
@ValueGenerationType(
        generatedBy = CreationUpdateByGeneration.class
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface EnableUpdateBy {
}
