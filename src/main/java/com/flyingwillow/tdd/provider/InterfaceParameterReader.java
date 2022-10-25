package com.flyingwillow.tdd.provider;

import com.flyingwillow.tdd.domain.FieldDataType;
import com.flyingwillow.tdd.domain.ParameterItem;
import com.intellij.psi.PsiElement;

import java.util.List;

public interface InterfaceParameterReader {

    String REQUEST_BODY = "org.springframework.web.bind.annotation.RequestBody";
    String REQUEST_PARAM = "org.springframework.web.bind.annotation.RequestParam";

    String SWAGGER_PARAM_ANNOTATION = "io.swagger.annotations.ApiParam";
    String SWAGGER_PARAMS_ANNOTATION = "io.swagger.annotations.ApiImplicitParams";
    String SWAGGER_MODEL_ANNOTATION = "io.swagger.annotations.ApiModel";
    String SWAGGER_MODEL_PROPERTY_ANNOTATION = "io.swagger.annotations.ApiModelProperty";

    String getComment();

    String getName();

    Boolean getRequired();

    String getDefaultValue();

    FieldDataType getDataType();

    String getQualifiedName();

    List<ParameterItem> loadChildren();

    boolean isSimpleType();
}
