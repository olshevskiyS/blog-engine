package ru.olshevskiy.blogengine.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "global_settings")
public class GlobalSetting {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "value", nullable = false)
    private String value;

    GlobalSetting(String code, String name, String value) {
        this.code = code;
        this.name = name;
        this.value = value;
    }
}