import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TestScenario} from '../../models/test-scenario';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class TestScenarioService {

  constructor(private http: HttpClient) {
  }
  getAll(): Observable<TestScenario[]> {
    return this.http.get<TestScenario[]>('/api/test-scenarios');
  }

  getProjectById(id: number): Observable<TestScenario> {
    return this.http.get<TestScenario>(`/api/test-scenarios/${id}`);
  }
}
