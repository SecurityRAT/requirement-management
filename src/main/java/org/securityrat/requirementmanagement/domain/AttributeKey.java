package org.securityrat.requirementmanagement.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.securityrat.requirementmanagement.domain.enumeration.AttributeType;

/**
 * Describes one group/collection of requirement attributes.
 * E.g. 'Criticality' for Low, Medium, High.
 */
@ApiModel(description = "Describes one group/collection of requirement attributes. E.g. 'Criticality' for Low, Medium, High.")
@Entity
@Table(name = "attribute_key")
public class AttributeKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AttributeType type;

    @Column(name = "show_order")
    private Integer showOrder;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "attributeKey")
    private Set<Attribute> attributes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("attributeKeys")
    private RequirementSet requirementSet;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AttributeKey name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public AttributeKey description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AttributeType getType() {
        return type;
    }

    public AttributeKey type(AttributeType type) {
        this.type = type;
        return this;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public AttributeKey showOrder(Integer showOrder) {
        this.showOrder = showOrder;
        return this;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public Boolean isActive() {
        return active;
    }

    public AttributeKey active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public AttributeKey attributes(Set<Attribute> attributes) {
        this.attributes = attributes;
        return this;
    }

    public AttributeKey addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
        attribute.setAttributeKey(this);
        return this;
    }

    public AttributeKey removeAttribute(Attribute attribute) {
        this.attributes.remove(attribute);
        attribute.setAttributeKey(null);
        return this;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    public RequirementSet getRequirementSet() {
        return requirementSet;
    }

    public AttributeKey requirementSet(RequirementSet requirementSet) {
        this.requirementSet = requirementSet;
        return this;
    }

    public void setRequirementSet(RequirementSet requirementSet) {
        this.requirementSet = requirementSet;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributeKey)) {
            return false;
        }
        return id != null && id.equals(((AttributeKey) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AttributeKey{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", showOrder=" + getShowOrder() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
