package com.project.gream.common.enumlist.converter;

import com.project.gream.common.enumlist.Role;

import javax.persistence.Converter;

@Converter
public class RoleConverter extends EnumAttributeConverter<Role>{
    public RoleConverter() {super(Role.class);}
}
