package com.im.BackendCore.Property;

import com.im.BackendCore.User.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tb_property")
@Data
@AllArgsConstructor
@NoArgsConstructor
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


}
