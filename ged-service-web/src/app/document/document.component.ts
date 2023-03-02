import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { DocumentService } from 'src/services/document-service';
import { Document } from 'src/models/Document';
import { LoanOfficerService } from 'src/services/loanOfficer-service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PopupDocumentComponent } from '../popup-document/popup-document.component';

@Component({
  selector: 'app-document',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.css']
})
export class DocumentComponent implements OnInit {
  @ViewChild('fileInput') fileInput?: ElementRef;
  formModal: any;

  documentsOnUpload = [
    new Document(),
    new Document(),
    new Document()
  ];
  selectedFiles?: Array<File | undefined> = [undefined, undefined, undefined];
  documents?: Observable<Document[]>;
  documentVersions?: Observable<Document[]>;
  retrievedDocument: any;

  docs!: Document[];

  documentService?: DocumentService;
  loanOfficer?: LoanOfficerService;

  constructor(private service: DocumentService, private modalService: NgbModal) {
    this.documentService = service;
    //console.log("from constructor i wanna see if the documents exists");
    console.log("this.documentsOnUpload");
    console.log(this.documentsOnUpload);
  }

  ngOnInit(): void {
    this.documents = this.service.getDocumentsObservable(true);
    this.documents.subscribe(docs => {
      console.log("Documents:");
      console.log(docs);
      this.docs = docs;

      docs.forEach(doc => {
        this.documentsOnUpload[this.documentService!.getIndex(doc!.category!)].name = doc.name;
        this.documentsOnUpload[this.documentService!.getIndex(doc!.category!)].date = doc.date;
        this.documentsOnUpload[this.documentService!.getIndex(doc!.category!)].category = doc.category;
      })
    });

  }

  openPopup(category: string) {
    //console.log("categoory is " + category);
    const modalRef = this.modalService.open(PopupDocumentComponent);

    modalRef.componentInstance.category = category;
  }

  onFileChanged(event: Event, index: number) {
    // Select File
    const target = event.target as HTMLInputElement;
    this.selectedFiles![index] = target.files![0];

    /* console.log("the index is here  :" + index)

    console.log("the file is below  :")
    console.log(this.documentService!.selectedFiles![index])

    console.log("the documents are below  :")
    console.log(this.documentService!.selectedFiles)
 */
    // add the properties of the file to the list
    this.documentsOnUpload[index].name = this.selectedFiles![index]?.name;
    this.documentsOnUpload[index].category = this.documentService?.getCategory(index);
    this.documentsOnUpload[index].date = new Date();

    /* console.log("the document is below with index :")
    console.log(this.documentService!.documentsOnUpload[index]) */
  }

  /*onUpload() {
    this.service.refreshDocumentsList();
  }*/

  /*onDelete(id: number) {
    this.documentService!.deleteDocument(id).subscribe(
      () => {
        console.log(`Document with id ${id} deleted successfully`),
          this.documentService!.refreshDocumentsList();
      },
      (error) => console.error(`Failed to delete document with id ${id}: ${error}`)
    );
  }*/

  onCancel() {
    this.selectedFiles = [undefined, undefined, undefined];

    this.documents!.subscribe(docs => {
      console.log("Documents:");
      console.log(docs);

      docs.forEach(doc => {
        this.documentsOnUpload[this.documentService!.getIndex(doc!.category!)].name = doc.name;
        this.documentsOnUpload[this.documentService!.getIndex(doc!.category!)].date = doc.date;
        this.documentsOnUpload[this.documentService!.getIndex(doc!.category!)].category = doc.category;
      })
      let categories = this.findMissingCategories(docs);
      categories.forEach(category => {
        this.documentsOnUpload[this.documentService!.getIndex(category!)] = new Document();
      });

    });
    console.log("DocumentsOnUpload ==> " + this.documentsOnUpload);
  }

  findMissingCategories(docs: Document[]): (string | undefined)[] {
    // Get all unique categories of documents
    const docCategories = Array.from(new Set(docs.map(doc => doc.category)));

    // Get all unique categories from the list of all categories
    const allCategories = Array.from(new Set(this.documentsOnUpload.map(doc => doc.category)));

    // Find the difference between all categories and document categories
    const missingCategories = allCategories.filter(cat => !docCategories.includes(cat));

    return missingCategories;
  }

}

