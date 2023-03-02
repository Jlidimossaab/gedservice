import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { DocumentService } from 'src/services/document-service';
import { LoanOfficerComponent } from './loan-officer/loan-officer.component';
import { LoanOfficerService } from 'src/services/loanOfficer-service';
import { DocumentComponent } from './document/document.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { PopupDocumentComponent } from './popup-document/popup-document.component';

@NgModule({
  declarations: [
    AppComponent,
    LoanOfficerComponent,
    DocumentComponent,
    PopupDocumentComponent
  ],
  imports: [
    NgbModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    DocumentService,
    LoanOfficerService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
