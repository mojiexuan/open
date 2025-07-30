package com.chenjiabao.open.utils.docs.scanner;

import com.chenjiabao.open.utils.annotation.ApiVersion;
import com.chenjiabao.open.utils.docs.annotation.Operation;
import com.chenjiabao.open.utils.docs.annotation.Parameter;
import com.chenjiabao.open.utils.docs.annotation.Tag;
import com.chenjiabao.open.utils.docs.model.ApiDefinition;
import com.chenjiabao.open.utils.docs.model.ApiGroup;
import com.chenjiabao.open.utils.docs.model.ApiParameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 接口扫描器
 *
 * @author ChenJiaBao
 */
public class ApiScanner implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger log = LoggerFactory.getLogger(ApiScanner.class);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    @Getter
    private static volatile List<ApiGroup> apiGroups = Collections.emptyList();

    private static final List<Class<? extends Annotation>> HTTP_METHOD_ANNOTATIONS = Arrays.asList(
            RequestMapping.class,
            GetMapping.class,
            PostMapping.class,
            PutMapping.class,
            DeleteMapping.class
//            PatchMapping.class
    );

    private static final Set<Class<?>> WRAPPER_TYPES = new HashSet<>(Arrays.asList(
            Integer.class, Long.class, Double.class, Float.class,
            Boolean.class, Character.class, Byte.class, Short.class
    ));

    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        // 应用启动完成后启动扫描线程
        executor.submit(()->{
            log.info("接口文档：开始扫描接口");
            try {
                apiGroups = scanControllers(event.getApplicationContext());
            } catch (RuntimeException e) {
                log.error("接口文档：扫描接口失败", e);
            }
            log.info("接口文档：扫描接口完成");
        });
    }

    /**
     * 扫描控制器
     *
     * @param context 应用上下文
     * @return 接口组列表
     */
    private List<ApiGroup> scanControllers(ApplicationContext context) {
        List<ApiGroup> result = new ArrayList<>();
        // 获取所有RestController
        Map<String, Object> controllers = context.getBeansWithAnnotation(RestController.class);
        controllers.values().forEach(controller -> {
            Class<?> clazz = controller.getClass();
            ApiGroup apiGroup = resolveGroup(clazz);
            if(clazz.isAnnotationPresent(ApiVersion.class)){
                ApiVersion apiVersion = clazz.getAnnotation(ApiVersion.class);
                if(apiVersion.value() > 0){
                    apiGroup.setVersion(String.valueOf(apiVersion.value()));
                }
            }
            // 扫描方法
            Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> HTTP_METHOD_ANNOTATIONS.stream()
                            .anyMatch(method::isAnnotationPresent))
                    .forEach(method -> {
                        ApiDefinition apiDefinition = parseApiDefinition(method);
                        apiGroup.addApi(apiDefinition);
                        if(method.isAnnotationPresent(ApiVersion.class)){
                            ApiVersion apiVersion = clazz.getAnnotation(ApiVersion.class);
                            if(apiVersion.value() > 0){
                                apiDefinition.setVersion(String.valueOf(apiVersion.value()));
                            }else {
                                apiDefinition.setVersion(apiGroup.getVersion());
                            }
                        }
                    });
            result.add(apiGroup);
        });

        return result;
    }

    /**
     * 解析分组信息
     * @param clazz 类
     * @return 分组名称
     */
    private ApiGroup resolveGroup(Class<?> clazz) {
        ApiGroup apiGroup = new ApiGroup();
        //  优先检查是否有自定义注解
        if (clazz.isAnnotationPresent(Tag.class)) {
            Tag tag = clazz.getAnnotation(Tag.class);
            apiGroup.setName(tag.name());
            apiGroup.setDescription(tag.description());
        }else {
            // 获取类的简单名称（不含包名）
            String simpleName = clazz.getSimpleName();
            if (simpleName.endsWith("Controller")) {
                apiGroup.setName(simpleName.substring(0,simpleName.length() - "Controller".length()));
            }
            apiGroup.setName(simpleName);
        }

        // 检查是否有 @RequestMapping 的 value
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            String[] values = clazz.getAnnotation(RequestMapping.class).value();
            if (values.length > 0 && !values[0].isEmpty()) {
                apiGroup.setPath(values[0]);
            }
        }

        return apiGroup;
    }

    /**
     * 解析接口定义
     */
    private ApiDefinition parseApiDefinition(Method method){
        // 解析方法或参数
        ApiDefinition apiDefinition = resolveHttpMethod(method);
        // 检查并处理Operation注解
        Operation operation = AnnotationUtils.findAnnotation(method, Operation.class);
        if (operation != null) {
            apiDefinition.setSummary(operation.summary());
            apiDefinition.setDescription(operation.description());
            apiDefinition.setDeprecated(operation.deprecated());
        }

        apiDefinition.setSummary(method.getName());

        // 解析参数
        parseParameters(apiDefinition,method);

        return apiDefinition;
    }

    /**
     * 解析方法的HTTP请求方法及接口路径
     * @param method 控制器方法
     * @return ApiDefinition
     */
    private ApiDefinition resolveHttpMethod(Method method) {
        ApiDefinition apiDefinition = new ApiDefinition();
        String[] paths = {"*"};
        if (method.isAnnotationPresent(GetMapping.class)) {
            apiDefinition.setMethod("GET");
            paths = method.getAnnotation(GetMapping.class).value();
        }
        if (method.isAnnotationPresent(PostMapping.class)) {
            apiDefinition.setMethod("POST");
            paths = method.getAnnotation(PostMapping.class).value();
        }
        if (method.isAnnotationPresent(PutMapping.class)) {
            apiDefinition.setMethod("PUT");
            paths = method.getAnnotation(PutMapping.class).value();
        }
        if (method.isAnnotationPresent(DeleteMapping.class)) {
            apiDefinition.setMethod("DELETE");
            paths = method.getAnnotation(DeleteMapping.class).value();
        }
        if (method.isAnnotationPresent(PatchMapping.class)) {
            apiDefinition.setMethod("PATCH");
            paths = method.getAnnotation(PatchMapping.class).value();
        }
        // 处理@RequestMapping
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            if (requestMapping.method().length > 0) {
                // 从@RequestMapping的method属性获取
                apiDefinition.setMethod(requestMapping.method()[0].name().toUpperCase());
            }
            apiDefinition.setMethod("GET");
            paths = method.getAnnotation(RequestMapping.class).value();
        }
        // 否则允许全部
        apiDefinition.setMethod("*");
        apiDefinition.setPath(paths.length > 0 ? paths[0] : "");
        return apiDefinition;
    }

    /**
     * 解析参数
     * @param method 方法
     */
    private void parseParameters(ApiDefinition apiDefinition,Method method) {
        Arrays.stream(method.getParameters())
                .filter(this::shouldIncludeParameter)
                .forEach(parameter -> {
                    if(parameter.isAnnotationPresent(RequestBody.class)){
                        ApiParameter apiParam = parseBodyParameter(parameter.getType());
                        if(parameter.isAnnotationPresent(Parameter.class)){
                            Parameter param = parameter.getAnnotation(Parameter.class);
                            if(apiParam.getName() == null){
                                apiParam.setName(param.name());
                            }
                            if(apiParam.getDescription() == null){
                                apiParam.setDescription(param.description());
                            }
                            apiParam.setRequired(param.required());
                        }
                        apiDefinition.addBodyParam(apiParam);
                    }else {
                        ApiParameter apiParam = parseParameter(parameter);
                        if("query".equals(apiParam.getIn())){
                            apiDefinition.addQueryParam(apiParam);
                        }else if("path".equals(apiParam.getIn())){
                            apiDefinition.addPathParam(apiParam);
                        }
                    }
                });
    }

    /**
     * 排除特定类型参数
     * @param parameter 参数
     */
    private boolean shouldIncludeParameter(java.lang.reflect.Parameter parameter) {
        // 排除特定类型的参数
        return !parameter.getType().equals(HttpServletRequest.class) &&
                !parameter.getType().equals(HttpServletResponse.class);
//       && !parameter.isAnnotationPresent(ApiIgnore.class)
    }

    /**
     * 解析单个参数
     * @param parameter 参数
     * @return ApiParameter
     */
    private ApiParameter parseParameter(java.lang.reflect.Parameter parameter) {
        ApiParameter apiParam = new ApiParameter();
        apiParam.setIn("query");

        // 检查@Parameter注解
        if(parameter.isAnnotationPresent(Parameter.class)){
            Parameter param = parameter.getAnnotation(Parameter.class);
            apiParam.setName(param.name());
            apiParam.setDescription(param.description());
            apiParam.setRequired(param.required());
        }
        // 检查@PathVariable注解
        if (parameter.isAnnotationPresent(PathVariable.class)) {
            PathVariable ann = parameter.getAnnotation(PathVariable.class);
            if (!ann.value().isEmpty()) {
                if(apiParam.getName() == null){
                    apiParam.setName(ann.value());
                }
            }
            apiParam.setIn("path");
        }

        // @NotNull
        if (parameter.isAnnotationPresent(NotNull.class)) {
            apiParam.setRequired(true);
        }

        // 类型
        apiParam.setType(parameter.getType().getSimpleName());
        if(apiParam.getName() == null){
            // 使用参数实际名称（需要编译时保留参数名）
            if (parameter.isNamePresent()) {
                apiParam.setName(parameter.getName());
            }
        }

        return apiParam;
    }

    /**
     * 解析请求体参数
     * @param type 类
     * @return List<ApiParameter>
     */
    private ApiParameter parseBodyParameter(Class<?> type) {
        ApiParameter parameter = new ApiParameter();
        parameter.setIn("body");

        // 基础类型
        if (type.isPrimitive() || isWrapperType(type)) {
            parameter.setType(type.getTypeName());
        }

        // 集合类型
        if (Collection.class.isAssignableFrom(type)) {
            return parseCollectionSchema(type);
        }

        // 数组类型
        if (type.isArray()) {
            parameter.setType(getTypeName(type.getComponentType()) + "[]");
            return parameter;
        }

        // 复杂对象
        return parseComplexSchema(type);
    }

    /**
     * 获取类型的规范化名称
     * @param type Java类型
     * @return 类型名称字符串
     */
    public String getTypeName(Class<?> type) {
        if (type == null) {
            return "object";
        }
        // 处理基本类型
        if (type.isPrimitive()) {
            return type.getSimpleName();
        }

        // 处理包装类型
        if (isWrapperType(type)) {
            return type.getSimpleName().toLowerCase();
        }

        // 处理数组
        if (type.isArray()) {
            return getTypeName(type.getComponentType()) + "[]";
        }

        // 默认返回简单类名
        return type.getSimpleName();
    }

    /**
     * 解析集合类型Schema
     * @param collectionType 集合类型
     * @return Schema对象
     */
    private ApiParameter parseCollectionSchema(Class<?> collectionType) {
        ApiParameter apiParameter = new ApiParameter();
        apiParameter.setType("array");

        // 获取集合元素的类型，并递归解析
        Type genericType = collectionType.getGenericSuperclass();
        if (genericType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            if (typeArguments.length > 0) {
                Class<?> elementType = (Class<?>) typeArguments[0];
                apiParameter.addProperty("items",parseBodyParameter(elementType));
            }
        }

        return apiParameter;
    }

    /**
     * 解析请求体的复杂对象
     * @param type class
     * @return ApiParameter
     */
    private ApiParameter parseComplexSchema(Class<?> type){
        ApiParameter parameter = new ApiParameter();
        Arrays.stream(type.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .forEach(field -> {
                    ApiParameter fieldParameter = this.parseBodyParameter(field.getType());
                    parameter.addProperty(field.getName(),fieldParameter);
                });
        return parameter;
    }

}
