import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { DocumentService } from 'src/services/document-service';
import { LoanOfficerService } from 'src/services/loanOfficer-service';
import { Document } from 'src/models/Document';
@Component({
  selector: 'app-popup-document',
  templateUrl: './popup-document.component.html',
  styleUrls: ['./popup-document.component.css']
})
export class PopupDocumentComponent implements OnInit {

  documentService?: DocumentService;
  loanOfficerService?: LoanOfficerService
  documents?: Document[];
  
  retrievedDocument: any;

  private documentsSubscription?: Subscription;
  public category!:string;

  constructor(private modal: NgbActiveModal,service: DocumentService,serviceLoan : LoanOfficerService) { 
    this.documentService=service;
    this.loanOfficerService = serviceLoan;
  }

  async ngOnInit()  : Promise<void> {
    const loanOfficerId = await this.loanOfficerService!.getLoanOfficerId();
    this.documentsSubscription = this.documentService!.getDocumentVersions(this.category,loanOfficerId).subscribe(
      (docs: Document[]) => {
        this.documents = docs;
      }
    );
  }
  ngOnDestroy() {
    this.documentsSubscription!.unsubscribe();
  }

  close() {
    this.modal.close();
  }

}
