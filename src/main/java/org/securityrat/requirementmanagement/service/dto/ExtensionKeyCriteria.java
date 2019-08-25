package org.securityrat.requirementmanagement.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import org.securityrat.requirementmanagement.domain.enumeration.ExtensionSection;
import org.securityrat.requirementmanagement.domain.enumeration.ExtensionType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link org.securityrat.requirementmanagement.domain.ExtensionKey} entity. This class is used
 * in {@link org.securityrat.requirementmanagement.web.rest.ExtensionKeyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /extension-keys?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExtensionKeyCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ExtensionSection
     */
    public static class ExtensionSectionFilter extends Filter<ExtensionSection> {

        public ExtensionSectionFilter() {
        }

        public ExtensionSectionFilter(ExtensionSectionFilter filter) {
            super(filter);
        }

        @Override
        public ExtensionSectionFilter copy() {
            return new ExtensionSectionFilter(this);
        }

    }
    /**
     * Class for filtering ExtensionType
     */
    public static class ExtensionTypeFilter extends Filter<ExtensionType> {

        public ExtensionTypeFilter() {
        }

        public ExtensionTypeFilter(ExtensionTypeFilter filter) {
            super(filter);
        }

        @Override
        public ExtensionTypeFilter copy() {
            return new ExtensionTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ExtensionSectionFilter section;

    private ExtensionTypeFilter type;

    private IntegerFilter showOrder;

    private BooleanFilter active;

    private LongFilter extensionId;

    private LongFilter requirementSetId;

    public ExtensionKeyCriteria(){
    }

    public ExtensionKeyCriteria(ExtensionKeyCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.section = other.section == null ? null : other.section.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.showOrder = other.showOrder == null ? null : other.showOrder.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.extensionId = other.extensionId == null ? null : other.extensionId.copy();
        this.requirementSetId = other.requirementSetId == null ? null : other.requirementSetId.copy();
    }

    @Override
    public ExtensionKeyCriteria copy() {
        return new ExtensionKeyCriteria(this);
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

    public ExtensionSectionFilter getSection() {
        return section;
    }

    public void setSection(ExtensionSectionFilter section) {
        this.section = section;
    }

    public ExtensionTypeFilter getType() {
        return type;
    }

    public void setType(ExtensionTypeFilter type) {
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

    public LongFilter getExtensionId() {
        return extensionId;
    }

    public void setExtensionId(LongFilter extensionId) {
        this.extensionId = extensionId;
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
        final ExtensionKeyCriteria that = (ExtensionKeyCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(section, that.section) &&
            Objects.equals(type, that.type) &&
            Objects.equals(showOrder, that.showOrder) &&
            Objects.equals(active, that.active) &&
            Objects.equals(extensionId, that.extensionId) &&
            Objects.equals(requirementSetId, that.requirementSetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        section,
        type,
        showOrder,
        active,
        extensionId,
        requirementSetId
        );
    }

    @Override
    public String toString() {
        return "ExtensionKeyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (section != null ? "section=" + section + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (showOrder != null ? "showOrder=" + showOrder + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (extensionId != null ? "extensionId=" + extensionId + ", " : "") +
                (requirementSetId != null ? "requirementSetId=" + requirementSetId + ", " : "") +
            "}";
    }

}
