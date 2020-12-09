import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TestCaseModel} from '../../test-case/test-case.model';
import {TestCaseResponse} from '../../test-case/test-case-response';
import {TestCaseFilter} from '../../test-case/test-case-filter';
import {Injectable} from '@angular/core';

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
}
