package com.work;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Administrator {
    public int administrator_id;
    public String administrator_name;
int age;
    public String sex;
    public String phone;
    public String city;
    public String username;
    public String password;
}
