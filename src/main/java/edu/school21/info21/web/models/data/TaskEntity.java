package edu.school21.info21.web.models.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Column(name = "title")
    @Id
    private String title;

    @Column(name = "parent_task")
    private String parentTask;

    @Column(name = "max_xp")
    private Integer maxXp;

}
