package com.weather.api.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
class BaseEntity {

    @Id
    private UUID id;
    private Timestamp createDate;
    private Timestamp changeDate;

    @OneToOne
    private User createUser;

    @OneToOne
    private User changeUser;
}
