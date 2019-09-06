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
 * Criteria class for the {@link org.securityrat.requirementmanagement.domain.Skeleton} entity. This class is used
 * in {@link org.securityrat.requirementmanagement.web.rest.SkeletonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /skeletons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SkeletonCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter showOrder;

    private BooleanFilter active;

    private LongFilter skAtExId;

    private LongFilter requirementSetId;

    public SkeletonCriteria(){
    }

    public SkeletonCriteria(SkeletonCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.showOrder = other.showOrder == null ? null : other.showOrder.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.skAtExId = other.skAtExId == null ? null : other.skAtExId.copy();
        this.requirementSetId = other.requirementSetId == null ? null : other.requirementSetId.copy();
    }

    @Override
    public SkeletonCriteria copy() {
        return new SkeletonCriteria(this);
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

    public LongFilter getSkAtExId() {
        return skAtExId;
    }

    public void setSkAtExId(LongFilter skAtExId) {
        this.skAtExId = skAtExId;
    }

    public LongFilter getRequirementSetId() {
        return requirementSetId;
    }

    public void setRequirementSetId(LongFilter requirementSetId) {
        this.requirementSetId = requirementSetId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SkeletonCriteria that = (SkeletonCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(showOrder, that.showOrder) &&
            Objects.equals(active, that.active) &&
            Objects.equals(skAtExId, that.skAtExId) &&
            Objects.equals(requirementSetId, that.requirementSetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        showOrder,
        active,
        skAtExId,
        requirementSetId
        );
    }

    @Override
    public String toString() {
        return "SkeletonCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (showOrder != null ? "showOrder=" + showOrder + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (skAtExId != null ? "skAtExId=" + skAtExId + ", " : "") +
                (requirementSetId != null ? "requirementSetId=" + requirementSetId + ", " : "") +
            "}";
    }

}
