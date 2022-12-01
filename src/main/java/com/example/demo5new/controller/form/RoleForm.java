
package com.example.demo5new.controller.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleForm {

    private String id;
    private String roleName;
    private String roleDesc;

}


