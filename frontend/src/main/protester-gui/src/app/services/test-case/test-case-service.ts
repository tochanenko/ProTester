import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {DataSetRequestResponse} from "../../models/data-set-request-response";
import {TestCaseModel} from "../../modules/projects-menu/projects/view/test-cases/test-case.model";
import {TestCaseFilter} from "../../modules/projects-menu/projects/view/test-cases/test-case-filter";
import {TestCaseResponse} from "../../modules/projects-menu/projects/view/test-cases/test-case-response";
import {RunResultModel} from '../../modules/projects-menu/projects/view/test-cases/run-result.model';
import {EnvironmentModel} from '../../models/environment.model';
import {RunTestCaseModel} from '../../modules/projects-menu/projects/view/test-cases/run-test-case.model';


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
    return this.http.get<void>(`/api/test/${id}`, httpOptions);
  }

  isEnvRequired(projectId: number, testCaseId: number): Observable<boolean> {
    return this.http.get<boolean>(`/api/testCase/project/${projectId}/${testCaseId}`, httpOptions);
  }

  loadEnvironments(): Observable<EnvironmentModel[]> {
    return this.http.get<EnvironmentModel[]>('/api/environment', httpOptions);
  }

  findRunResultByID(id: number): Observable<RunResultModel> {
    return this.http.get<RunResultModel>(`/api/test/result/${id}`, httpOptions);
  }
}
