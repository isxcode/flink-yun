package com.isxcode.acorn.modules.form.mapper;

import com.isxcode.acorn.api.form.req.AddFormReq;
import com.isxcode.acorn.api.form.res.AddFormRes;
import com.isxcode.acorn.api.form.res.FormPageRes;
import com.isxcode.acorn.api.form.res.GetFormLinkInfoRes;
import com.isxcode.acorn.modules.form.entity.FormEntity;
import com.isxcode.acorn.modules.form.entity.FormLinkEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FormMapper {

    FormEntity addFormReqToFormEntity(AddFormReq addFormReq);

    AddFormRes formEntityToAddFormRes(FormEntity formEntity);

    FormPageRes formEntityToFormPageRes(FormEntity formEntity);

    GetFormLinkInfoRes formLinkEntityToGetFormLinkInfoRes(FormLinkEntity formLinkEntity);
}
