package com.im.BackendCore.Property;

import com.im.BackendCore.User.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tb_property")
@Getter
@Setter
public class PropertyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String description;
    private Integer value;
    private List<String> propertyPhotos;
    // Um imóvel só pode ser vinculada a uma pessoa
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserModel owner;
    // Um imóvel só pode ter um responsável a alugando
    @ManyToOne
    @JoinColumn(name = "lessor_id")
    private UserModel lessor;

    public PropertyModel() {
    }

    public PropertyModel(String name, String description, Integer value, List<String> propertyPhotos) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.propertyPhotos = propertyPhotos;
    }

}
