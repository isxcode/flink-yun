package com.isxcode.acorn.modules.form.mapper;

import com.isxcode.acorn.api.form.pojos.dto.FormComponentDto;
import com.isxcode.acorn.api.form.pojos.req.AddFormReq;
import com.isxcode.acorn.api.form.pojos.res.AddFormRes;
import com.isxcode.acorn.api.form.pojos.res.FormPageRes;
import com.isxcode.acorn.api.form.pojos.res.GetFormLinkInfoRes;
import com.isxcode.acorn.modules.form.entity.FormComponentEntity;
import com.isxcode.acorn.modules.form.entity.FormEntity;
import com.isxcode.acorn.modules.form.entity.FormLinkEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * mapstruct映射.
 */
@Mapper(componentModel = "spring")
public interface FormMapper {

    FormEntity addFormReqToFormEntity(AddFormReq addFormReq);

    AddFormRes formEntityToAddFormRes(FormEntity formEntity);

    FormPageRes formEntityToFormPageRes(FormEntity formEntity);

    List<FormComponentEntity> fomComponentListToFormComponentEntityList(List<FormComponentDto> fomComponentDto);

    GetFormLinkInfoRes formLinkEntityToGetFormLinkInfoRes(FormLinkEntity formLinkEntity);
}
