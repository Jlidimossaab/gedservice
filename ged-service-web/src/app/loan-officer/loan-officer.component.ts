import { Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { LoanOfficer } from "src/models/LoanOfficer";
import { LoanOfficerService } from "src/services/loanOfficer-service";

@Component({
  selector: 'app-loan-officer',
  templateUrl: './loan-officer.component.html',
  styleUrls: ['./loan-officer.component.css']
})
export class LoanOfficerComponent implements OnInit {

  loanOfficer?:Observable<LoanOfficer>;


  constructor(private loanOfficerService: LoanOfficerService) { 
    this.loanOfficer= loanOfficerService.findByName("JohnSmith");
  }

  ngOnInit(): void {
    this.loanOfficer= this.loanOfficerService.findByName("JohnSmith");
  }

}
