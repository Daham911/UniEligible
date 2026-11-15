package lk.ac.ou.unieligible.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prerequisite_rule_groups")
public class PrerequisiteRuleGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_code", nullable = false)
    @JsonIgnoreProperties("ruleGroups")
    private Course course;

    @Column(name = "group_order", nullable = false)
    private Integer groupOrder;

    @Column(name = "logical_operator", nullable = false, length = 10)
    private String logicalOperator = "AND"; // "AND" or "OR"

    @Column(name = "description", length = 255)
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("group")
    private List<PrerequisiteRule> rules = new ArrayList<>();

    public PrerequisiteRuleGroup() {}

    public PrerequisiteRuleGroup(Course course, Integer groupOrder, String logicalOperator, String description) {
        this.course = course;
        this.groupOrder = groupOrder;
        this.logicalOperator = logicalOperator;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Integer getGroupOrder() { return groupOrder; }
    public void setGroupOrder(Integer groupOrder) { this.groupOrder = groupOrder; }

    public String getLogicalOperator() { return logicalOperator; }
    public void setLogicalOperator(String logicalOperator) { this.logicalOperator = logicalOperator; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<PrerequisiteRule> getRules() { return rules; }
    public void setRules(List<PrerequisiteRule> rules) { this.rules = rules; }

    public void addRule(PrerequisiteRule rule) {
        rules.add(rule);
        rule.setGroup(this);
    }
}

