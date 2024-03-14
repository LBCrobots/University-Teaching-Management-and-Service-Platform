package com.example.annotation.generation;

import com.example.annotation.EnableCreateBy;

import org.hibernate.tuple.AnnotationValueGeneration;
import org.hibernate.tuple.GenerationTiming;
import org.hibernate.tuple.ValueGenerator;


public class CreationCreateByGeneration implements AnnotationValueGeneration<EnableCreateBy> {
    private ValueGenerator<?> generator;

    public CreationCreateByGeneration() {
    }

    @Override
    public void initialize(EnableCreateBy enableUpdateBy, Class<?> propertyType) {
        this.generator = CreateByGenerators.get(propertyType);
    }

    @Override
    public GenerationTiming getGenerationTiming() {
        return GenerationTiming.INSERT;
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
