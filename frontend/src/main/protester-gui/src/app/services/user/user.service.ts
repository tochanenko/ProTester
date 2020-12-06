import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<any> {
    return this.http.get('/api/admin/profiles', httpOptions);
  }

  getUserById(id: any): Observable<any> {
    return this.http.get('/api/admin/profiles/' + id, httpOptions);
  }

  updateUser(id: any, user: any): Observable<any> {
    return this.http.put('/api/admin/profiles/' + id, user, httpOptions);
  }

  deactivateUser(id: any): Observable<any> {
    return this.http.post('/api/admin/profiles/' + id, httpOptions);
  }

}
