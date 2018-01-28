package org.securityrat.requirementmanagement.service.dto;

import java.io.Serializable;
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
 * Criteria class for the ExtensionKey entity. This class is used in ExtensionKeyResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /extension-keys?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExtensionKeyCriteria implements Serializable {
    /**
     * Class for filtering ExtensionSection
     */
    public static class ExtensionSectionFilter extends Filter<ExtensionSection> {
    }

    /**
     * Class for filtering ExtensionType
     */
    public static class ExtensionTypeFilter extends Filter<ExtensionType> {
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

    public ExtensionKeyCriteria() {
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
