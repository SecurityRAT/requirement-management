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
 * Requirements are organized in different sets.
 * These define also the structure of contained requirements.
 * Selecting a suitable requirement set is the initial step when creating a 'case'.
 */
@ApiModel(description = "Requirements are organized in different sets. These define also the structure of contained requirements. Selecting a suitable requirement set is the initial step when creating a 'case'.")
@Entity
@Table(name = "requirement_set")
public class RequirementSet implements Serializable {

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

    @OneToMany(mappedBy = "requirementSet")
    @JsonIgnore
    private Set<AttributeKey> attributeKeys = new HashSet<>();

    @OneToMany(mappedBy = "requirementSet")
    @JsonIgnore
    private Set<Skeleton> skeletons = new HashSet<>();

    @OneToMany(mappedBy = "requirementSet")
    @JsonIgnore
    private Set<ExtensionKey> extensionKeys = new HashSet<>();

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

    public RequirementSet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public RequirementSet description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public RequirementSet showOrder(Integer showOrder) {
        this.showOrder = showOrder;
        return this;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public Boolean isActive() {
        return active;
    }

    public RequirementSet active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<AttributeKey> getAttributeKeys() {
        return attributeKeys;
    }

    public RequirementSet attributeKeys(Set<AttributeKey> attributeKeys) {
        this.attributeKeys = attributeKeys;
        return this;
    }

    public RequirementSet addAttributeKey(AttributeKey attributeKey) {
        this.attributeKeys.add(attributeKey);
        attributeKey.setRequirementSet(this);
        return this;
    }

    public RequirementSet removeAttributeKey(AttributeKey attributeKey) {
        this.attributeKeys.remove(attributeKey);
        attributeKey.setRequirementSet(null);
        return this;
    }

    public void setAttributeKeys(Set<AttributeKey> attributeKeys) {
        this.attributeKeys = attributeKeys;
    }

    public Set<Skeleton> getSkeletons() {
        return skeletons;
    }

    public RequirementSet skeletons(Set<Skeleton> skeletons) {
        this.skeletons = skeletons;
        return this;
    }

    public RequirementSet addSkeleton(Skeleton skeleton) {
        this.skeletons.add(skeleton);
        skeleton.setRequirementSet(this);
        return this;
    }

    public RequirementSet removeSkeleton(Skeleton skeleton) {
        this.skeletons.remove(skeleton);
        skeleton.setRequirementSet(null);
        return this;
    }

    public void setSkeletons(Set<Skeleton> skeletons) {
        this.skeletons = skeletons;
    }

    public Set<ExtensionKey> getExtensionKeys() {
        return extensionKeys;
    }

    public RequirementSet extensionKeys(Set<ExtensionKey> extensionKeys) {
        this.extensionKeys = extensionKeys;
        return this;
    }

    public RequirementSet addExtensionKey(ExtensionKey extensionKey) {
        this.extensionKeys.add(extensionKey);
        extensionKey.setRequirementSet(this);
        return this;
    }

    public RequirementSet removeExtensionKey(ExtensionKey extensionKey) {
        this.extensionKeys.remove(extensionKey);
        extensionKey.setRequirementSet(null);
        return this;
    }

    public void setExtensionKeys(Set<ExtensionKey> extensionKeys) {
        this.extensionKeys = extensionKeys;
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
        RequirementSet requirementSet = (RequirementSet) o;
        if (requirementSet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requirementSet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RequirementSet{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", showOrder=" + getShowOrder() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
