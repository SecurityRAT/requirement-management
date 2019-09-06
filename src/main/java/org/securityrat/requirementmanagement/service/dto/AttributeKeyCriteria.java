package org.securityrat.requirementmanagement.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import org.securityrat.requirementmanagement.domain.enumeration.AttributeType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link org.securityrat.requirementmanagement.domain.AttributeKey} entity. This class is used
 * in {@link org.securityrat.requirementmanagement.web.rest.AttributeKeyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /attribute-keys?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AttributeKeyCriteria implements Serializable, Criteria {
    /**
     * Class for filtering AttributeType
     */
    public static class AttributeTypeFilter extends Filter<AttributeType> {

        public AttributeTypeFilter() {
        }

        public AttributeTypeFilter(AttributeTypeFilter filter) {
            super(filter);
        }

        @Override
        public AttributeTypeFilter copy() {
            return new AttributeTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private AttributeTypeFilter type;

    private IntegerFilter showOrder;

    private BooleanFilter active;

    private LongFilter attributeId;

    private LongFilter requirementSetId;

    public AttributeKeyCriteria(){
    }

    public AttributeKeyCriteria(AttributeKeyCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.showOrder = other.showOrder == null ? null : other.showOrder.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.attributeId = other.attributeId == null ? null : other.attributeId.copy();
        this.requirementSetId = other.requirementSetId == null ? null : other.requirementSetId.copy();
    }

    @Override
    public AttributeKeyCriteria copy() {
        return new AttributeKeyCriteria(this);
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

    public AttributeTypeFilter getType() {
        return type;
    }

    public void setType(AttributeTypeFilter type) {
        this.type = type;
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

    public LongFilter getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(LongFilter attributeId) {
        this.attributeId = attributeId;
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
        final AttributeKeyCriteria that = (AttributeKeyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(showOrder, that.showOrder) &&
            Objects.equals(active, that.active) &&
            Objects.equals(attributeId, that.attributeId) &&
            Objects.equals(requirementSetId, that.requirementSetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        type,
        showOrder,
        active,
        attributeId,
        requirementSetId
        );
    }

    @Override
    public String toString() {
        return "AttributeKeyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (showOrder != null ? "showOrder=" + showOrder + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (attributeId != null ? "attributeId=" + attributeId + ", " : "") +
                (requirementSetId != null ? "requirementSetId=" + requirementSetId + ", " : "") +
            "}";
    }

}
