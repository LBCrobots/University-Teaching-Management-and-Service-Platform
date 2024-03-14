package com.example.annotation.generation;

import com.example.annotation.EnableUpdateBy;
import org.hibernate.tuple.AnnotationValueGeneration;
import org.hibernate.tuple.GenerationTiming;
import org.hibernate.tuple.ValueGenerator;


public class CreationUpdateByGeneration implements AnnotationValueGeneration<EnableUpdateBy> {
    private ValueGenerator<?> generator;

    public CreationUpdateByGeneration() {

    }

    @Override
    public void initialize(EnableUpdateBy enableUpdateBy, Class<?> propertyType) {
        this.generator = UpdateByGenerators.get(propertyType);
    }

    @Override
    public GenerationTiming getGenerationTiming() {
        return GenerationTiming.ALWAYS;
    }

    @Override
    public ValueGenerator<?> getValueGenerator() {
        return this.generator;
    }

    @Override
    public boolean referenceColumnInSql() {
        return false;
    }

    @Override
    public String getDatabaseGeneratedReferencedColumnValue() {
        return null;
    }
}

