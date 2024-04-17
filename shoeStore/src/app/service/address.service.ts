import { Injectable } from '@angular/core';
import { Environment } from '../environment/environment';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class AddressService {
  private apiUrl = `${Environment.apiBaseUrl}`;
  private jsonUrl = 'assets/data.json';
  private jsonCityUrl = 'assets/cities.json';
  private jsonDistrictUrl = 'assets/districts.json';
  private jsonWardUrl = 'assets/wards.json';

  private apiConfig = { headers: this.createHeader() }

  constructor(private http: HttpClient) { }

  private createHeader() {
    return new HttpHeaders({ 'Content-Type': 'application/json' });
  }

  getJsonDataAddress(): Observable<any> {
    return this.http.get<any>(this.jsonUrl);
  }

  // GoShip API

  getJsonDataCity(): Observable<any> {
    return this.http.get<any>(this.jsonCityUrl);
  }

  getJsonDataDistrict(): Observable<any> {
    return this.http.get<any>(this.jsonDistrictUrl);
  }

  getJsonDataWard(): Observable<any> {
    return this.http.get<any>(this.jsonWardUrl);
  }

  getDataWardByDistrictId(districtId: string): Observable<any> {
    const params = new HttpParams()
      .set('districtId', districtId);
    return this.http.get<any>(`${this.apiUrl}/api/go-ship/wards`, { params });
  }

  getRates(data: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/go-ship/rates`, data, this.apiConfig);
  }

}
