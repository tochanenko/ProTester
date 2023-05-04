import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {DataSetRequestResponse} from '../../models/data-set-request-response';
import {TestCaseModel} from '../../models/test-case/test-case.model';
import {TestCaseFilter} from '../../models/test-case/test-case-filter';
import {TestCaseResponse} from '../../models/test-case/test-case-response';

import {EnvironmentModel} from '../../models/environment/environment.model';

import {ValidationDataSetResponseModel} from '../../models/run-analyze/validation-data-set-response.model';
import {RunResultModel} from '../../models/run-analyze/run-result.model';
import {RunTestCaseModel} from '../../models/run-analyze/run-test-case.model';


@Injectable({
  providedIn: 'root'
})
export class TestCaseService {
  constructor(private http: HttpClient) {
  }

  create(testCase: TestCaseModel): Observable<any> {
    return this.http.post('/api/testCase', testCase);
  }

  update(testCase: TestCaseModel): Observable<any> {
    return this.http.put('/api/testCase', testCase);
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
    return this.http.post<RunResultModel>('/api/test', runTestCase);
  }

  runTestCase(id: number): Observable<void> {
    return this.http.get<void>(`/api/test/${id}`);
  }

  isEnvRequired(scenarioId: number): Observable<boolean> {
    return this.http.get<boolean>(`/api/testCase/environment/${scenarioId}`);
  }

  loadEnvironments(id: number): Observable<EnvironmentModel[]> {
    return this.http.get<EnvironmentModel[]>(`/api/environment/findAll/${id}`);
  }

  findRunResultByID(id: number): Observable<RunResultModel> {
    return this.http.get<RunResultModel>(`/api/test/result/${id}`);
  }


  validateTestCaseDataSet(testCase: TestCaseModel): Observable<ValidationDataSetResponseModel> {
    return this.http.post<ValidationDataSetResponseModel>(`/api/test/validate`, testCase);
  }
}
