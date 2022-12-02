package com.unvise.oop.entity;

import java.io.Serializable;

public interface Identifiable<ID extends Serializable> {
    ID getId();
}
