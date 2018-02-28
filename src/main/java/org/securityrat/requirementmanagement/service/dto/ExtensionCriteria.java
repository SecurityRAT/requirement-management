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
 * Criteria class for the Extension entity. This class is used in ExtensionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /extensions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExtensionCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private IntegerFilter showOrder;

    private BooleanFilter active;

    private LongFilter skAtExId;

    private LongFilter extensionKeyId;

    public ExtensionCriteria() {
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
