package com.flower.pms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
public class ApiDocController {

    private final RequestMappingHandlerMapping handlerMapping;

    public ApiDocController(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @GetMapping("/api-docs")
    public String apiDocs(Model model) {
        List<ApiEndpoint> endpoints = new ArrayList<>();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod method = entry.getValue();

            if (!method.getBeanType().getName().startsWith("com.flower.pms.controller")) {
                continue;
            }
            if (method.getBeanType() == HomeController.class || method.getBeanType() == ApiDocController.class) {
                continue;
            }

            String path = info.getPatternsCondition().getPatterns().stream().findFirst().orElse("/unknown");
            String httpMethod = info.getMethodsCondition().getMethods().stream()
                    .map(Enum::name)
                    .findFirst()
                    .orElse("ANY");

            String description = generateDescription(method.getMethod().getName(), path, httpMethod);

            endpoints.add(new ApiEndpoint(httpMethod, path, description));
        }

        endpoints.sort(Comparator.comparing(ApiEndpoint::getPath));

        model.addAttribute("endpoints", endpoints);
        return "api-docs";
    }

    private String generateDescription(String methodName, String path, String httpMethod) {
        if (path.contains("/projects")) {
            if (methodName.equals("list") || httpMethod.equals("GET") && !path.contains("{")) return "项目列表";
            if (methodName.equals("createForm") || path.endsWith("/new")) return "新增项目表单";
            if (methodName.equals("save")) return "保存项目";
            if (methodName.equals("editForm")) return "编辑项目表单";
            if (methodName.equals("update")) return "更新项目";
            if (methodName.equals("delete")) return "删除项目";
            if (methodName.equals("batchDelete")) return "批量删除项目";
        } else if (path.contains("/users")) {
            return inferCrudDescription(methodName, "用户");
        } else if (path.contains("/staffs")) {
            return inferCrudDescription(methodName, "人员");
        } else if (path.contains("/roles")) {
            return inferCrudDescription(methodName, "角色");
        }
        return "未分类接口";
    }

    private String inferCrudDescription(String methodName, String entity) {
        if (methodName.contains("list") || methodName.equals("get")) return entity + "列表";
        if (methodName.contains("save") || methodName.contains("create")) return "新增" + entity;
        if (methodName.contains("update") || methodName.contains("edit")) return "更新" + entity;
        if (methodName.contains("delete")) return "删除" + entity;
        if (methodName.contains("batch")) return "批量操作" + entity;
        return "管理" + entity;
    }

    public static class ApiEndpoint {
        private final String method;
        private final String path;
        private final String description;

        public ApiEndpoint(String method, String path, String description) {
            this.method = method;
            this.path = path;
            this.description = description;
        }

        public String getMethod() { return method; }
        public String getPath() { return path; }
        public String getDescription() { return description; }
    }
}
