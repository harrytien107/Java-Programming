package com.project.codebasespringjpa.service.interfaces;

import com.project.codebasespringjpa.entity.RoleEntity;

public interface IRoleService {
    boolean exitsByName(String name);
    void create(String name);
}
