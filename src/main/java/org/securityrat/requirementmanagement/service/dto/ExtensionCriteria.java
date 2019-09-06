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
 * Criteria class for the {@link org.securityrat.requirementmanagement.domain.Extension} entity. This class is used
 * in {@link org.securityrat.requirementmanagement.web.rest.ExtensionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /extensions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExtensionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter showOrder;

    private BooleanFilter active;

    private LongFilter skAtExId;

    private LongFilter extensionKeyId;

    public ExtensionCriteria(){
    }

    public ExtensionCriteria(ExtensionCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.showOrder = other.showOrder == null ? null : other.showOrder.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.skAtExId = other.skAtExId == null ? null : other.skAtExId.copy();
        this.extensionKeyId = other.extensionKeyId == null ? null : other.extensionKeyId.copy();
    }

    @Override
    public ExtensionCriteria copy() {
        return new ExtensionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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

    public LongFilter getSkAtExId() {
        return skAtExId;
    }

    public void setSkAtExId(LongFilter skAtExId) {
        this.skAtExId = skAtExId;
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
        final ExtensionCriteria that = (ExtensionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(showOrder, that.showOrder) &&
            Objects.equals(active, that.active) &&
            Objects.equals(skAtExId, that.skAtExId) &&
            Objects.equals(extensionKeyId, that.extensionKeyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        showOrder,
        active,
        skAtExId,
        extensionKeyId
        );
    }

    @Override
    public String toString() {
        return "ExtensionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (showOrder != null ? "showOrder=" + showOrder + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (skAtExId != null ? "skAtExId=" + skAtExId + ", " : "") +
                (extensionKeyId != null ? "extensionKeyId=" + extensionKeyId + ", " : "") +
            "}";
    }

}
