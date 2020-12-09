import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TestScenario} from '../../models/test-scenario';
import {TestScenarioRequestResponse} from "../../models/test-scenario-request-response";

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

}
