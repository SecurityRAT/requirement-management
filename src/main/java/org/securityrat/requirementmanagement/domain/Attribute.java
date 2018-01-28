package org.securityrat.requirementmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Every requirement can be classified in many ways
 * according to different attributes.
 */
@ApiModel(description = "Every requirement can be classified in many ways according to different attributes.")
@Entity
@Table(name = "attribute")
public class Attribute implements Serializable {

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

    @Column(name = "show_order")
    private Integer showOrder;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "attribute")
    @JsonIgnore
    private Set<SkAtEx> skAtExes = new HashSet<>();

    @ManyToOne
    private Attribute parent;

    @ManyToOne
    private AttributeKey attributeKey;

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

    public Attribute name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Attribute description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public Attribute showOrder(Integer showOrder) {
        this.showOrder = showOrder;
        return this;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public Boolean isActive() {
        return active;
    }

    public Attribute active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<SkAtEx> getSkAtExes() {
        return skAtExes;
    }

    public Attribute skAtExes(Set<SkAtEx> skAtExes) {
        this.skAtExes = skAtExes;
        return this;
    }

    public Attribute addSkAtEx(SkAtEx skAtEx) {
        this.skAtExes.add(skAtEx);
        skAtEx.setAttribute(this);
        return this;
    }

    public Attribute removeSkAtEx(SkAtEx skAtEx) {
        this.skAtExes.remove(skAtEx);
        skAtEx.setAttribute(null);
        return this;
    }

    public void setSkAtExes(Set<SkAtEx> skAtExes) {
        this.skAtExes = skAtExes;
    }

    public Attribute getParent() {
        return parent;
    }

    public Attribute parent(Attribute attribute) {
        this.parent = attribute;
        return this;
    }

    public void setParent(Attribute attribute) {
        this.parent = attribute;
    }

    public AttributeKey getAttributeKey() {
        return attributeKey;
    }

    public Attribute attributeKey(AttributeKey attributeKey) {
        this.attributeKey = attributeKey;
        return this;
    }

    public void setAttributeKey(AttributeKey attributeKey) {
        this.attributeKey = attributeKey;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attribute attribute = (Attribute) o;
        if (attribute.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attribute.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Attribute{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", showOrder=" + getShowOrder() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
