import { LoanOfficer } from "./LoanOfficer";

export class Document {
    id?: number;
    date?: Date;
    category?: string;
    name?: string;
    path?: string;
    type?: string;
    status?: boolean;
    tag?: string;
    loanOfficer?: LoanOfficer;
    
  }