import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TestCaseResult} from './result.model';
import {TestCaseModel} from "../modules/projects-menu/projects/view/test-cases/test-case.model";

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
    console.log('IN LOAD ' + id);
    return this.http.get<TestCaseResult>(`/api/test-case-results/${id}`, httpOptions);
  }

  getTestCaseById(id: number): Observable<TestCaseModel> {
    return this.http.get<TestCaseModel>(`/api/testCase/${id}`, httpOptions);
  }
}
