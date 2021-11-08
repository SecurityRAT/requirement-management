package org.securityrat.requirementmanagement.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;

import java.io.Serializable;

/**
 * Implements ternary relation of req. skeleton, attribute an extension.
 * Requirement extension can be added to a skeleton if a particular attribute is set.
 */
@ApiModel(description = "Implements ternary relation of req. skeleton, attribute an extension. Requirement extension can be added to a skeleton if a particular attribute is set.")
@Entity
@Table(name = "sk_at_ex")
public class SkAtEx implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value="skAtExes", allowSetters = true)
    private Skeleton skeleton;

    @ManyToOne
    @JsonIgnoreProperties(value="skAtExes", allowSetters = true)
    private Attribute attribute;

    @ManyToOne
    @JsonIgnoreProperties(value="skAtExes", allowSetters = true)
    private Extension extension;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public SkAtEx skeleton(Skeleton skeleton) {
        this.skeleton = skeleton;
        return this;
    }

    public void setSkeleton(Skeleton skeleton) {
        this.skeleton = skeleton;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public SkAtEx attribute(Attribute attribute) {
        this.attribute = attribute;
        return this;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public Extension getExtension() {
        return extension;
    }

    public SkAtEx extension(Extension extension) {
        this.extension = extension;
        return this;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkAtEx)) {
            return false;
        }
        return id != null && id.equals(((SkAtEx) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SkAtEx{" +
            "id=" + getId() +
            "}";
    }
}
