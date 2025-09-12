package com.tracker_app.tracker.DataSource_WorkConnect.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class site_tickets {
    @Id
    private Long id;
    private String queue_name;
    private String site_id;
    private String rt_url;
    
}
