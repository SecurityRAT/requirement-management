package org.securityrat.requirementmanagement.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link org.securityrat.requirementmanagement.domain.RequirementSet} entity. This class is used
 * in {@link org.securityrat.requirementmanagement.web.rest.RequirementSetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /requirement-sets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RequirementSetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter showOrder;

    private BooleanFilter active;

    private LongFilter attributeKeyId;

    private LongFilter skeletonId;

    private LongFilter extensionKeyId;

    public RequirementSetCriteria(){
    }

    public RequirementSetCriteria(RequirementSetCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.showOrder = other.showOrder == null ? null : other.showOrder.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.attributeKeyId = other.attributeKeyId == null ? null : other.attributeKeyId.copy();
        this.skeletonId = other.skeletonId == null ? null : other.skeletonId.copy();
        this.extensionKeyId = other.extensionKeyId == null ? null : other.extensionKeyId.copy();
    }

    @Override
    public RequirementSetCriteria copy() {
        return new RequirementSetCriteria(this);
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RequirementSetCriteria that = (RequirementSetCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(showOrder, that.showOrder) &&
            Objects.equals(active, that.active) &&
            Objects.equals(attributeKeyId, that.attributeKeyId) &&
            Objects.equals(skeletonId, that.skeletonId) &&
            Objects.equals(extensionKeyId, that.extensionKeyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        showOrder,
        active,
        attributeKeyId,
        skeletonId,
        extensionKeyId
        );
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
