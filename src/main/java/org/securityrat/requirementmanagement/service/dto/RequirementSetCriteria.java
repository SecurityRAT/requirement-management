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
 * Criteria class for the RequirementSet entity. This class is used in RequirementSetResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /requirement-sets?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RequirementSetCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private IntegerFilter showOrder;

    private BooleanFilter active;

    private LongFilter attributeKeyId;

    private LongFilter skeletonId;

    private LongFilter extensionKeyId;

    public RequirementSetCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(IntegerFilter showOrder) {
        this.showOrder = showOrder;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getAttributeKeyId() {
        return attributeKeyId;
    }

    public void setAttributeKeyId(LongFilter attributeKeyId) {
        this.attributeKeyId = attributeKeyId;
    }

    public LongFilter getSkeletonId() {
        return skeletonId;
    }

    public void setSkeletonId(LongFilter skeletonId) {
        this.skeletonId = skeletonId;
    }

    public LongFilter getExtensionKeyId() {
        return extensionKeyId;
    }

    public void setExtensionKeyId(LongFilter extensionKeyId) {
        this.extensionKeyId = extensionKeyId;
    }

    @Override
    public String toString() {
        return "RequirementSetCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (showOrder != null ? "showOrder=" + showOrder + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (attributeKeyId != null ? "attributeKeyId=" + attributeKeyId + ", " : "") +
                (skeletonId != null ? "skeletonId=" + skeletonId + ", " : "") +
                (extensionKeyId != null ? "extensionKeyId=" + extensionKeyId + ", " : "") +
            "}";
    }

}
