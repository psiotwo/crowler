package cz.sio2.crowler.model;

import cz.sio2.crowler.Namespaces;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(namespace = Namespaces.MAIN)
public enum PropertyType {
    ANNOTATION, DATA, OBJECT;
}
