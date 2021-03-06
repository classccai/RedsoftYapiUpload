package com.redsoft.idea.plugin.yapiv2.req.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.parser.ObjectJsonParser;
import com.redsoft.idea.plugin.yapiv2.parser.impl.Json5ParserImpl;
import com.redsoft.idea.plugin.yapiv2.parser.impl.JsonSchemaParserImpl;
import com.redsoft.idea.plugin.yapiv2.req.PsiParamFilter;
import com.redsoft.idea.plugin.yapiv2.req.abs.AbstractRequestParamResolver;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import org.jetbrains.annotations.NotNull;

public class RequestBodyResolverImpl extends AbstractRequestParamResolver {

    private final ObjectJsonParser objectJsonParser;

    private final int dataMode;

    public RequestBodyResolverImpl(YApiProjectProperty property, Project project) {
        this.dataMode = property.getDataMode();
        if (this.dataMode == 0) {
            this.objectJsonParser = new JsonSchemaParserImpl(property, project);
        } else {
            this.objectJsonParser = new Json5ParserImpl(property, project);
        }
    }

    @NotNull
    @Override
    public PsiParamFilter getPsiParamFilter(@NotNull PsiMethod m,
            @NotNull YApiParam target) {
        return p -> PsiAnnotationUtils.isAnnotatedWith(p, SpringMVCConstants.RequestBody);
    }

    @Override
    public void doResolverItem(@NotNull PsiMethod m, @NotNull PsiParameter param,
            @NotNull YApiParam target) {
        target.setRequestBody(this.objectJsonParser.getJsonResponse(param.getType()));
        target.setReq_body_is_json_schema(this.dataMode == 0);
    }

}
