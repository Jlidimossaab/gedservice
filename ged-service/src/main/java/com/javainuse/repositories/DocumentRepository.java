package com.javainuse.repositories;

import com.javainuse.models.Document;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<List<Document>> findByCategory(String tag);

    Optional<Document>findById(Long id);

    void deleteById(Long id);

    @Query("SELECT d FROM Document d WHERE d.status = :status AND d.loanOfficer.id = :loanOfficerId")
    List<Document> findByStatusAndLoanOfficerId(@Param("status") Boolean status, @Param("loanOfficerId")Long loanOfficerID);



    @Query("SELECT d FROM Document d WHERE d.category = :category AND d.status = :status AND d.loanOfficer.id = :loanOfficerId")
    List<Document> findDocumentsByCategoryAndStatusAndLoanOfficerID(@Param("category") String category, @Param("status") boolean status, @Param("loanOfficerId") Long loanOfficerID);
}