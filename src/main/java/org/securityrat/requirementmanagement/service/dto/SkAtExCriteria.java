package org.securityrat.requirementmanagement.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the SkAtEx entity. This class is used in SkAtExResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sk-at-exes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SkAtExCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter skeletonId;

    private LongFilter attributeId;

    private LongFilter extensionId;

    public SkAtExCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getSkeletonId() {
        return skeletonId;
    }

    public void setSkeletonId(LongFilter skeletonId) {
        this.skeletonId = skeletonId;
    }

    public LongFilter getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(LongFilter attributeId) {
        this.attributeId = attributeId;
    }

    public LongFilter getExtensionId() {
        return extensionId;
    }

    public void setExtensionId(LongFilter extensionId) {
        this.extensionId = extensionId;
    }

    @Override
    public String toString() {
        return "SkAtExCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (skeletonId != null ? "skeletonId=" + skeletonId + ", " : "") +
                (attributeId != null ? "attributeId=" + attributeId + ", " : "") +
                (extensionId != null ? "extensionId=" + extensionId + ", " : "") +
            "}";
    }

}
