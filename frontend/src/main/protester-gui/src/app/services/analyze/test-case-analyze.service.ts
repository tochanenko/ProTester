import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TestCaseModel} from '../../models/test-case/test-case.model';
import {TestCaseResultModel} from '../../models/run-analyze/result.model';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class TestCaseAnalyzeService {

  constructor(private http: HttpClient) {
  }

  loadTestCasesResults(id: number): Observable<TestCaseResultModel> {
    console.log('IN LOAD ' + id);
    return this.http.get<TestCaseResultModel>(`/api/test-case-results/${id}`, httpOptions);
  }

  getTestCaseById(id: number): Observable<TestCaseModel> {
    return this.http.get<TestCaseModel>(`/api/testCase/${id}`, httpOptions);
  }
}
