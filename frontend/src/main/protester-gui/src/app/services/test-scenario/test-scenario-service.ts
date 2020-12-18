import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TestScenario} from '../../models/test-scenario';
import {TestScenarioRequestResponse} from "../../models/test-scenario-request-response";
import {ScenarioFilterModel} from "../../modules/projects-menu/test-scenario/search/scenario-filter.model";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class TestScenarioService {

  constructor(private http: HttpClient) {
  }
  getAll(): Observable<TestScenarioRequestResponse> {
    return this.http.get<TestScenarioRequestResponse>('/api/test-scenarios');
  }
  getById(id: number): Observable<TestScenario> {
    return this.http.get<TestScenario>(`/api/test-scenarios/${id}`);
  }

  create(scenario): Observable<any> {
    return this.http.post(`/api/test-scenarios`, scenario, httpOptions);
  }

  getAllWithFilter(filter: ScenarioFilterModel): Observable<TestScenarioRequestResponse[]> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('compoundName', String(filter.scenarioName));
    params = params.append('loadSteps', "false");
    return this.http.get<TestScenarioRequestResponse[]>("/api/test-scenarios", {params});
  }

  delete(id): Observable<any> {
    return this.http.delete<any>(`/api/test-scenarios/${id}`);
  }
}
