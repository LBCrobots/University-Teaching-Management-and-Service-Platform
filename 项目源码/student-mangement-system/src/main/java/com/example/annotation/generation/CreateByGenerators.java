package com.example.annotation.generation;

import com.example.utils.HutoolJWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.tuple.ValueGenerator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**验证登录token*/

final class CreateByGenerators {

    private static final Map<Class<?>, ValueGenerator<?>> GENERATORS = new HashMap<>();

    public CreateByGenerators() {
    }

    public static ValueGenerator<?> get(Class<?> type){
       ValueGenerator<?> valueGeneratorSupplier = (ValueGenerator)GENERATORS.get(type);
       if(Objects.isNull(valueGeneratorSupplier)){
           return null;
       }else {
           return valueGeneratorSupplier;
       }
    }
    static {
        GENERATORS.put(java.lang.Long.class,(session,owner)->{
            // 在此编写实现业务逻辑获取数据
            HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = (String)request.getServletContext().getAttribute("token");
            Long creatBy = HutoolJWTUtil.parseToken(token);
            return creatBy;
        });
    }
}
