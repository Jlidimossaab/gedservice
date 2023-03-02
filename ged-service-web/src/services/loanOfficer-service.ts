import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { filter, map, Observable, take } from "rxjs";
import { LoanOfficer } from "src/models/LoanOfficer";

@Injectable()
export class LoanOfficerService {

    private loanOfficer?: Observable<LoanOfficer>;

    constructor(private httpClient: HttpClient) { }

    findByName(name: string): Observable<LoanOfficer> {
        this.loanOfficer = this.httpClient.get<LoanOfficer>('http://localhost:8081/ged-service/v1/loanOfficer/get/' + name);
        return this.loanOfficer;
    }
    public async getLoanOfficerId(): Promise<number> {
        const loanOfficer = await this.loanOfficer!.pipe(
          filter(loanOfficer => !!loanOfficer), // filter out undefined values
          take(1) // convert id to number
        ).toPromise();
        return Number(loanOfficer!.id);
      }

    public getLoanOfficerName() {
        return this.loanOfficer?.pipe(
            map(loanOfficer => loanOfficer.id)
        );
    }
}