package com.yeoboya.lunch.api.v1.Item.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ItemEditor {

    private String name;
    private int price;

    public ItemEditor(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public static ItemEditorBuilder builder() {
        return new ItemEditorBuilder();
    }

    public static class ItemEditorBuilder {
        private String name;
        private int price;

        ItemEditorBuilder() {
        }

        public ItemEditorBuilder itemName(final String name) {
            if(name != null) {
                this.name = name;
            }
            return this;
        }

        public ItemEditorBuilder price(final int price) {
            if(price != 0) {
                this.price = price;
            }
            return this;
        }

        public ItemEditor build() {
            return new ItemEditor(this.name, this.price);
        }

        public String toString() {
            return "ItemEditor.ItemEditorBuilder(itemName=" + this.name + ", price=" + this.price + ")";
        }
    }
}
