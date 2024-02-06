export class SearchDTO {
  constructor(
    public employeeName: string = '',
    public employerSurname: string = '',
    public governmentName: string = '',
    public governmentLevel: string = '',
    public fullText: string = '',
    public booleanQuery: string = '',
    public lawDoc: boolean = true,
    public contractDoc: boolean = true,
    public typeOfSearch: string = 'standard_search',
    //TODO: geo location 
  ) {}
}