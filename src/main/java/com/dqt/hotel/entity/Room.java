package com.dqt.hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "room")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "hotel_id")
    private Integer hotelId;

    @Column(name = "description")
    private String description;

    @Column(name = "acreage")
    private Integer acreage;

    @Column(name = "member")
    private Integer member;

    @Column(name = "price")
    private Integer price;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "enabled")
    private Integer enabled;

    @Column(name = "status")
    private Integer status;

}