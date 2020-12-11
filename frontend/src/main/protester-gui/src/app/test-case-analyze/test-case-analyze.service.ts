import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Project} from '../models/project/project.model';
import {Observable} from 'rxjs';
import {TestCaseResult} from './result.model';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class TestCaseAnalyzeService {

  constructor(private http: HttpClient) {
  }

  loadTestCasesResults(id: number): Observable<TestCaseResult> {
    return this.http.get<TestCaseResult>('/api/test-case-analyze/load/{id}', httpOptions);
  }
}
