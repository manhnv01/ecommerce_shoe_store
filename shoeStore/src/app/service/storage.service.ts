import {Injectable} from '@angular/core';
import {Environment} from "../environment/environment";
import {HttpClient, HttpParams} from "@angular/common/http";
@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private api = `${Environment.apiBaseUrl}/images`;

  constructor(private http: HttpClient) {
  }

  deleteImage(imageName: string) {
    return this.http.delete(`${this.api}/${imageName}`);
  }
}
