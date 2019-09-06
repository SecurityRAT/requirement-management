package org.securityrat.requirementmanagement.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.securityrat.requirementmanagement.domain.enumeration.ExtensionSection;

import org.securityrat.requirementmanagement.domain.enumeration.ExtensionType;

/**
 * Describes properties of one type of a requirement extension.
 */
@ApiModel(description = "Describes properties of one type of a requirement extension.")
@Entity
@Table(name = "extension_key")
public class ExtensionKey implements Serializable {

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
    @Column(name = "section", nullable = false)
    private ExtensionSection section;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ExtensionType type;

    @Column(name = "show_order")
    private Integer showOrder;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "extensionKey")
    private Set<Extension> extensions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("extensionKeys")
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

    public ExtensionKey name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public ExtensionKey description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExtensionSection getSection() {
        return section;
    }

    public ExtensionKey section(ExtensionSection section) {
        this.section = section;
        return this;
    }

    public void setSection(ExtensionSection section) {
        this.section = section;
    }

    public ExtensionType getType() {
        return type;
    }

    public ExtensionKey type(ExtensionType type) {
        this.type = type;
        return this;
    }

    public void setType(ExtensionType type) {
        this.type = type;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public ExtensionKey showOrder(Integer showOrder) {
        this.showOrder = showOrder;
        return this;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public Boolean isActive() {
        return active;
    }

    public ExtensionKey active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Extension> getExtensions() {
        return extensions;
    }

    public ExtensionKey extensions(Set<Extension> extensions) {
        this.extensions = extensions;
        return this;
    }

    public ExtensionKey addExtension(Extension extension) {
        this.extensions.add(extension);
        extension.setExtensionKey(this);
        return this;
    }

    public ExtensionKey removeExtension(Extension extension) {
        this.extensions.remove(extension);
        extension.setExtensionKey(null);
        return this;
    }

    public void setExtensions(Set<Extension> extensions) {
        this.extensions = extensions;
    }

    public RequirementSet getRequirementSet() {
        return requirementSet;
    }

    public ExtensionKey requirementSet(RequirementSet requirementSet) {
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
        if (!(o instanceof ExtensionKey)) {
            return false;
        }
        return id != null && id.equals(((ExtensionKey) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ExtensionKey{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", section='" + getSection() + "'" +
            ", type='" + getType() + "'" +
            ", showOrder=" + getShowOrder() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
