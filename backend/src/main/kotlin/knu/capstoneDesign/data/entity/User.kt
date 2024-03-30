package knu.capstoneDesign.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class User(
    @Id
    var id:Int,

    @Column(length = 20)
    var name: String?
){
    constructor(id: Int): this(id=id, name=null)
}
