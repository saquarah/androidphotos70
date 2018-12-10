package com.example.photos70.model;

import java.io.Serializable;

public class Tag implements Serializable {
    private String type, value;
    /**
     * Initializes tag
     * @param type the name of the tag
     * @param value the value of the tag
     */
    public Tag(String type, String value) {
        super();
        this.type = type;
        this.value = value;
    }
    /**
     * Gets the name part of the tag
     * @return name part of the tag
     */
    public String getTag() {
        return type;
    }
    /**
     *
     * @return the value part of the tag
     */
    public String getValue() {
        return value;
    }
    /**
     * shows tag in the form of name=value
     */
    public String toString() {
        return type + "=" + value;
    }

    /**
     * Returns true if the object o is a tag with same value and name, case insensitive
     */
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag) o;
        if(this.type.toLowerCase().equals(tag.type.toLowerCase())  &&
                this.value.toLowerCase().equals(tag.value.toLowerCase())) {
            return true;
        }
        return false;
    }

}
