package com.javainuse.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "loan_officer")
//@JsonIgnoreProperties({"documents"})
public class LoanOfficer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

//    @OneToMany(mappedBy = "loanOfficer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Document> documents;

}
