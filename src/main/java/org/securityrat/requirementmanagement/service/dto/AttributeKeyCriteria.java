package org.securityrat.requirementmanagement.service.dto;

import java.io.Serializable;
import org.securityrat.requirementmanagement.domain.enumeration.AttributeType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the AttributeKey entity. This class is used in AttributeKeyResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /attribute-keys?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AttributeKeyCriteria implements Serializable {
    /**
     * Class for filtering AttributeType
     */
    public static class AttributeTypeFilter extends Filter<AttributeType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private AttributeTypeFilter type;

    private IntegerFilter showOrder;

    private BooleanFilter active;

    private LongFilter attributeId;

    private LongFilter requirementSetId;

    public AttributeKeyCriteria() {
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
