package com.tracker_app.tracker.DataSource_NMS.Entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "store_info_changes")
public class store_info_changes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_id")
    private String siteId;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "type_of_change")
    private String typeOfChange;

    @Column(name = "previous_data")
    private String previousData;

    @Column(name = "new_data")
    private String newData;

}
