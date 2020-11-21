import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {StorageService} from "./auth/storage.service";
import {Observable} from "rxjs";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class PasswordService {

  constructor(private http: HttpClient, private storageService: StorageService) {
  }

  forgotPassword(user: any): Observable<any> {
    return this.http.post('/api/forgot-password', user, httpOptions);
  }

  confirmReset(token: any): Observable<any> {
    return this.http.get('/api/forgot-password/confirm-reset?t=' + token);
  }

  resetPassword(user: any): Observable<any> {
    return this.http.post('/api/forgot-password/reset-password', user, httpOptions)
  }


}
