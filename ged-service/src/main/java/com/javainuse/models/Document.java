package com.javainuse.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "document")
public class Document {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "tag")
    private String tag;

    @Column(name = "name")
    private String name;

    @Column(name = "path",length = 3000)
    private String path;

    @Column(name = "type")
    private String  type;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "category")
    private String category;

    @ManyToOne
    @JoinColumn(name = "loanOfficerId")
    private LoanOfficer loanOfficer;

    public Document(String name, String type, String path, String tag, LocalDateTime date,Boolean status,String category, LoanOfficer loanOfficer) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.tag = tag;
        this.date = date;
        this.status = status;
        this.category = category;
        this.loanOfficer= loanOfficer;
    }
}
