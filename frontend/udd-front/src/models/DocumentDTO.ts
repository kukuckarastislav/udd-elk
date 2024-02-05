export class DocumentDTO {
  constructor(
    public employeeName: string = '',
    public employeeSurname: string = '',
    public governmentName: string = '',
    public governmentLevel: string = '',
    public title: string = '',
    public address: string = '',
    public typeOfDoc: string = '',
    public fileId: string = '',
    public lawText: string = '',
    public contractText: string = '',
  ) {}
}