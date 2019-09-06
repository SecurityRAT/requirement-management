package org.securityrat.requirementmanagement.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The 'core' part of a particular requirement.
 */
@ApiModel(description = "The 'core' part of a particular requirement.")
@Entity
@Table(name = "skeleton")
public class Skeleton implements Serializable {

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

    @OneToMany(mappedBy = "skeleton")
    private Set<SkAtEx> skAtExes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("skeletons")
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

    public Skeleton name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Skeleton description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public Skeleton showOrder(Integer showOrder) {
        this.showOrder = showOrder;
        return this;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public Boolean isActive() {
        return active;
    }

    public Skeleton active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<SkAtEx> getSkAtExes() {
        return skAtExes;
    }

    public Skeleton skAtExes(Set<SkAtEx> skAtExes) {
        this.skAtExes = skAtExes;
        return this;
    }

    public Skeleton addSkAtEx(SkAtEx skAtEx) {
        this.skAtExes.add(skAtEx);
        skAtEx.setSkeleton(this);
        return this;
    }

    public Skeleton removeSkAtEx(SkAtEx skAtEx) {
        this.skAtExes.remove(skAtEx);
        skAtEx.setSkeleton(null);
        return this;
    }

    public void setSkAtExes(Set<SkAtEx> skAtExes) {
        this.skAtExes = skAtExes;
    }

    public RequirementSet getRequirementSet() {
        return requirementSet;
    }

    public Skeleton requirementSet(RequirementSet requirementSet) {
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
        if (!(o instanceof Skeleton)) {
            return false;
        }
        return id != null && id.equals(((Skeleton) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Skeleton{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", showOrder=" + getShowOrder() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
