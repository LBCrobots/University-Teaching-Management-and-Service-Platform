package com.example.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.annotation.EnableQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**功能描述：查询工具类*/
@Slf4j
public class QueryHelp {
    public static <R, Q> Predicate getPredicate(Root<R> root, Q query, CriteriaBuilder cb) {
        List<Predicate> list = new ArrayList<Predicate>();

        if(query == null){
            return cb.and(list.toArray(new Predicate[0]));//逻辑与运算，生成一个表示"与"关系的 Predicate
        }
        try {
            //获取查询所以字段
            List<Field> fields = getAllFields(query.getClass(), new ArrayList<Field>());
            for (Field field : fields) {
                // 测试调用者是否可以访问此反射对象
                boolean accessible = field.canAccess(query);
                // 设置对象的访问权限，保证对private的属性的访问；true为可访问
                field.setAccessible(true);
                // 获取字段上的 EnableQuery 注解。
                EnableQuery q = field.getAnnotation(EnableQuery.class);
                //存在注解则处理
                if (q != null) {
                    String propName = q.propName();//属性名
                    String joinName = q.joinName();//连接名
                    String blurry = q.blurry();//模糊查询属性
                    String attributeName = isBlank(propName) ? field.getName() : propName;//根据属性名或者字段名设置属性的名称。
                    Class<?> fieldType = field.getType();//获取字段类型
                    Object val = field.get(query);//获取字段的值。
                    if (ObjectUtil.isNull(val) || "".equals(val)) {
                        continue;
                    }//如果字段值为空，则跳过当前循环
                    Join join = null;//定义连接对象，用于处理关联查询。
                    // 模糊多字段，若存在模糊查询则处理
                    if (ObjectUtil.isNotEmpty(blurry)) {
                        String[] blurrys = blurry.split(",");//按逗号分割，得到一个字段数组
                        List<Predicate> orPredicate = new ArrayList<Predicate>();//创建一个 Predicate 列表，用于存储各个字段的模糊查询条件
                        //对于每个字段，创建一个模糊查询条件，并加入到 orPredicate 列表中
                        for (String s : blurrys) {
                            orPredicate.add(cb.like(root.get(s)
                                    .as(String.class), "%" + val.toString() + "%"));
                        }
                        Predicate[] p = new Predicate[orPredicate.size()];
                        list.add(cb.or(orPredicate.toArray(p)));
                        continue;
                    }
                    //如果存在连接名，处理关联查询。
                    if (ObjectUtil.isNotEmpty(joinName)) {
                        String[] joinNames = joinName.split(">");
                        for (String name : joinNames) {
                            switch (q.join()) {
                                case LEFT:
                                    if(ObjectUtil.isNotNull(join)){
                                        join = join.join(name, JoinType.LEFT);
                                    } else {
                                        join = root.join(name, JoinType.LEFT);
                                    }
                                    break;
                                case RIGHT:
                                    if(ObjectUtil.isNotNull(join)){
                                        join = join.join(name, JoinType.RIGHT);
                                    } else {
                                        join = root.join(name, JoinType.RIGHT);
                                    }
                                    break;
                                default: break;
                            }
                        }
                    }
                    //根据注解中的查询类型，生成相应的查询条件
                    switch (q.type()) {
                        case EQUAL:
                            list.add(cb.equal(getExpression(attributeName,join,root)
                                    .as((Class<? extends Comparable>) fieldType),val));
                            break;
                        case GREATER_THAN:
                            list.add(cb.greaterThanOrEqualTo(getExpression(attributeName,join,root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case LESS_THAN:
                            list.add(cb.lessThanOrEqualTo(getExpression(attributeName,join,root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case LESS_THAN_NQ:
                            list.add(cb.lessThan(getExpression(attributeName,join,root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case INNER_LIKE:
                            list.add(cb.like(getExpression(attributeName,join,root)
                                    .as(String.class), "%" + val.toString() + "%"));
                            break;
                        case LEFT_LIKE:
                            list.add(cb.like(getExpression(attributeName,join,root)
                                    .as(String.class), "%" + val.toString()));
                            break;
                        case RIGHT_LIKE:
                            list.add(cb.like(getExpression(attributeName,join,root)
                                    .as(String.class), val.toString() + "%"));
                            break;
                        case IN:
                            if (CollUtil.isNotEmpty((Collection<Long>)val)) {
                                list.add(getExpression(attributeName,join,root).in((Collection<Long>) val));
                            }
                            break;
                        case NOT_EQUAL:
                            list.add(cb.notEqual(getExpression(attributeName,join,root), val));
                            break;
                        case NOT_NULL:
                            list.add(cb.isNotNull(getExpression(attributeName,join,root)));
                            break;
                        case BETWEEN:
                            List<Object> between = new ArrayList<Object>((List<Object>)val);
                            list.add(cb.between(getExpression(attributeName, join, root).as((Class<? extends Comparable>) between.get(0).getClass()),
                                    (Comparable) between.get(0), (Comparable) between.get(1)));
                            break;
                        default: break;
                    }
                }
                field.setAccessible(accessible);//恢复字段的原始访问权限
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        //构建并返回最终的 Predicate 对象
        int size = list.size();
        return cb.and(list.toArray(new Predicate[size]));
    }

    //获取表达式对象，支持关联查询。
    @SuppressWarnings("unchecked")
    private static <T, R> Expression<T> getExpression(String attributeName, Join join, Root<R> root) {
        if (ObjectUtil.isNotEmpty(join)) {
            return join.get(attributeName);
        } else {
            return root.get(attributeName);
        }
    }

    //判断字符串是否为空
    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //获取类及其父类的所有字段
    private static List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }
}
