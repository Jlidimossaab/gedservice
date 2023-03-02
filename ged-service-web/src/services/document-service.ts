import { HttpClient, HttpHeaders, HttpRequest } from '@angular/common/http'
import { Injectable } from '@angular/core';
import { Observable, startWith, Subject, switchMap } from 'rxjs';
import { Document } from 'src/models/Document';
import { LoanOfficerService } from './loanOfficer-service';
@Injectable()
export class DocumentService {

    
    loanOfficerService?: LoanOfficerService;
    message?: string;

    private refreshDocuments = new Subject<void>();

    constructor(private httpClient: HttpClient, service: LoanOfficerService) {
        this.loanOfficerService = service;
    }

    //Gets called when the user clicks on submit to upload the documents
    onUpload(documentsOnUpload: Document[], selectedFiles: Array<File | undefined>) {
        console.log(selectedFiles);

        for (let i = 0; i < selectedFiles.length; i++) {
            const uploadDocumentData = new FormData();
            const file = selectedFiles![i];
            if (file) {
                const category = documentsOnUpload[i].category ?? '';
                this.putDocument(documentsOnUpload[i].category!);
                uploadDocumentData.append('file', file, file.name);
                this.loanOfficerService?.findByName("JohnSmith").subscribe((loanOfficer) => {
                    uploadDocumentData.append('loanOfficer', JSON.stringify(loanOfficer));
                    uploadDocumentData.append('category', category);
                    //Make a call to the Spring Boot Application to save the document
                    this.httpClient.post('http://localhost:8081/ged-service/v1/document/upload', uploadDocumentData, { observe: 'response' })
                        .subscribe((response) => {
                            if (response.status === 200) {
                                this.message = 'Document uploaded successfully';
                                console.log("message is ==>", this.message );
                                this.refreshDocumentsList();
                                setTimeout(() => {
                                    this.message = '';
                                }, 3000);
                                
                            } else {
                                this.message = 'Document upload failed';
                                setTimeout(() => {
                                    this.message = '';
                                }, 3000);
                            }
                        });
                });
            }
        }
        selectedFiles = [undefined, undefined, undefined];
    }

    getDocumentsObservable(status: boolean): Observable<Document[]> {
        return this.refreshDocuments.pipe(
            startWith(null),
            switchMap(() => this.loanOfficerService!.getLoanOfficerId()),
            switchMap(loanOfficerId => this.getDocuments(status, loanOfficerId))
        );
    }

    getDocuments(status: boolean, LoanOfficerId: number): Observable<Document[]> {
        return this.httpClient.get<Document[]>('http://localhost:8081/ged-service/v1/document/byLoadOfficerIdAndStatus/get/' + status + '/' + LoanOfficerId);
    }

    getDocumentVersion(retrievedDocument:any ,id: number) {
        // Method to download the document
        this.httpClient.get("http://localhost:8081/ged-service/v1/document/get/" + id, { responseType: 'blob' }).subscribe(
            (data: any) => {
                console.log('Received data:', data);
                retrievedDocument = data;
                console.log("retrieved document is ",retrievedDocument);
                const fileExtension = data.type;
                console.log("fileExtension is" + fileExtension);
                this.openDocument(retrievedDocument,fileExtension);
            },
            (error: any) => {
                console.log(error);
            }
        );
    }

    refreshDocumentsList() {
        this.refreshDocuments.next();
    }


    // Method to retrieve the document from the back-end
    putDocument(category: string) {
        this.httpClient
            .put('http://localhost:8081/ged-service/v1/document/put/' + category, {
            }).subscribe(data => {
                console.log(data);
            });
    }

    // Method to open the retrieved document
    openDocument(retrievedDocument: any ,fileExtension: string) {
        const file = new Blob([retrievedDocument], { type: fileExtension });
        console.log("mimetypeee " + this.getMimeType(fileExtension!))
        const fileUrl = URL.createObjectURL(file);
        window.open(fileUrl, '_blank');
    }

    getCategory(index: number): string {
        switch (index) {
            case 0:
                return "facture";
            case 1:
                return "cin";
            case 2:
                return "personal"
            default:
                return "";
        }
    }
    getIndex(category: string) {
        switch (category) {
            case "facture":
                return 0;
            case "cin":
                return 1;
            case "personal":
                return 2;
            default:
                return -1;
        }
    }

    // Method to get the MIME type of the retrieved document
    getMimeType(fileExtension: string) {
        switch (fileExtension) {

            case 'pdf':
                return 'application/pdf';
            case 'docx':
                return 'application/vnd.openxmlformats-officedocument.wordprocessingml.document';
            case 'doc':
                return 'application/msword';
            case 'xlsx':
                return 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
            case 'xls':
                return 'application/vnd.ms-excel';
            case 'pptx':
                return 'application/vnd.openxmlformats-officedocument.presentationml.presentation';
            case 'ppt':
                return 'application/vnd.ms-powerpoint';
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png"
            case "gif":
                return "image/gif"
            case "bmp":
                return "image/bmp"
            default:
                return '';
        }
    }
    /*deleteDocument(id: number): Observable<any> {
        const url = 'http://localhost:8081/ged-service/v1/document/delete/' + id;

        return this.httpClient.delete(url);
    }*/
    getDocumentVersions(category: string, loanOfficerId: number): Observable<Document[]> {
        return this.httpClient.get<Document[]>('http://localhost:8081/ged-service/v1/document/byLoadOfficerIdAndStatusAndCategory/get/' + category + '/false/' + loanOfficerId);
    }

}