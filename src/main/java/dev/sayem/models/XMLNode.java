package dev.sayem.models;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XMLNode {
    private String name;
    private String label;

    private String value;

    private XMLNode parent;
    private List<XMLNode> children;

    public boolean hasChildren() {
        List<XMLNode> nodes = getChildren();
        return nodes != null && !nodes.isEmpty();
    }

    public void renameDuplicateChildren() {
        if (this.children == null) return;
        Map<String, List<XMLNode>> groupedChildren = this.children.stream().collect(Collectors.groupingBy(XMLNode::getName));
        groupedChildren.forEach((s, xmlNodes) -> {
            if (xmlNodes.size() >= 2) {
                for (int i = 0; i < xmlNodes.size(); i++) {
                    XMLNode n = xmlNodes.get(i);
                    int index = children.indexOf(n);
//                String name = i == 0 ? n.getName() : n.getName() + i;
                    String name = n.getName() + (i + 1);
                    n.setName(name);
                    children.set(index, n);
                }
            }
        });
//        System.out.println(groupedChildren.size());
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

    public XMLNode getParent() {
        return parent;
    }

    public void setParent(XMLNode parent) {
        this.parent = parent;
    }

    public List<XMLNode> getChildren() {
        return children;
    }

    public void setChildren(List<XMLNode> children) {
        this.children = children;
    }
}
