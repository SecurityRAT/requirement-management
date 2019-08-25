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
 * Criteria class for the {@link org.securityrat.requirementmanagement.domain.SkAtEx} entity. This class is used
 * in {@link org.securityrat.requirementmanagement.web.rest.SkAtExResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sk-at-exes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SkAtExCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter skeletonId;

    private LongFilter attributeId;

    private LongFilter extensionId;

    public SkAtExCriteria(){
    }

    public SkAtExCriteria(SkAtExCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.skeletonId = other.skeletonId == null ? null : other.skeletonId.copy();
        this.attributeId = other.attributeId == null ? null : other.attributeId.copy();
        this.extensionId = other.extensionId == null ? null : other.extensionId.copy();
    }

    @Override
    public SkAtExCriteria copy() {
        return new SkAtExCriteria(this);
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SkAtExCriteria that = (SkAtExCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(skeletonId, that.skeletonId) &&
            Objects.equals(attributeId, that.attributeId) &&
            Objects.equals(extensionId, that.extensionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        skeletonId,
        attributeId,
        extensionId
        );
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
