package org.securityrat.requirementmanagement.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Requirement extension (extending the requirement skeleton).
 */
@ApiModel(description = "Requirement extension (extending the requirement skeleton).")
@Entity
@Table(name = "extension")
public class Extension implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "show_order")
    private Integer showOrder;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "extension")
    private Set<SkAtEx> skAtExes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("extensions")
    private ExtensionKey extensionKey;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public Extension content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public Extension description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public Extension showOrder(Integer showOrder) {
        this.showOrder = showOrder;
        return this;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public Boolean isActive() {
        return active;
    }

    public Extension active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<SkAtEx> getSkAtExes() {
        return skAtExes;
    }

    public Extension skAtExes(Set<SkAtEx> skAtExes) {
        this.skAtExes = skAtExes;
        return this;
    }

    public Extension addSkAtEx(SkAtEx skAtEx) {
        this.skAtExes.add(skAtEx);
        skAtEx.setExtension(this);
        return this;
    }

    public Extension removeSkAtEx(SkAtEx skAtEx) {
        this.skAtExes.remove(skAtEx);
        skAtEx.setExtension(null);
        return this;
    }

    public void setSkAtExes(Set<SkAtEx> skAtExes) {
        this.skAtExes = skAtExes;
    }

    public ExtensionKey getExtensionKey() {
        return extensionKey;
    }

    public Extension extensionKey(ExtensionKey extensionKey) {
        this.extensionKey = extensionKey;
        return this;
    }

    public void setExtensionKey(ExtensionKey extensionKey) {
        this.extensionKey = extensionKey;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Extension)) {
            return false;
        }
        return id != null && id.equals(((Extension) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Extension{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", description='" + getDescription() + "'" +
            ", showOrder=" + getShowOrder() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
