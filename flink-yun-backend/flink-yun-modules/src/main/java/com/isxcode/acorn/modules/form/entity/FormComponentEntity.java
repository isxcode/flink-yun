package com.isxcode.acorn.modules.form.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.isxcode.acorn.common.config.CommonConfig.TENANT_ID;

@Data
@Entity
@SQLDelete(sql = "UPDATE FY_FORM_COMPONENT SET deleted = 1 WHERE id = ? and version_number = ?")
@Where(clause = "deleted = 0 ${TENANT_FILTER} ")
@Table(name = "FY_FORM_COMPONENT")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@EntityListeners(AuditingEntityListener.class)
public class FormComponentEntity {

    @Id
    @GeneratedValue(generator = "sy-id-generator")
    @GenericGenerator(name = "sy-id-generator", strategy = "com.isxcode.acorn.config.GeneratedValueConfig")
    private String id;

    private String formId;

    private String uuid;

    private String componentConfig;

    @CreatedDate
    private LocalDateTime createDateTime;

    @LastModifiedDate
    private LocalDateTime lastModifiedDateTime;

    @CreatedBy
    private String createBy;

    @LastModifiedBy
    private String lastModifiedBy;

    @Version
    private Long versionNumber;

    @Transient
    private Integer deleted;

    private String tenantId;

    @PrePersist
    public void prePersist() {
        this.tenantId = TENANT_ID.get();
    }
}
