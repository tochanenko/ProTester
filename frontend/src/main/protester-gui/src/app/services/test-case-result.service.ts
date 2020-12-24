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


  /*
    test_case_result_id SERIAL PRIMARY KEY,
    user_id INTEGER,
    test_case_id INTEGER,
    status_id INTEGER NOT NULL,
    start_date TIMESTAMPTZ NOT NULL,
    end_date TIMESTAMPTZ,
   */
}
