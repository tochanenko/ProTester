import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TestCaseModel} from '../../models/test-case/test-case.model';
import {TestCaseResultModel} from '../../models/run-analyze/result.model';


const httpOptions = {
  responseType  : 'arraybuffer' as 'json'
};

@Injectable({
  providedIn: 'root'
})
export class TestCaseAnalyzeService {

  constructor(private http: HttpClient) {
  }

  loadTestCasesResults(id: number): Observable<TestCaseResultModel> {
    return this.http.get<TestCaseResultModel>(`/api/test-case-results/${id}`);
  }

  getTestCaseById(id: number): Observable<TestCaseModel> {
    return this.http.get<TestCaseModel>(`/api/testCase/${id}`);
  }


  getImage(path: string): Observable<any> {
    return this.http.get(`/api/screenshots/${path}`, httpOptions);
  }
}
