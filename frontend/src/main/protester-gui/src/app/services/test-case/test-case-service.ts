import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TestCaseModel} from '../../test-case/test-case.model';
import {TestCaseResponse} from '../../test-case/test-case-response';
import {TestCaseFilter} from '../../test-case/test-case-filter';
import {Injectable} from '@angular/core';
import {DataSetResponse} from "../../models/data-set-response";
import {DataSetRequestResponse} from "../../models/data-set-request-response";
import {RunTestCaseModel} from '../../test-case/run-test-case.model';
import {RunResultModel} from '../../test-case/run-result.model';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};
@Injectable({
  providedIn: 'root'
})
export class TestCaseService {
  constructor(private http: HttpClient) {
  }

  create(testCase: TestCaseModel): Observable<any> {
    return this.http.post('/api/testCase', testCase, httpOptions);
  }

  update(testCase: TestCaseModel): Observable<any> {
     return this.http.put('/api/testCase', testCase, httpOptions);
  }

  getAll(projectId: number, filter: TestCaseFilter): Observable<TestCaseResponse> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('testCaseName', filter.testCaseName);

    return this.http.get<TestCaseResponse>(`/api/testCase/project/${projectId}`, {params});
  }


  getFilterById(id: number): Observable<TestCaseModel> {
    return this.http.get<TestCaseModel>(`/api/testCase/${id}`);
  }

  getAllDataSets(): Observable<DataSetRequestResponse> {
    return this.http.get<DataSetRequestResponse>(`/api/dataset`);
  }
  deleteTestCase(id: number): Observable<any> {
   return this.http.delete<string>(`/api/testCase/${id}`);
  }

  saveTestCaseResult(runTestCase: RunTestCaseModel): Observable<RunResultModel> {
    return this.http.post<RunResultModel>('/api/test', runTestCase, httpOptions);
  }

  runTestCase(id: number): Observable<void> {
    console.log('00000000000000000000000000000000');
    return this.http.get<void>(`/api/test/${id}`, httpOptions);
  }
}
