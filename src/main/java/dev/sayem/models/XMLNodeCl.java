package dev.sayem.models;

import dev.sayem.utils.DateTimeUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLNodeCl {
    private String nodeId;
    private String name;
    private String label;

    private String value;
    private Map<String, String> attributes;

    private List<XMLNodeCl> children;

    public boolean hasChildren() {
        List<XMLNodeCl> nodes = getChildren();
        return nodes != null && !nodes.isEmpty();
    }


    public String getCode() {
        if (this.attributes == null) return null;
        return this.attributes.get("code");
    }

    public Date getValidFrom() {
        if (this.attributes == null) return null;
        String date = this.attributes.get("validFrom");
        return date == null ? null : DateTimeUtil.parse(date);
    }

    public Date getValidTill() {
        if (this.attributes == null) return null;
        String date = this.attributes.get("validTo");
        return date == null ? null : DateTimeUtil.parse(date);
    }

    public String getDescription() {
        if (this.attributes == null) return null;
        return this.attributes.get("description");
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        String text = label != null && !label.isEmpty() ? label : name;
        if (children == null || children.isEmpty())
            text += " : " + value;
        return text;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<XMLNodeCl> getChildren() {
        return children;
    }

    public void setChildren(List<XMLNodeCl> children) {
        this.children = children;
    }
}
