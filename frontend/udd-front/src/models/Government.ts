export class Government {
  constructor(
    public name: string = '',
    public govLevel: string = 'OPSTINSKA',
    public password: string = '',
    public numberOfEmployees: number = 0,

    // Address
    public street: string = '',
    public number: string = '',
    public city: string = '',
    public country: string = '',
    public postalCode: string = '',
  ) {}
}