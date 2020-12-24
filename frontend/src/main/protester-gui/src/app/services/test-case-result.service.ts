import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class TestCaseResultService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<any> {
    return this.http.get("/api/test-case-results/all", httpOptions);
  }

  getAllProjects(): Observable<any> {
    return this.http.get("/api/project")
  }

  getForProject(id: any): Observable<any> {
    return this.http.get('/api/test-case-results/project/' + id, httpOptions);
  }
}
